package cn.itcast.web.controller.company;


import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/company")
public class CompanyController extends BaseController {

    @Reference
    private CompanyService companyService;


    // 1.查询所有的企业
    @RequestMapping(value = "/list",name = "企业列表")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){

        /*
        注意：1. 这里的value表示请求路径，可以省略；
        2. name只是给路径起名字，增加代码的可读性，也是可选的
         */
//        List<Company> list = companyService.findAll();
//        request.setAttribute("list",list);
        // 企业列表分页
        PageInfo<Company> pageInfo = companyService.findByPage(pageNum, pageSize);
        request.setAttribute("pageInfo",pageInfo);
        return "company/company-list";
    }


    /* 2.添加 新建按钮进入添加页面功能
        功能入口：列表点击添加按钮
        请求地址：http://localhost:8080/company/toAdd.do
        响应地址：/WEB-INF/pages/company/company-add.jsp
    */
    @RequestMapping(value = "/toAdd")
    public String toAdd(){
        return "company/company-add";
    }

    /*
    保存或修改企业信息（都是封装数据）
    1.如果请求参数中没有传入id，说明事添加操作
    2.如果请求参数有传入id，说明是修改操作
     */
    @RequestMapping("/edit")
    public String edit(Company company){
        // 判断
        //1.如果请求参数中没有传入id，说明事添加操作
        if (StringUtils.isEmpty(company.getId())){
            companyService.save(company);
        }
        // 2.如果请求参数有传入id，说明是修改操作
        else {
            companyService.upDate(company);
        }

        // 重定向到企业列表
        return "redirect:/company/list.do";
    }


    // 进入修改页面
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        // 根据id查询
        Company company = companyService.findById(id);

        //返回
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("company/company-update");
        modelAndView.addObject("company",company);
        return modelAndView;
    }

    // 删除
    @RequestMapping("/delete")
    public String delete(String id){

        companyService.delete(id);
        return "redirect:/company/list.do";
    }





//    // 保存
//    // 统一异常处理
//    @RequestMapping("/save")
//    public String save(Date date){
//        //int i = 10/0;
//        System.out.println(date);
//        return "success";
//        //return "error";
//    }
}
