package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Module Service实现
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    ModuleDao moduleDao;
    @Autowired
    private UserDao userDao;

    // 根据id查询
    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    // 添加模块
    @Override
    public void save(Module module) {
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    // 修改
    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    // 根据id删除
    @Override
    public boolean delete(String id) {
        // 通过id查找要删除模块被其他表引用次数，为空或0可删
        Long count = moduleDao.findRoleModuleBuId(id);

        // 判断
        if (count == null || count == 0){
            moduleDao.delete(id);
            return true;
        }
        return false;
    }

    // 分页查询全部
    @Override
    public PageInfo<Module> findByPage(int pageNum, int pageSize) {
        // 开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<Module> list = moduleDao.findAll();

        return new PageInfo<>(list);
    }

    // 查询全部
    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    /*
        根据角色id查询拥有的权限
        参数角色id
     */
    @Override
    public List<Module> findModuleByRoleId(String roleId) {
        return moduleDao.findModuleByRoleId(roleId);
    }

    /**
     * 根据登录的用户id查询用户所具有的所有权限（模块，菜单）
     *   1.根据用户id查询用户
     *   2.根据用户degree级别判断
     *   3.如果degree==0 （内部的sass管理）
     *      根据模块中的belong字段进行查询，belong = "0";
     *   4.如果degree==1 （租用企业的管理员）
     *      根据模块中的belong字段进行查询，belong = "1";
     *   5.其他的用户类型
     *      借助RBAC的数据库模型，多表联合查询出结果
     * @param userId
     * @return
     */
    @Override
    public List<Module> findModuleByUserId(String userId) {

        // 1.根据用户id查询用户
        User user = userDao.findById(userId);
        // 2.根据用户degree级别判断
        if (user.getDegree() == 0){
            // 3.如果degree==0 （内部的sass管理）
            return moduleDao.findByBelong("0");
        }else if (user.getDegree() == 1){
            // 4.如果degree==1 （租用企业的管理员）
            // 根据模块中的belong字段进行查询，belong = "1";
            return moduleDao.findByBelong("1");
        }else {
            // 5.其他的用户类型
            // 借助RBAC的数据库模型，多表联合查询出结果
            return moduleDao.findModuleByUserId(userId);
        }
    }
}
