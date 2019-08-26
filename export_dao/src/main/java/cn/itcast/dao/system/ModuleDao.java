package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.List;

/**
 * ModuleDao接口
 */
public interface ModuleDao {

    // 根据id查询
    Module findById(String moduleId);

    // 根据id删除
    boolean delete(String moduleId);

    // 添加
    void save(Module module);

    // 修改
    void update(Module module);

    // 查询全部
    List<Module> findAll();

    // 通过id查找要删除模块被其他表引用次数，为空或0可删
    Long findRoleModuleBuId(String id);

    /*
        根据角色id查询拥有的权限
        参数角色id
     */
    List<Module> findModuleByRoleId(String roleId);

    // 根据从属belong字段进行查询用户权限
    List<Module> findByBelong(String belong);

    // 根据用户id查询用户权限
    List<Module> findModuleByUserId(String userId);
}
