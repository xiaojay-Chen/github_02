package cn.itcast.service.system;

import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志service
 */
public interface SysLogService {

    // 查询全部
    PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize);

    // 添加
    void save(SysLog log);
}
