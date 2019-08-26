package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;

import java.util.List;

public interface CompanyDao {

    // 查询所有企业
    List<Company> findAll();

    // 添加保存企业
    void save(Company company);

    // 修改更新企业
    void update(Company company);

    // 根据id查询
    Company findById(String id);

    // 删除企业
    void delete(String id);
}
