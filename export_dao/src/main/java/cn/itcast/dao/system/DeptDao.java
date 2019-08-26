package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

/**
 * 部门dao接口
 */
public interface DeptDao {

    /**
     * 查询所有部门
     * @param companyId 根据企业id查询
     * @return
     */
    List<Dept> findAll(String companyId);

    // 添加部门
    void save(Dept dept);

    // 修改部门
    void update(Dept dept);

    // 根据id查询
    Dept findById(String id);

    /**
     * 根据父部门id作为条件查询
     * @param parentId 父部门id
     * @return
     */
    List<Dept> findByParentId(String parentId);

    // 删除部门
    void delete(String id);
}
