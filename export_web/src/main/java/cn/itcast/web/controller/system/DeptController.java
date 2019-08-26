package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
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
 * 部门控制器
 */
@Controller
@RequestMapping(value = "/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    /**
     * 1.部门列表分页
     *   参数给默认值
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){

        // 分页查询部门要根据企业id查询，而企业id是根据登陆用户获取其对应的企业id
        // 现在先写死
        String companyId = getLoginCompanyId();

        // 调用service查询
        PageInfo<Dept> pageInfo = deptService.findByPage(companyId, pageNum, pageSize);

        // 返回
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/dept/dept-list");
        mv.addObject("pageInfo",pageInfo);
        return mv;
    }


    /**
     * 2.部门添加：进去部门新建页面
     * 地址：http://localhost:8080/system/dept/toAdd.do
     * @return
     */
    @RequestMapping("/toAdd")
    public ModelAndView toAdd(){
        //模拟获取当前登录用户的数据
        //初始化当前登录用户所属的企业ID为1
        String companyId = getLoginCompanyId();

        // 查询所有部门，作为页面下拉列表显示
        List<Dept> deptList =  deptService.findAll(companyId);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/dept/dept-add");
        mv.addObject("deptList",deptList);
        return mv;
    }

    /**
     * 3.添加保存部门/修改部门
     * 请求地址：http://localhost:8080/system/dept/edit.do
     */
    @RequestMapping("/edit")
    public String edit(Dept dept){
        /*
            模拟获取当前登录用户的数据
            初始化当前登录用户所属的企业id和企业名
         */
        String companyId =getLoginCompanyId();
        String companyName = getLoginCompanyName();

        dept.setCompanyId(companyId);
        dept.setCompanyName(companyName);
        // 判断是否有id,没有则是添加
        if (StringUtils.isEmpty(dept.getId())){
            // 添加
            deptService.save(dept);
        } else {
            // 修改
            deptService.update(dept);
        }
        return "redirect:/system/dept/list.do";
    }

    /**
     * 4.进入修改页面
     * 请求地址：http://localhost:8080/system/dept/toUpdate.do?id=
     * 1）根据id查询
     * 2）查询所有的部门，页面下拉列表显示
     * @return
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        // 模拟获取当前登录用户的数据
        // 初始化当前登录用户所属的企业ID为id
        String companyId = getLoginCompanyId();

        // 根据id进行查询
        Dept dept = deptService.findById(id);
        // 查询所有部门
        List<Dept> deptList = deptService.findAll(companyId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("dept",dept);
        mv.addObject("deptList",deptList);
        mv.setViewName("/system/dept/dept-update");
        return mv;
    }

    /**
     * 5.删除部门
     * ajax请求地址：/system/dept/delete.do
     * 请求参数：当前模拟为100
     * 响应数据：{message：删除成功/失败}
     */
    @RequestMapping("/delete")
    @ResponseBody   //返回json
    public Map<String,String> delete(String id){
        // 返回的对象
        Map<String,String> result = new HashMap<>();
        // 调用service
        boolean flag = deptService.delete(id);
        // 判断
        if (flag){
            result.put("message","删除成功");
        }else {
            result.put("message","删除失败，当前部门下有子部门不能删除！");
        }
        return result;
    }
}
