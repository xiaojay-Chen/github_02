package cn.itcast.web.cargo;

import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 购销合同中货物的附件功能
 */
@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController{

    // 注入附件service
    @Reference
    private ExtCproductService extCproductService;
    // 注入工厂service
    @Reference
    private FactoryService factoryService;

    /**
     * 进入购销合同货物的附件页面
     * 请求地址：http://localhost:8080/cargo/extCproduct/list.do
     * 响应地址：/cargo/extCproduct/list.do
     * @param contractId 后效合同id
     * @param contractProductId 货物id
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @RequestMapping("/list")
    public String list(String contractId, String contractProductId,
                       @RequestParam(defaultValue = "1")Integer pageNum,
                       @RequestParam(defaultValue = "6") Integer pageSize){

        // 1.查询全部工厂
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        // 保存
        request.setAttribute("factoryList",factoryList);

        // 2.分页显示附件列表
        // 2.1查询对象
        ExtCproductExample extCproductExample = new ExtCproductExample();
        // 2.2根据货物id查询所有附件
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        PageInfo<ExtCproduct> pageInfo = extCproductService.findByPage(extCproductExample, pageNum, pageSize);

        // 2.3 保存
        request.setAttribute("pageInfo",pageInfo);
        // 3 把当前附件页面的购销合同id和货物id保存到请求域
        // 注意：主要是为了后面添加附件的时候，要指定属于哪个购销合同和哪个货物
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);

        return "/cargo/extc/extc-list";
    }

    /**
     * 添加/保存
     * 请求地址：http://localhost:8080/cargo/extCproduct/edit.do
     * @return
     */
    // 注入上传到七牛云的工具类
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto){
        // 设置企业id，名称
        extCproduct.setCompanyId(getLoginCompanyId());
        extCproduct.setCompanyName(getLoginCompanyName());


        // 判断
        if (StringUtils.isEmpty(extCproduct.getId())){
            // 判断附件是否为空,否则上传附件图片
            if (extCproduct.getCnumber() != null){
                try {
                    String url = "Http://" + fileUploadUtil.upload(productPhoto);

                    extCproduct.setProductImage(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 没有id，是新增附件
            extCproductService.save(extCproduct);
        }else {
            // 否则是修改附件
            extCproductService.update(extCproduct);
        }

        // 跳转
        return "redirect:/cargo/extCproduct/list.do?contractId="+
                extCproduct.getContractId()+"&contractProductId="+
                extCproduct.getContractProductId();
    }

    /**
     * 附件修改：进入修改页面
     * 请求地址：/cargo/extCproduct/toUpdate.do
     * 参数：id=?
     * @param id 附件id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        /* 1.根据id查询附件 */
        ExtCproduct extCproduct = extCproductService.findById(id);
        /* 2.查询附件工厂 */
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        request.setAttribute("extCproduct",extCproduct);
        request.setAttribute("factoryList",factoryList);

        return "/cargo/extc/extc-update";
    }

    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId){

        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+
                contractId+"&contractProductId="+contractProductId;
    }
}
