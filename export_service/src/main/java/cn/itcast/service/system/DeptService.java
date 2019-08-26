package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 部门业务层接口
 */
public interface DeptService {

    /**
     * 分页查询所有部门
     * @param companyId 根据部门所属的企业分页查询
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    public PageInfo<Dept> findByPage(String companyId,int pageNum,int pageSize);

    // 根据企业id查询所有部门
    List<Dept> findAll(String companyId);

    // 添加部门
    void save(Dept dept);

    // 修改部门
    void update(Dept dept);

    // 根据id查询部门
    Dept findById(String id);

    /**
     * 根据id删除部门，删除成功返回true
     * @param id 部门id
     * @return
     */
    boolean delete(String id);
}
