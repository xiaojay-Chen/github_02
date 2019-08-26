package cn.itcast.web.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;

    // 获取登录用户所属的企业id
    protected String getLoginCompanyId(){
        return getLoginInfo().getCompanyId();
    }

    // 获取登录用户所属的企业名称
    protected String getLoginCompanyName(){
        return "传智播客教育股份有限公司";
    }

    // 从session中获取登陆用户对象
    protected User getLoginInfo(){
        User loginInfo = (User) session.getAttribute("loginInfo");
        return loginInfo;
    }
}
