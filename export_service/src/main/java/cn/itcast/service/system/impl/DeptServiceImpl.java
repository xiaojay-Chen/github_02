package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 部门业务实现类
 */
@Service
public class DeptServiceImpl implements DeptService {

    // 注入dao
    @Autowired
    private DeptDao deptDao;

    // 分页查询
    @Override
    public PageInfo<Dept> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启查询分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao方法，传入企业id查询
        List<Dept> list = deptDao.findAll(companyId);
        // // 创建PageInfo对象封装分页结果，传入查询集合，会自动计算分页参数
        PageInfo<Dept> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 根据企业id查询所有部门
     * @param companyId 企业id
     * @return
     */
    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    /**
     * 添加保存部门
     * @param dept
     */
    @Override
    public void save(Dept dept) {
        dept.setId(UUID.randomUUID().toString());
        deptDao.save(dept);
    }

    /**
     * 修改更新部门
     * @param dept
     */
    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    /**
     * 根据部门id查询部门
     * @param id  部门id
     * @return
     */
    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    /**
     * 根据部门id删除部门
     * @param id 部门id
     * @return
     */
    @Override
    public boolean delete(String id) {

        // 1.根据要删除的部门id查询，看是否有子部门
        List<Dept> list = deptDao.findByParentId(id);
        // 2.判断
        if (list == null || list.size()==0){
            // 当前删除的部门没有子部门，可以删除
            deptDao.delete(id);
            return true;
        }else {
            return false;
        }
    }
}
