package cn.itcast.web.aspect;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * 日志切面类
 */
@Component  // 创建对象加入容器
@Aspect // 指定当前类为切面类/通知类
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpServletRequest request;


    /**
     * 需求：在执行控制器的方法之后自动记录日志
     * （使用环绕通知对controller方法进行增强：自动记录日志）
     * @Around（execution（当前方法路径方法名））
     * @param pjp
     * @return
     */
    @Around(value = "execution(* cn.itcast.web.controller.*.*.*(..))")
    public Object insertLog(ProceedingJoinPoint pjp){

        // 记录当前日志信息
        SysLog sysLog = new SysLog();
        sysLog.setId(UUID.randomUUID().toString());
        sysLog.setTime(new Date());
        // 设置来访者
        sysLog.setIp(request.getRemoteAddr());
        // 设置当前执行的方法信息（方法名）
        sysLog.setMethod(pjp.getSignature().getName());
        sysLog.setAction(pjp.getTarget().getClass().getName());

        // 从session获取登录用户信息
        User user = (User) request.getSession().getAttribute("loginInfo");
        // 判断
        if (user != null){
            sysLog.setUserName(user.getUserName());
            sysLog.setCompanyId(user.getCompanyId());
            sysLog.setCompanyName(user.getCompanyName());
        }

        try {
            // 1.执行控制器方法
            Object proceed = pjp.proceed();
            // 2.记录日志
            sysLogService.save(sysLog);
            // 返回控制器方法
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
