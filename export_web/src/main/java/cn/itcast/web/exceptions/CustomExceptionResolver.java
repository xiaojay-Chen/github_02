package cn.itcast.web.exceptions;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一类处理
 */
public class CustomExceptionResolver implements HandlerExceptionResolver {

    /*
    跳转到美化了的错误信息
    携带错误信息
     */
    @Override
    public ModelAndView resolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handle, Exception ex) {
        // 打印异常信息
        ex.printStackTrace();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("errorMsg","对不起，您的操作有误");
        mv.addObject("ex",ex);

        return mv;

    }
}
