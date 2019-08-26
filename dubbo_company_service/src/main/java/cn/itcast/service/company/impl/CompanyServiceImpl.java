package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * service服务接口的实现
 */
@Service(timeout = 100000)
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    /**
     * 查询全部
     * @return
     */
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    /**
     * 添加保存
     * @param company
     */
    @Override
    public void save(Company company) {

        // 设置企业id
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    /**
     * 修改更新
     * @param company
     */
    @Override
    public void upDate(Company company) {

        companyDao.update(company);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(String id) {

        companyDao.delete(id);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Company> findByPage(int pageNum, int pageSize) {

        // 开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询全部企业
        List<Company> companyList = companyDao.findAll();
        // 创建PageInfo封装分页结果，传入查询集合，会自动计算分页参数
        PageInfo<Company> pageInfo = new PageInfo<>(companyList);

        return pageInfo;
    }
}
