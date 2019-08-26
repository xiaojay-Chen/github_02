package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 购销合同模块服务接口实现
 */
@Service
public class ContractServiceImpl implements ContractService{

    /* 注入dao */
    @Autowired
    private ContractDao contractDao;

    /**
     * 分页查询
     * @param contractExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {

        // 开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao方法
        List<Contract> list = contractDao.selectByExample(contractExample);

        return new PageInfo<>(list);
    }

    /**
     * 查询所有
     * @param contractExample 分页查询的参数
     * @return
     */
    @Override
    public List<Contract> findAll(ContractExample contractExample) {

        List<Contract> list = contractDao.selectByExample(contractExample);
        return list;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Contract findById(String id) {

        Contract contract = contractDao.selectByPrimaryKey(id);
        return contract;
    }

    /**
     * 动态添加保存
     * @param contract
     */
    @Override
    public void save(Contract contract) {

        // 设置UUID作为主键
        contract.setId(UUID.randomUUID().toString());

        // 记录购销合同创建时间
        contract.setCreateTime(new Date());
        // 初始化修改时间
        contract.setUpdateTime(new Date());
        // 默认状态为草稿
        contract.setState(0);

        // 初始化总金额，货物数，附件数
        contract.setTotalAmount(0d);
        contract.setProNum(0);
        contract.setExtNum(0);

        contractDao.insertSelective(contract);

    }

    /**
     * 动态修改更新
     * @param contract
     */
    @Override
    public void update(Contract contract) {

        // 记录修改的时间
        contract.setUpdateTime(new Date());
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(String id) {

        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Contract> selectByDeptId(String deptId, int pageNum, int pageSize) {
        // 1.开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 2.调用dao
        List<Contract> list = contractDao.selectByDeptId(deptId);
        return new PageInfo<>(list);
    }
}
