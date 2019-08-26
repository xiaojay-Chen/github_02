package cn.itcast.system;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Module;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 分页查询测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class DeptServiceTest {

    @Autowired
    private DeptService deptService;
    @Autowired
    private ModuleDao moduleDao;

    @Test
    public void findByPage(){
        PageInfo<Dept> pageInfo = deptService.findByPage("1", 1, 2);
        System.out.println(pageInfo);
    }

    @Test
    public void findModuleByRoleId(){
        String id = "4028a1c34ec2e5c8014ec2ec38cc0002";
        List<Module> moduleByRoleId = moduleDao.findModuleByRoleId(id);
        System.out.println(moduleByRoleId);
    }
}
