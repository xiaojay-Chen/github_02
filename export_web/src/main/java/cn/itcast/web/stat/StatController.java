package cn.itcast.web.stat;

import cn.itcast.service.stat.StatService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 统计分析模块功能实现
 */
@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {

    @Reference
    private StatService statService;

    /**
     * 进入统计分析的三个页面，公用一个转发方法
     * http://localhost:8080/stat/toCharts.do?chartsType=factory
     * http://localhost:8080/stat/toCharts.do?chartsType=sell
     * http://localhost:8080/stat/toCharts.do?chartsType=online
     * @param chartsType
     * @return
     */
    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){
        return "stat/stat-"+ chartsType;
    }

    /**
     * 需求1：根据生产厂家统计货物销售金额
     */
    @RequestMapping("/getFactorySale")
    @ResponseBody // 自动把方法返回对象转换为json格式
    public List<Map<String, Object>> getFactorySale(){
        List<Map<String, Object>> factorySale = statService.getFactorySale(getLoginCompanyId());

        return factorySale;
    }

    /**
     * 2.产品销售排行，前五
     */
    @RequestMapping("/getProductSale")
    @ResponseBody
    public List<Map<String, Object>> getProductSale(){
        List<Map<String, Object>> productSale = statService.getProductSale(getLoginCompanyId(),5);

        return productSale;
    }

    /**
     * 需求3：按小时统计访问认数
     */
    @RequestMapping("/getOnline")
    @ResponseBody
    public List<Map<String, Object>> getOnline(){
        List<Map<String, Object>> online = statService.getOnline();

        return online;
    }
}
