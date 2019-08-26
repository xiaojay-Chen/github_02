package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;

import java.util.List;

public interface FactoryDao {

    //条件查询
    List<Factory> selectByExample(FactoryExample example);

    //保存
    int insertSelective(Factory record);

	//删除
    int deleteByPrimaryKey(String id);

    //更新
    int updateByPrimaryKeySelective(Factory record);

    //id查询
    Factory selectByPrimaryKey(String id);

    // 根据工厂名查询工厂id
    List<Factory> selectByFactoryName(String factoryName);
}