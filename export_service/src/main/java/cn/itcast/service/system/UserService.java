package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 用户service接口
 */
public interface UserService {

    // 分页查询
    PageInfo<User> findByPage(String companyId, Integer pageNum, Integer pageSize);

    // 根据企业id查询所有用户
    List<User> findAll(String companyId);

    // 保存
    void save(User user);

    // 更新
    void update(User user);

    // 根据用户id删除
    boolean delete(String id);

    // 根据用户id查询
    User findById(String userId);

    // 实现给用户分配角色
    void updateUserRole(String userId, String[] roleIds);

    // 根据邮箱查找用户
    User findByEmail(String email);
}
