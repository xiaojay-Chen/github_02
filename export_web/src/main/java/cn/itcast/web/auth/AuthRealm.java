package cn.itcast.web.auth;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 自定义reaml
 */
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    /* 登录认证 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1.获取登录的用户名（邮箱）
        String email = (String) token.getPrincipal();

        // 2.根据email查询用户
        User user = userService.findByEmail(email);

        // 3.判断用户是否存在
        if (user == null){
            // 一旦认证方法返回null，就是UnknowAccountException异常
            return null;
        }

        // 4.获取数据库中正确的密码
        String password = user.getPassword();

        // 5.返回
        /*
            参数一：身份对象，通过token.getPrincipal()；获取的就是这里的参数1
            参数二：数据库中正确的密码
            参数三：realm名称，可以随意。唯一.getName()获取默认名称
         */
        SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(user, password, this.getName());

        return sai;
    }


    /* 授权认证 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 1.获取认证后的用户身份对象，（realm认证方法返回对象的构造函数的第一个参数）
        User user = (User) principals.getPrimaryPrincipal();

        // 2.根据用户id查询用户权限
        List<Module> modules = moduleService.findModuleByUserId(user.getId());

        // 3.获得返回对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 4.遍历用户的权限集合
        if (modules != null && modules.size() > 0){
            for (Module module : modules) {
                // 返回用户的权限
                info.addStringPermission(module.getName());
            }
        }
        return info;
    }
}
