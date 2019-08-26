package cn.itcast.web.controller.system;

import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.SysLogService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 日志控制器
 */
@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController{

    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页显示日志列表
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){
        // 1.调用service查询部门列表
        PageInfo<SysLog> pageInfo = sysLogService.findByPage(getLoginCompanyId(), pageNum, pageSize);
        // 2.将部门列表保存到request中
        request.setAttribute("pageInfo",pageInfo);

        // 3.跳转到对象的页面
        return "system/log/log-list";
    }
}
