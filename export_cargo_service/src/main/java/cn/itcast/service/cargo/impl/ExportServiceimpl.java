package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductResult;
import cn.itcast.vo.ExportResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 出口报运单业务实现
 */
@Service
public class ExportServiceimpl implements ExportService {

    // 注入报运单dao
    @Autowired
    private ExportDao exportDao;
    // 注入货物dao
    @Autowired
    private ContractProductDao contractProductDao;
    // 注入购销合同dao
    @Autowired
    private ContractDao contractDao;
    // 注入商品dao
    @Autowired
    private ExportProductDao exportProductDao;
    // 注入报运单附件dao
    @Autowired
    private ExtCproductDao extCproductDao;
    // 注入商品附件dao
    @Autowired
    private ExtEproductDao extEproductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    /**
     * 添加：生成出口报运单
     * @param export
     */
    @Override
    public void save(Export export) {
        // 1.设置报运单id
        export.setId(UUID.randomUUID().toString());
        // 1.1.设置制单时间
        export.setInputDate(new Date());
        // 1.2 获得多个购销合同id数组
        String[] array = export.getContractIds().split(",");
        // 1.3 获得合同号
        String contaractNos = "";
        // 遍历id数组
        for (String contractId : array) {
            // 根据购销合同id查询购销合同
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            // 获取合同号
            contaractNos += contract.getContractNo()+" ";

            // 修改购销合同状态为2
            contract.setState(2);
            // 更新购销合同
            contractDao.updateByPrimaryKeySelective(contract);
        }
        // 2. 设置合同号
        export.setCustomerContract(contaractNos);

        // 2.保存报运的商品
        // 需求：报运的商品数据来源：购销合同的货物
        /*
            定义一个map集合，存储货物id，商品id
            Map<货物id，商品id> map
         */
        Map<String,String> map = new HashMap<>();
        // 2.1根据购销合同id查询货物
        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria criteria = contractProductExample.createCriteria();
        criteria.andContractIdIn(Arrays.asList(array));
        List<ContractProduct> cpList = contractProductDao.selectByExample(contractProductExample);
        // 2.2 遍历货物，构造报运的商品
        for (ContractProduct contractProduct : cpList) { // 一个货物对应一个商品
            // 创建商品对象
            ExportProduct exportProduct = new ExportProduct();
            // 货物信息拷给商品
            BeanUtils.copyProperties(contractProduct,exportProduct);

            // 设置商品id和报运单id
            exportProduct.setId(UUID.randomUUID().toString());
            exportProduct.setExportId(export.getId());
            // 保存商品
            exportProductDao.insertSelective(exportProduct);
            // 存储货物id，以及对应的商品id
            map.put(contractProduct.getId(),exportProduct.getId());
        }
        // 3.保存报运的商品附件
        // 需求：报运单的商品附件数据来源：购销合同的附件
        // 3.1 查询附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdIn(Arrays.asList(array));
        List<ExtCproduct> cproductList = extCproductDao.selectByExample(extCproductExample);
        for (ExtCproduct extCproduct : cproductList) { // 一个购销合同的货物附件对应商品附件
            // 创建商品附件对象
            ExtEproduct extEproduct = new ExtEproduct();
            // 货物附件信息拷给商品附件
            BeanUtils.copyProperties(extCproduct,extEproduct);

            // 设置商品附件属性
            extEproduct.setId(UUID.randomUUID().toString());
            // 设置报运单id
            extEproduct.setExportId(export.getId());
            // 设置商品id
            extEproduct.setExportProductId(map.get(extCproduct.getContractProductId()));

            // 保存商品附件
            extEproductDao.insertSelective(extEproduct);
        }
        //4.保存报运单
        // 4.1设置报运单状态
        export.setState(0);
        // 设置商品数。附件数
        export.setProNum(cpList.size());
        export.setExtNum(cproductList.size());

        // 4.2保存报运单
        exportDao.insertSelective(export);
    }

    /**
     * 修改：出口报运单和货物列表
     * 货物列表是报运单的商品集合。
     * @param export
     */
    @Override
    public void update(Export export) {
        // 修改报运单
        exportDao.updateByPrimaryKeySelective(export);

        // 修改报运单商品集合
        // 1.获取报运单的商品集合
        List<ExportProduct> exportProducts = export.getExportProducts();
        // 2.判断和遍历
        if (exportProducts != null && exportProducts.size() > 0) {
            for (ExportProduct exportProduct : exportProducts) {
                // 修改商品
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<Export> list = exportDao.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public void updateExport(ExportResult exportresult) {
        // 1.修改报运单状态，备注，商品的交税金额
        // 1.1获取报运单
        String exportId = exportresult.getExportId();
        Export export = exportDao.selectByPrimaryKey(exportId);

        export.setState(exportresult.getState());
        export.setRemark(exportresult.getRemark());

        // 修改报运单
        exportDao.updateByPrimaryKeySelective(export);

        // 修改商品信息
        Set<ExportProductResult> products = exportresult.getProducts();
        // 判断和遍历
        if (products != null && products.size() > 0){
            for (ExportProductResult product : products) {
                // 创建请求的商品对象
                ExportProduct exportProduct = new ExportProduct();
                // 修改商品id和收税金额
                exportProduct.setId(product.getExportProductId());
                exportProduct.setTax(product.getTax());

                // 保存
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }
}
