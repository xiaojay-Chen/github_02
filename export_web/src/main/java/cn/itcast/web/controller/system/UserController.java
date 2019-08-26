package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    // 查询用户所属部门
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;



    /**
     * 用户列表分页
     * 请求地址：http://localhost:8080/system/user/list.do
     */
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){

//        // 认证授权后，获取subject对象
//        Subject subject = SecurityUtils.getSubject();
//        // 权限校验（访问当前用户列表，需要权限：“用户管理”）
//        subject.checkPermission("用户管理");


        String companyId = getLoginCompanyId();

        // 1.调用service查询用户列表
        PageInfo<User> pageInfo = userService.findByPage(companyId, pageNum, pageSize);
        // 2.将用户列表保存到request域中
        request.setAttribute("pageInfo",pageInfo);

        // 3.跳转到对象的页面
        return "/system/user/user-list";
    }

    /**
     * 进入用户新增页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        String companyId = getLoginCompanyId();
        // 查询所有部门
        // 用户新增页面需要填写部门
        List<Dept> deptList = deptService.findAll(companyId);
        // 存入request域中
        request.setAttribute("deptList",deptList);
        return "system/user/user-add";
    }

    /*
        进入用户修改页面
        请求地址：http://localhost:8080/system/user/toUpdate.do
        参数为：用户id：?id=
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        String companyId = getLoginCompanyId();
        // 根据id查询
        User user = userService.findById(id);
        // 查询所属部门
        List<Dept> deptList = deptService.findAll(companyId);
        // 放入request中
        request.setAttribute("user",user);
        request.setAttribute("deptList",deptList);
        return "system/user/user-update";
    }


    /**
     * 添加保存用户/修改用户
     * 请求地址：http://localhost:8080/system/user/edit.do
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/edit")
    public String edit(User user){
        /*
            模拟获取当前登录用户的数据
            初始化当前登录用户所属的企业id和企业名
         */
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        /*
            放入用户中
         */
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        /* 判断是否有id，有则为添加，没有则是修改 */
        if (StringUtils.isEmpty(user.getId())){
                userService.save(user);
                // 添加用户成功，发送邮件
            if (user.getEmail() != null && !"".equals(user.getEmail())){
                String to = user.getEmail();
                // 处理发送邮件的业务
                String subject = "新员工入职通知";
                String content = "欢迎你来到SaasExport大家庭，我们是一个充满激情的团队，不是996哦！";

                // 发送消息
               Map<String, String> map = new HashMap<>();
               map.put("to",to);
               map.put("subject",subject);
               map.put("content",content);
               rabbitTemplate.convertAndSend("msg.email",map);
            }
        }else {
            userService.update(user);
        }
        return "redirect:/system/user/list.do";
    }

    /*
        删除用户
     */
    @RequestMapping("/delete")
    @ResponseBody //返回json
    public Map<String, String> delete(String id){
        // 返回对象
        HashMap<String, String> result = new HashMap<>();
        // 调用service方法
        boolean flag = userService.delete(id);
        // 判断
        if (flag){
            result.put("message","删除成功");
        }else {
            result.put("message","删除失败，当前用户被其他数据引用！");
        }
        return result;
    }

    /**
     * 用户进入角色列表页面
     * 请求地址：localhost:8080/system/user/roleList.do
     * 跳转页面：user-role.jsp
     * @param id 用户id
     * @return
     */
    @RequestMapping("/roleList")
    public String roleList(String id){
        // 1. 查询所有的角色
        List<Role> roleList = roleService.findAll(getLoginCompanyId());

        // 2. 根据用户id查询用户
        User user = userService.findById(id);
        // 3.根据用户id查询用户已经拥有的所有角色集合
        List<Role> userRoles = roleService.findUserRoleById(id);
        // 4.定义一个角色字符串，保存用户的所有角色，用逗号隔开
        String userRoleStr = "";
        // 遍历集合
        for (Role userRole : userRoles) {
            userRoleStr += userRole.getId() + ",";
        }

        // 5. 保存数据到请求域
        request.setAttribute("user",user);
        request.setAttribute("userRoleStr",userRoleStr);
        request.setAttribute("roleList",roleList);

        return "system/user/user-role";
    }


    /**
     * 实现给用户分配角色
     * 返回地址：http://localhost:8080/system/user/list.do
     * @param userId 用户id
     * @param roleIds 角色id。可以选多个，用数组
     * @return
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userId,String[] roleIds){

        // 1.调用service分配角色
        userService.updateUserRole(userId,roleIds);
        // 2.页面跳转
        return "redirect:/system/user/list.do";
    }

}
