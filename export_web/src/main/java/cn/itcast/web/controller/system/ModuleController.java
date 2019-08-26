package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Module 控制器
 */
@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;


    /**
     * 分页查询全部
     * 请求地址：http://localhost:8080/system/module/list.do
     * 默认赋予当前页和页大小数据
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){

        // 调用service方法查询列表
        PageInfo<Module> pageInfo = moduleService.findByPage(pageNum, pageSize);

        // 放进请求域中
        request.setAttribute("pageInfo",pageInfo);
        // 返回
        return "/system/module/module-list";

    }

    /**
     * 添加保存/修改更新
     * 地址：http://localhost:8080/system/module/edit.do
     * @param module
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Module module){
        // 判断请求是否有id，有是修改，无是添加
        if (StringUtils.isEmpty(module.getId())){
            moduleService.save(module);
        }else {
            moduleService.update(module);
        }
        return "redirect:/system/module/list.do";
    }

    /**
     * 进入新增页面
     * 地址：http://localhost:8080/system/module/toAdd.do
     * @param
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        // 查询所有模块数据
        List<Module> list = moduleService.findAll();
        // 放进请求域中
        request.setAttribute("menus",list);
        // 返回
        return "/system/module/module-add";
    }

    /**
     * 进入修改页面
     * 地址：http://localhost:8080/system/module/toUpdate.do
     * @param id  模块id：?id=
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据id获取当前模块的数据
        Module module = moduleService.findById(id);
        // 查询所有的模块
        List<Module> list = moduleService.findAll();

        // 放进请求域中
        request.setAttribute("menus",list);
        request.setAttribute("module",module);
        // 返回
        return "/system/module/module-update";
    }

    /**
     * 根据id删除模块
     * @param id 模块id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody //返回json
    public Map<String,String> delete(String id){
        // 返回的对象
        Map<String,String> result = new HashMap<>();
        // 调用service方法
        boolean flag = moduleService.delete(id);

        // 判断
        if (flag){
            result.put("message","删除成功！");
        }else {
            result.put("message","删除失败，当前模块被其他数据引用");
        }

        // 跳转到模块列表
        return result;
    }
}
