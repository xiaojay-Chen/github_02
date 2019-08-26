package cn.itcast.web.controller;


        import cn.itcast.domain.system.Module;
        import cn.itcast.domain.system.User;
        import cn.itcast.service.system.ModuleService;
        import cn.itcast.service.system.UserService;
        import org.apache.shiro.SecurityUtils;
        import org.apache.shiro.authc.AuthenticationException;
        import org.apache.shiro.authc.UsernamePasswordToken;
        import org.apache.shiro.subject.Subject;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.util.StringUtils;
        import org.springframework.web.bind.annotation.RequestMapping;

        import java.util.List;

@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    /*
    @RequestMapping("/login")
    public String login(String email,String password){

        // 1.判断参数是否为空
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            // 1.1为空，跳转到登陆页面
            return "forward:/login.jsp";
        }

        // 不为空：
        // 2.根据email查找用户
        User user = userService.findByEmail(email);

        // 3.判断输入的email是否存在，
        if (email != null){

            // 3.1判断输入的密码是否正确
            if (user.getPassword().equals(password)){
                // 3.2正确，登陆成功
                // 保存用户到session
                session.setAttribute("loginInfo",user);

                // 3.3. 登录成功，根据用户id获取当前登录用户拥有的权限，保存到session中
                List<Module> modules = moduleService.findModuleByUserId(user.getId());
                session.setAttribute("modules",modules);

                // 跳转到main.jsp
                return "home/main";

            }else {
                // 登录失败，密码有误
                request.setAttribute("error","邮箱或密码错误！");
                return "forward:/login.jsp";
            }
        }else {
            // 登录失败，密码有误
            request.setAttribute("error","邮箱或密码错误！");
            return "forward:/login.jsp";
        }
    }
    */

    /**
     * 通过shrio进行登录认证
     * @param email
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public String login(String email,String password){

        /* 1.判断参数是否为空 */
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            // 1.1为空，跳转到登陆页面
            return "forward:/login.jsp";
        }

        try {
            /* 2.登录认证：*/
            // 2.1.获取/创建Subject(头一次使用创建)：SecurityUtils.getSubject().var
            Subject subject = SecurityUtils.getSubject();
            // 2.2.创建token：封装输入的账号密码：new UsernamePasswordToken(email,password);.var
            UsernamePasswordToken token = new UsernamePasswordToken(email,password);
            // 2.3.登录认证,借助subject完成用户登录：subject.login(token)
            // token会去AuthReaml进行数据库访问，进行认证和授权
            subject.login(token);

            /* 3.获取用户认证的身份对象，（Realm的认证方法构造器的第一个参数） */
            User user = (User) subject.getPrincipal();

            /* 4.登录成功，保存：登录用户，用户权限 */
            // 保存用户到session
            session.setAttribute("loginInfo",user);

            // 3.3. 登录成功，根据用户id获取当前登录用户拥有的权限，保存到session中
            List<Module> modules = moduleService.findModuleByUserId(user.getId());
            session.setAttribute("modules",modules);

            // 跳转到main.jsp
            return "home/main";
        } catch (Exception e) {
            e.printStackTrace();
            // 登录认证失败
            request.setAttribute("error","登录认证失败！");
            return "forward:/login.jsp";
        }
    }


    @RequestMapping("/home")
    public String home(){
        return "home/home";
    }

    /**
     * 注销用户登录（退出）
     * @return
     */
    @RequestMapping("/logout")
    public String logout(){
        /*
            完成shrio自定义凭证匹配器后，
            可以用shrio提供的推出方法（清楚shrio的认证信息）
            用不用无所谓。退出功能不影响
         */
        SecurityUtils.getSubject().logout();
        // 删除session中的用户
        session.removeAttribute("loginInfo");
        // 销毁session
        session.invalidate();
        return "forward:/login.jsp";
    }
}
