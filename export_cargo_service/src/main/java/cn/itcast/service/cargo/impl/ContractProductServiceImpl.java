package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.vo.ContractProductVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 *  购销合同货物模块服务接口实现
 */
@Service(timeout = 100000)
public class ContractProductServiceImpl implements ContractProductService {

    // 注入货物dao
    @Autowired
    private ContractProductDao contractProductDao;
    // 注入购销合同dao
    @Autowired
    private ContractDao contractDao;
    // 注入附件Dao
    @Autowired
    private ExtCproductDao extCproductDao;

    /**
     * 分页查询列表
     * @param contractProductExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample contractProductExample, int pageNum, int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 查询所有货物
        List<ContractProduct> list = contractProductDao.selectByExample(contractProductExample);

        //返回
        return new PageInfo<>(list);
    }

    /**
     * 查询所有
     * @param contractProductExample 分页查询的参数
     * @return
     */
    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    /**通过id查询
     *
     * @param id 货物id
     * @return
     */
    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    /**
     * 添加保存
     * @param contractProduct
     */
    @Override
    public void save(ContractProduct contractProduct) {
        // 0. 给新添加的货物设置id
        contractProduct.setId(UUID.randomUUID().toString());

        // 1.计算添加的货物的总金额:数量 * 单价
        // 初始化总金额,判断货物数量和单价都不为null
        Double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null) {

            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        // 保存总金额
        contractProduct.setAmount(amount);

        // 2.修改购销合同总金额，货物件数
        // 2.1 根据货物中的合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        // 2.2 修改总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        // 2.3 修改货物数量
        contract.setProNum(contract.getProNum() + 1);
        // 2.4 更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);

        contractProductDao.insertSelective(contractProduct);
    }

    /**
     * 动态更新
     * @param contractProduct
     */
    @Override
    public void update(ContractProduct contractProduct) {

        // 1. 计算修改后的货物总金额
        Double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null){
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        // 1.1 保存新的总金额
        contractProduct.setAmount(amount);

        // 2. 修改购销合同中的总金额（件数是不变的）
        // 2.1 获取数据库旧的总金额（从数据库中获取，新的总金额还未保存进数据库）
        ContractProduct product = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        // 2.2 查找货物的购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        // 2.2 修改购销合同的总金额:合同总金额-旧的货物总金额+新的货物的总金额
        contract.setTotalAmount(contract.getTotalAmount()-product.getAmount()+amount);
        // 2.3 更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);


        contractProductDao.updateByPrimaryKeySelective(contractProduct);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {

        // 1. 删除货物，要修改购销合同的总金额(购销合同的总金额=货物总金额+附件总金额)
        // 1.1 获得删除的货物(以便获得被删的总金额)
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        Double amount = contractProduct.getAmount();
        // 1.2 根据货物id查询附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);
        // 1.3 判断附件如果不为null，遍历集合，累加附件金额，删除附件
        Double extAmount = 0d;
        if (extCproductList != null && extCproductList.size() > 0){
            for (ExtCproduct extCproduct : extCproductList) {
                // 累加金额
                extAmount += extCproduct.getAmount();
                // 删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }

        // 2.修改购销合同的总金额
        // 2.1 查找货物的购销合同对象
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        // 2.2 修改购销合同的总金额(购销合同的总金额=货物总金额+附件总金额)
        contract.setTotalAmount(contract.getTotalAmount() - amount - extAmount);
        // 2.3 修改购销合同的货物数量,附件数量
        contract.setProNum(contract.getProNum() - 1);
        contract.setExtNum(contract.getExtNum() - extCproductList.size());

        // 2.4 更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);

        // 3. 删除货物
        contractProductDao.deleteByPrimaryKey(id);
    }

    /**
     * 出货表统计，根据船期查询
     * @param companyId 企业id
     * @param shipTime 船期
     * @return
     */
    @Override
    public List<ContractProductVo> findByShipTime(String companyId, String shipTime) {
        return contractProductDao.findByShipTime(companyId,shipTime);
    }
}
