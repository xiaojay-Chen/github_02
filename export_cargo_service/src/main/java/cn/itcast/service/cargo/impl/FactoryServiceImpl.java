package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.FactoryService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 生产厂家服务实现类
 */
@Service
public class FactoryServiceImpl implements FactoryService {

    // 注入dao
    @Autowired
    private FactoryDao factoryDao;

    /**
     * 分页列表显示
     * @param factoryExample
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<Factory> findByPage(FactoryExample factoryExample,
                                        int pageNum, int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 查询所有上产厂家
        List<Factory> list = factoryDao.selectByExample(factoryExample);

        // 返回
        return new PageInfo<>(list);
    }


    /**
     * 查询所有
     * @param factoryExample
     * @return
     */
    @Override
    public List<Factory> findAll(FactoryExample factoryExample) {
        return factoryDao.selectByExample(factoryExample);
    }

    /**
     * 根据id查询
     * @param id 厂家id
     * @return
     */
    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    /**
     * 动态添加保存生产厂家
     * @param factory
     */
    @Override
    public void save(Factory factory) {
        factoryDao.insertSelective(factory);
    }

    /**
     * 动态修改更新
     * @param factory
     */
    @Override
    public void update(Factory factory) {
        factoryDao.updateByPrimaryKeySelective(factory);
    }


    /**
     * 删除生产厂家
     * @param id 厂家id
     */
    @Override
    public void delete(String id) {
        factoryDao.deleteByPrimaryKey(id);
    }

    /**
     * 根据工厂名查询工厂id
     * @param factoryName
     * @return
     */
    @Override
    public Factory findIdByFactoryName(String factoryName) {

        List<Factory> list = factoryDao.selectByFactoryName(factoryName);

        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
