package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * 购销合同货物附件模块实现类
 */
@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    // 注入附件dao
    @Autowired
    private ExtCproductDao extCproductDao;
    // 注入购销合同dao
    @Autowired
    private ContractDao contractDao;

    /**
     * 附件分页列表
     * @param extCproductExample
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        // 1.开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 2.调用dao
        List<ExtCproduct> list = extCproductDao.selectByExample(extCproductExample);
        // 返回
        return new PageInfo<>(list);
    }

    /**
     * 查询所有
     * @param extCproductExample
     * @return
     */
    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ExtCproduct findById(String id) {

        return extCproductDao.selectByPrimaryKey(id);
    }

    /**
     * 添加保存
     * @param extCproduct
     */
    @Override
    public void save(ExtCproduct extCproduct) {
        // 1.设置新增附件的id
        extCproduct.setId(UUID.randomUUID().toString());

        /* 2.计算附件金额 */
        Double amount = 0d;
        if (extCproduct.getCnumber() != null && extCproduct.getPrice() != null){
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        // 保存附件总金额
        extCproduct.setAmount(amount);

        /* 3.修改购销合同的总金额和附件数 */
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()+amount);
        contract.setExtNum(contract.getExtNum()+1);
        /* 3.1 更新购销合同 */
        contractDao.updateByPrimaryKeySelective(contract);

        // 添加附件
        extCproductDao.insertSelective(extCproduct);
    }

    /**
     * 更新修改
     * @param extCproduct
     */
    @Override
    public void update(ExtCproduct extCproduct) {
        /* 1.计算修改后附件的总金额 */
        Double newAmount = 0d;
        if (extCproduct.getCnumber() != null && extCproduct.getPrice() != null){
            newAmount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        extCproduct.setAmount(newAmount);

        /* 2.修改购销合同的总金额 */
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        /* 2.1 计算修改前的附件总金额,需要查询数据库 */
        ExtCproduct extC = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double oldAmount = extC.getAmount();
        // 修改购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() - oldAmount + newAmount);
        /* 2.2 更新购销合同 */
        contractDao.updateByPrimaryKeySelective(contract);

        // 修改附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {
        // 计算删除的附件的金额
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        Double amount = extCproduct.getAmount();

        // 修改购销合同总金额和附件数
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() - amount);
        contract.setExtNum(contract.getExtNum() - 1);
        // 更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);

        // 删除附件
        extCproductDao.deleteByPrimaryKey(id);
    }
}
