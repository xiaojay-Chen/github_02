package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import cn.itcast.vo.ContractProductVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 企业service接口
 */
public interface CompanyService {

    // 查询所有企业
    List<Company> findAll();

    // 保存添加企业
    void save(Company company);

    // 修改更新企业
    void upDate(Company company);

    // 根据id查询
    Company findById(String id);

    // 删除
    void delete(String id);

    // 分页查询
    PageInfo<Company> findByPage(int pageNum,int pageSize);


}
