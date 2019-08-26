package cn.itcast.service.stat;

import cn.itcast.dao.stat.StatDao;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class StatServiceImpl implements StatService{

    @Autowired
    private StatDao statDao;

    /**
     * 1.根据生产厂家统计货物销售金额
     */
    @Override
    public List<Map<String, Object>> getFactorySale(String companyId) {
        return statDao.getFactorySale(companyId);
    }

    /**
     * 2.产品销售排行，前五
     */
    @Override
    public List<Map<String, Object>> getProductSale(String companyId,int top) {
        return statDao.getProductSale(companyId,top);
    }

    /**
     * 需求3：按小时统计访问认数
     */
    @Override
    public List<Map<String, Object>> getOnline() {
        return statDao.getOnline();
    }
}
