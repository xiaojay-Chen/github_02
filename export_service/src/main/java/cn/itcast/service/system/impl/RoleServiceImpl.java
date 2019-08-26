package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.SplittableRandom;
import java.util.UUID;

/**
 * 业务层实现
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    /*
        分页查询
     */
    @Override
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<Role> list = roleDao.findAll(companyId);

        return new PageInfo<>(list);
    }

    /*
        根据id查询角色
     */
    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    /*
        添加
     */
    @Override
    public void save(Role role) {
        // 赋予id属性
        role.setId(UUID.randomUUID().toString());
        roleDao.save(role);
    }

    /*
        更新
     */
    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    /*
        删除
     */
    @Override
    public boolean delete(String id) {
        Long count = roleDao.findRoleModuleById(id);

        if (count == null || count == 0){
            roleDao.delete(id);
            return true;
        }
        return false;
    }

    /*
        查询所有角色
     */
    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    // 实现角色分配权限
    @Override
    public void updateRoleModule(String roleId, String moduleIds) {
        // 1.实现删除用户角色中间表数据（取消权限）
        roleDao.deleteRoleModule(roleId);
        // 2.判断
        if (moduleIds != null && !"".equals(moduleIds)) {
            // 3.分割字符串
            String[] array = moduleIds.split(",");

            if (array != null && array.length > 0) {
                // 4.遍历所有模块，实现角色添加模块
                for (String moduleId : array) {
                    roleDao.saveRoleModule(roleId,moduleId);
                }
            }

        }
    }

    // 根据用户id查询用户已经拥有的角色
    @Override
    public List<Role> findUserRoleById(String userId) {
        return roleDao.findUserRoleById(userId);
    }
}
