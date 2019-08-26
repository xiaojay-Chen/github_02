package cn.itcast.web.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 控制器
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    // 注入货物service
    @Reference
    private ContractProductService contractProductService;
    // 注入工厂service
    @Reference
    private FactoryService factoryService;


    /**
     * 分页列表查询
     * 1. 从购销合同列表，点击货物，进入货物列表和添加页面
     * 存储数据：工厂、货物、..
     * 请求地址：http://localhost:8080/cargo/contractProduct/list.do?contractId=
     * 相应地址：/cargo/product/product-list.jsp
     * @return
     */
    @RequestMapping("/list")
    public String list(String contractId,
                        @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "6")int pageSize){

        // 1.查询工厂
        FactoryExample factoryExample = new FactoryExample();
        // 1.1查询条件
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        // 2.查询购销合同id，查询货物
        ContractProductExample contractProductExample = new ContractProductExample();
        // 2.1查询条件
        contractProductExample.createCriteria().andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> pageInfo = contractProductService.findByPage(contractProductExample, pageNum, pageSize);

        // 3.保存数据
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("pageInfo",pageInfo);
        // 注意：这里需要保存购销合同id，主要是为了后面添加货物时候，要指定购销合同id
        request.setAttribute("contractId",contractId);
        return "/cargo/product/product-list";
    }


    /**
     * 保存货物添加/修改
     * 请求地址：http://localhost:8080/cargo/contractProduct/edit.do
     * 响应地址：跳转回/cargo/product/list.do
     *
     * 上传文件：提交的文件参数：
     *      <input type="file"  name="productPhoto" >
     * @param contractProduct
     * @return
     */
    // 注入上传到青牛云的工具类
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto){

        // 设置所属企业的id，名称
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());

        // 判断货物有没有id
        if (StringUtils.isEmpty(contractProduct.getId())){
            // 上传图片到七牛云
            if (contractProduct.getProductNo() != null){
                try {
                    String url = "Http://" + fileUploadUtil.upload(productPhoto);
                    // 保存
                    contractProduct.setProductImage(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 没有id，添加
            contractProductService.save(contractProduct);
        }else {
            // 有id。修改
            contractProductService.update(contractProduct);
        }

        // 跳转回货物列表
        // 列表有货物id参数。所以路径还要配上货物id参数
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
    }


    /**
     * 进入货物修改页面
     * @param id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        // 1.根据货物id查询
        ContractProduct contractProduct = contractProductService.findById(id);
        // 2.修改页面有生产厂家的选择，需要把生产厂家的集合保存到会话域中
        // 2.1 根据货物查询货物工厂
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("货物");

        List<Factory> factoryList = factoryService.findAll(factoryExample);

        // 3. 保存到会话域
        request.setAttribute("contractProduct",contractProduct);
        request.setAttribute("factoryList",factoryList);
        // 4. 跳转到货物管理页面
        return "/cargo/product/product-update";
    }


    /**
     * 删除货物
     * 按钮的请求地址：/cargo/contractProduct/delete.do?id=${o.id}&contractId=${o.contractId}
     * 需要两个参数，货物id，购销合同id
     * 响应地址：跳转回/cargo/product/list.do
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId){

        contractProductService.delete(id);
        // 跳转回货物列表
        // 列表有货物id参数。所以路径还要配上货物id参数
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    /**
     * ApachePOI实现货物上传（1）：进入上传货物页面
     * 请求地址：http://localhost:8080/cargo/contractProduct/toImport.do
     * @param contractId 购销合同id
     * @return
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId){

        request.setAttribute("contractId",contractId);
        return "/cargo/product/product-import";
    }

    /**
     * ApachePOI实现货物上传（1）：上传   读取excel--->封装对象--->调用service保存
     * 点击保存后台参数：contractId: 购销合同id
     *                  file: 文件
     * @param contractId
     * @param file
     * @return
     */
    @RequestMapping("/import")
    public String importExcel(String contractId,MultipartFile file) throws IOException {
        // 1.根据excel工作流，创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        // 2.获取工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 3.获取总行数
        int rows = sheet.getPhysicalNumberOfRows();
        // 4.遍历行
        for (int i = 1;i<rows;i++){
            // 4.1获取每一行
            Row row = sheet.getRow(i);

            // 4.2创建货物对象，把excel的每一行封装为一个货物对象
            ContractProduct cp = new ContractProduct();

            cp.setContractId(contractId);//注意要设置购销合同id
            cp.setFactoryName(row.getCell(1).getStringCellValue());
            cp.setProductNo(row.getCell(2).getStringCellValue());
            cp.setCnumber((int) row.getCell(3).getNumericCellValue());
            cp.setPackingUnit(row.getCell(4).getStringCellValue());
            cp.setLoadingRate(row.getCell(5).getNumericCellValue()+"");
            cp.setBoxNum((int) row.getCell(6).getNumericCellValue());
            cp.setPrice(row.getCell(7).getNumericCellValue());
            cp.setProductDesc(row.getCell(8).getStringCellValue());
            cp.setProductRequest(row.getCell(9).getStringCellValue());

            // 设置厂家id
            Factory factory = factoryService.findIdByFactoryName(cp.getFactoryName());
            if (factory != null){
                cp.setFactoryId(factory.getId());
            }
            // 保存货物
            contractProductService.save(cp);
        }

        return "/cargo/product/product-import";
    }
}
