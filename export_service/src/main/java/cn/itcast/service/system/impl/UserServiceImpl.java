package cn.itcast.service.system.impl;

import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 用户业务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    // 分页查询
    @Override
    public PageInfo<User> findByPage(String companyId, Integer pageNum, Integer pageSize) {
        // 开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao方法查询全部列表
        List<User> list = userDao.findAll(companyId);

        return new PageInfo<User>(list);
    }


    // 根据企业id查询所有用户
    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    //添加用户
    @Override
    public void save(User user) {
        user.setId(UUID.randomUUID().toString());
        // 完成自定义凭证匹配器（指定的加密算法），
        // 需要对用户输入的密码加密、加盐，这样就可以使用添加的用户登陆了
        if (user.getPassword() != null){
            String encodepwd = new Md5Hash(user.getPassword(), user.getEmail()).toString();
            user.setPassword(encodepwd);
        }
        userDao.save(user);
    }

    // 更新用户
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    // 删除用户
    @Override
    public boolean delete(String id) {
        // 先根据要删除的用户id查询用户角色中间表
        Long count = userDao.findUserRoleById(id);

        // 判断
        if (count == null || count == 0){
            userDao.delete(id);
            return true;
        }
        return false;
    }

    // 根据id查询
    @Override
    public User findById(String userId) {
        return userDao.findById(userId);
    }

    /*
        实现给用户分配角色
        1.取消角色
        2.添加角色
     */
    @Override
    public void updateUserRole(String userId, String[] roleIds) {
        // 1.解除用户角色关系
        userDao.deleteUserRoleByUserId(userId);

        // 2.添加用户角色
        if (roleIds != null && roleIds.length > 0){
            for (String roleId : roleIds) {
                userDao.saveUserRole(userId,roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        List<User> list = userDao.findByEmail(email);
        return list!=null && list.size()>0 ? list.get(0) : null;
    }
}
