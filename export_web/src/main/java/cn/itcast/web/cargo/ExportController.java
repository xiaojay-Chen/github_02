package cn.itcast.web.cargo;

import cn.itcast.dao.cargo.ExportProductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductVo;
import cn.itcast.vo.ExportResult;
import cn.itcast.vo.ExportVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 购销合同下合同管理模块控制器
 */
@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    // 注入购销合同service
    @Reference
    private ContractService contractService;
    // 注入报运单service
    @Reference
    private ExportService exportService;
    // 注入报运单货物service
    @Reference
    private ExportProductService exportProductService;

    /**
     * 1.合同管理（1）：进入合同管理页面
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        // 1.构造查询条件
        ContractExample contractExample = new ContractExample();
        // 1.1 根据cream_time进行降序
        contractExample.setOrderByClause("create_time desc");

        // 2.查询条件对象
        ContractExample.Criteria criteria = contractExample.createCriteria();
        // 2.1查询企业id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        // 2.2按照合同的状态查询
        criteria.andStateEqualTo(1);

        // 3.调用service分页查询
        PageInfo<Contract> pageInfo = contractService.findByPage(contractExample, pageNum, pageSize);
        // 3.1保存
        request.setAttribute("pageInfo",pageInfo);

        return "/cargo/export/export-contractList";
    }

    /**
     * 2.合同管理（2）：点击报运进入新增出口报运单页面
     * 请求地址：http://localhost:8080/cargo/export/toExport.do
     * String id：选择多个id，会自动用逗号隔开
     * @param id
     * @return
     */
    @RequestMapping("/toExport")
    public String toExport(String id){

        request.setAttribute("id",id);
        return "/cargo/export/export-toExport";
    }

    /**
     *  3.合同管理（3）：新增/修改出口报运单
     * 请求地址：http://localhost:8080/cargo/export/edit.do
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Export export){
        // 1.设置合同所属企业id。名称
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());
        //2. 判断
        if (StringUtils.isEmpty(export.getId())){
            //id为空，是新增
            exportService.save(export);
        }else {
            //id不为空，是修改
            exportService.update(export);
        }
        // 跳转回合同管理页面
        return "redirect:/cargo/export/list.do";
    }


    /**
     * 4.出口报运模块：进入出口报运页面
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){
        // 1.获得构造条件
        ExportExample exportExample = new ExportExample();
        // 2.构造对象
        ExportExample.Criteria criteria = exportExample.createCriteria();

        // 3.设置企业id，名称
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        criteria.andCompanyNameEqualTo(getLoginCompanyName());

        // 4.调用service
        PageInfo<Export> pageInfo = exportService.findByPage(exportExample, pageNum, pageSize);

        // 5. 保存
        request.setAttribute("pageInfo",pageInfo);
        return "/cargo/export/export-list";

    }

    /**
     * 5.出口报运模块：进入出口报运修改页面
     * 请求地址：http://localhost:8080/cargo/export/toUpdate.do
     * @param id 报运单id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,
                           @RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "5") int pageSize){
        // 1.根据id查询报运单
        Export export = exportService.findById(id);

        // 2.显示报运单货物列表
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProductList = exportProductService.findAll(exportProductExample);
        // 保存
        request.setAttribute("export",export);
        request.setAttribute("eps",exportProductList);
        return "/cargo/export/export-update";
    }

    /* 6.报运单的 提交 与 取消 */

    /**
     * 6.1:报运单的提交
     * 请求地址：http://localhost:8080/cargo/export/submit.do
     * 状态：1 为提交，0为取消
     * @param id    报运单id
     * @return
     */
    @RequestMapping("/submit")
    public String submit(String id){
        // 1.根据报运单id查询报运单
        Export export = exportService.findById(id);
        // 2.设置状态为1
        export.setState(1);
        // 3.设置主键(更新条件)
        export.setId(id);
        // 4.更新
        exportService.update(export);
        // 提交后重定向到报运列表页面
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 6.2:报运单取消提交
     * 请求地址：http://localhost:8080/cargo/export/cancel.do
     * 状态：1 为提交，0为取消
     * @param id 报运单id
     * @return
     */
    @RequestMapping("/cancel")
    public String cancel(String id){
        // 1.根据报运单id查询报运单
        Export export = exportService.findById(id);
        // 2.设置状态为0
        export.setState(0);
        // 3.设置主键(更新条件)
        export.setId(id);
        // 4.更新
        exportService.update(export);
        // 取消后重定向到报运列表页面
        return "redirect:/cargo/export/list.do";
    }


    /**
     *  7.电子报运：远程调用海关的报运平台
     * 请求地址：http://localhost:8080/cargo/export/exportE.do
     * @param id
     * @return
     */
    @RequestMapping("/exportE")
    public String exportE(String id){
        /* 封装请求的报运单和报运的商品 */
        // 7.1.准备webservice请求的对象：封装的报运单
        ExportVo exportVo = new ExportVo();
        // 7.1.1.根据报运单id查询报运单中的商品
        Export export = exportService.findById(id);
        // 7.1.2.拷贝信息
        BeanUtils.copyProperties(export,exportVo);
        // 7.1.3.设置属性
        exportVo.setExportId(id);

        // 7.2.封装报运的商品集合
        List<ExportProductVo> products = exportVo.getProducts();
        // 7.2.1 根据报运单id查询要封装的商品
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProductList = exportProductService.findAll(exportProductExample);
        // 7.2.2 判断和遍历要封装的商品集合
        if (exportProductList != null && exportProductList.size() > 0){
            for (ExportProduct exportProduct : exportProductList) {
                // 创建报运的商品集合
                ExportProductVo exportProductVo = new ExportProductVo();
                // 拷贝
                BeanUtils.copyProperties(exportProduct,exportProductVo);
                // 设置属性
                exportProductVo.setId(exportProduct.getId());
                exportProductVo.setExportId(id);

                // 添加到报运的商品集合
                products.add(exportProductVo);
            }
        }

        // 7.3.电子报运（1）远程访问海关报运平台，保存报运结果到海关平台的数据库
        WebClient
                .create("http://192.168.190.1:9001/ws/export/user")
                .post(exportVo);
        // 7.4.电子报运（2）远程访问海关报运平台，查询报运结果
        ExportResult exportResult = WebClient
                .create("http://192.168.190.1:9001/ws/export/user/" + id)
                .get(ExportResult.class);
        // 7.5.修改saasExport货代云平台的数据库，报运单状态，备注，商品交税金额
        exportService.updateExport(exportResult);
        // 重定向到列表
        return "redirect:/cargo/export/list.do";
    }


}
