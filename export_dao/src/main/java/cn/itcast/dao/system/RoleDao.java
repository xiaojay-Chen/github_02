package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色接口
 */
public interface RoleDao {

    /*
        根据企业id查询全部角色
     */
    List<Role> findAll(String companyid);

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
    void delete(String id);

    /*
        根据角色id查找角色权限中间表
    */
    Long findRoleModuleById(String id);

    // 取消角色权限
    void deleteRoleModule(String roleId);

    // 实现角色添加权限
    void saveRoleModule(String roleId,String moduleId);

    // 根据用户id查询用户所拥有的角色
    List<Role> findUserRoleById(String userId);
}
