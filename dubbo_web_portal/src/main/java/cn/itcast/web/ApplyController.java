package cn.itcast.web;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制器
 */
@Controller
    public class ApplyController {

        /**
         * @Reference（retries=0）
         *  retries:配置重试次数
         *  参考dubbo使用注意事项
         */
        // @Reference(retries = 2)
        private CompanyService companyService;

    /**
     * 企业入驻申请，保存
     * @param company
     * @return
     */
    @RequestMapping("/apply")
    @ResponseBody //返回json格式
    public String apply(Company company){

        try {
            // 直接保存企业数据，应前端要求正确返回“1”
            companyService.save(company);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}
