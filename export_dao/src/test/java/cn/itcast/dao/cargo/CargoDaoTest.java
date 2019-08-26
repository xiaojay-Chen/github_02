package cn.itcast.dao.cargo;


import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class CargoDaoTest {

    // 注入DAO
    @Autowired
    private FactoryDao factoryDao;

    /**
     * 根据主键来查
     * sql语句：select
     *             id, ctype, full_name, factory_name, contacts, phone,
     *             mobile, fax, address, inspector, remark, order_no, state, create_by,
     *             create_dept, create_time, update_by, update_time
     *          from
     *             co_factory
     *          where id = ?
     */
    @Test
    public void findById(){
        System.out.println(factoryDao.selectByPrimaryKey("18"));
    }


    /**
     * 多条件查询
     * sql语句：select
     *             id, ctype, full_name, factory_name, contacts, phone,
     *             mobile, fax, address, inspector, remark, order_no,
     *             state, create_by, create_dept, create_time, update_by,
     *             update_time
     *          from co_factory
     *          WHERE ( factory_name = ? )
     */
    @Test
    public void findByFactoryName(){

        // g构造条件对象
        FactoryExample example = new FactoryExample();
        // 查询条件：Factory_name
        FactoryExample.Criteria criteria = example.createCriteria();
        criteria.andFactoryNameEqualTo("升华");
        List<Factory> selectByExample = factoryDao.selectByExample(example);
        System.out.println(selectByExample);
    }

    /**
     * 普通更新
     * sql语句：update
     *             co_factory
     *          set
     *              ctype = ?, full_name = ?, factory_name = ?, contacts = ?, phone = ?,
     *              mobile = ?, fax = ?, address = ?, inspector = ?, remark = ?, order_no = ?,
     *              state = ?, create_by = ?, create_dept = ?, create_time = ?, update_by = ?, update_time = ?
     *           where id = ?
     *
     *  与下面进行对比，而普通更新是全部一起修改，没值就为null。动态更新是根据哪些属性有值，就修改哪些属性。
     *  一般情况下推荐使用动态修改
     */
    @Test
    public void update(){

        Factory factory = new Factory();
        factory.setId("4028817a3abe8f15013ac019a61f0031");
        factory.setCtype("厂家");
        factory.setFullName("慕玲集团");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        factoryDao.updateByPrimaryKeySelective(factory);
    }


    /**
     * 动态更新:根据对象属性值是否为null，不为null才进行更新
     * sql语句：update co_factory SET update_time = ? where id = ?
     *
     * 与上面进行对比，动态更新是根据哪些属性有值，就修改哪些属性。而上面是全部一起修改，没值就为null。
     * 一般情况下推荐使用动态修改
     */
    @Test
    public void dynamicUpdate(){

        Factory factory = new Factory();
        factory.setId("4028817a3abe8f15013ac019a61f0031");
        factory.setUpdateTime(new Date());
        factoryDao.updateByPrimaryKeySelective(factory);
    }
}
