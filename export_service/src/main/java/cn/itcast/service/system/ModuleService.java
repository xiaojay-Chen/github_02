package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Module service接口
 */
public interface ModuleService {

    // 根据id查询
    Module findById(String id);

    // 添加
    void save(Module module);

    // 修改
    void update(Module module);

    // 根据id删除
    boolean delete(String id);

    // 分页查询全部
    PageInfo<Module> findByPage(int pageNum,int pageSize);

    // 查询全部
    List<Module> findAll();

    /*
        根据角色id查询权限
        参数角色id
     */
    List<Module> findModuleByRoleId(String roleId);

    /*
        根据用户id查询用户的权限
     */
    List<Module> findModuleByUserId(String userId);
}
