package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 角色业务层接口
 */
public interface RoleService {

    /*
        根据企业id分页查询全部角色
     */
    PageInfo<Role> findByPage(String company,int pageNum,int pageSize);

    /*
        根据id查询角色
     */
    Role findById(String id);

    /*
        添加
     */
    void save(Role role);

    /*
        更新
     */
    void update(Role role);

    /*
        根据角色id删除
     */
    boolean delete(String id);

    /*
        查询所有角色
     */
    List<Role> findAll(String companyId);

    // 实现角色分配权限
    void updateRoleModule(String roleId, String moduleIds);

    // 通过用户id查询用户已经拥有的角色
    List<Role> findUserRoleById(String userId);
}
