package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ModuleService moduleService;

    /*
        分页查询角色
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){
        // 初始化当前登录用户所属的企业id为1
        String companyId = getLoginCompanyId();

        // 1.调用service查询部门列表
        PageInfo<Role> pageInfo = roleService.findByPage(companyId, pageNum, pageSize);

        request.setAttribute("pageInfo",pageInfo);
        return "system/role/role-list";

    }

    /*
        进入角色新增页面
        请求地址：http://localhost:8080/system/role/toAdd.do
     */
    @RequestMapping("/toAdd")
    public String toAdd(){

        return "/system/role/role-add";
    }

    /*
        进入角色修改页面
        请求地址：http://localhost:8080/system/role/toUpdate.do
        参数：id
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据id查找角色
        Role role = roleService.findById(id);
        // 放进request
        request.setAttribute("role",role);

        return "/system/role/role-update";
    }

    /*
        添加保存角色/修改角色
        请求地址：http://localhost:8080/system/role/edit.do
     */
    @RequestMapping("/edit")
    public String edit(Role role){
        // 模拟获取当前登录用户的数据
        // 初始化当前登录用户的企业id和企业名
        String comapnyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        // 放入角色中
        role.setCompanyId(comapnyId);
        role.setCompanyName(companyName);
        // 判断当前角色是否有id，有则为修改
        if (StringUtils.isEmpty(role.getId())){
            roleService.save(role);
        }else {
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    /*
        删除角色
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,String> delete(String id){

        Map<String, String> result = new HashMap<>();

        boolean falg = roleService.delete(id);
        if (falg){
            result.put("message","删除成功");
        }else {
            result.put("message","角色正在被其他用户使用");
        }
        return result;
    }

    /*
        角色分配权限（1）：进入角色权限
        地址：http://localhost:8080/system/role/roleModule.do
        参数：角色id：?roleid=
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleId){
        // 根据id查找角色
        Role role = roleService.findById(roleId);

        request.setAttribute("role",role);

        return "/system/role/role-module";
    }

    /*
        角色分配权限（2）：role-module.jsp页面发送ajax请求，返回ztree需要的接送格式数据
        请求地址在role-module.jsp：/system/role/getZtreeNodes.do
        请求参数：roleId
        响应的数据：
            json = [
                { id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
            ]这里【】是list集合，，里面的元素都是键对值的Map集合。
     */
    @RequestMapping("/getZtreeNodes")
    @ResponseBody   //返回json格式数据
    public List<Map<String,Object>> getZtreeNodes(String roleId){
        // 1.返回的集合
        List<Map<String,Object>> result = new ArrayList<>();

        // 2.查询所有权限
        List<Module> moduleList = moduleService.findAll();

        // 3.查询当前角色已经拥有的权限（checked:true）
        List<Module> roleModuleList = moduleService.findModuleByRoleId(roleId);

        // 4.遍历所有权限，构造json格式数据
        if (moduleList != null && moduleList.size() > 0){
            for (Module module : moduleList) {
                // 4.1构造Map
                Map<String,Object> map = new HashMap<>();
                // 4.2封装
                map.put("id",module.getId());
                map.put("pId",module.getParentId());
                map.put("name",module.getName());
                map.put("open",true);

                // 4.3 判断：当前角色如果已经拥有遍历到的权限，就checked：true
                if (roleModuleList.contains(module)){
                    map.put("checked",true);
                }

                // 4.4 map添加到List集合
                result.add(map);
            }
        }

        // 5.返回封装的数据：所有权限，角色已经拥有的权限
        return result;
    }

    /**
     * 角色分配权限（3）实现分配权限
     * @param roleId 角色id
     * @param moduleIds 权限id
     * @return
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleId,String moduleIds){
        // 1.调用service完成权限分配
        roleService.updateRoleModule(roleId,moduleIds);
        // 2.跳转到角色列表
        return "redirect:/system/role/list.do";
    }

}
