package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.User;

import java.util.List;

/**
 * 用户DAO接口
 */
public interface UserDao {

    /*
        根据企业id查询所有用户
     */
    List<User> findAll(String companyId);

    /*
        根据用户id查询用户
     */
    User findById(String userId);

    /*
        添加保存用户
     */
    void save(User user);

    /*
        修改更新用户
     */
    void update(User user);

    /*
        删除用户
     */
    void delete(String userId);

    /*
        先根据要删除的用户id查询用户角色中间表
     */
    Long findUserRoleById(String id);

    // 解除用户角色关系
    void deleteUserRoleByUserId(String userId);

    // 添加角色用户
    void saveUserRole(String userId, String roleId);

    // 根据邮箱查找用户
    /*
        dao接口返回一个对象
        为了提供系统更好的容错能力，更符合接口设计原则（条件查询时候，
        为了考虑通用性，所以最好返回集合）
     */
    List<User> findByEmail(String email);
}
