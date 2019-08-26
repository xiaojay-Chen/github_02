package cn.itcast.test;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class TestDemo {

    @Autowired
    private ModuleService moduleService;

    @Test
    public void findModuleByRoleId(){

        String id = "4028a1cd4ee2d9d6014ee2df4c6a0000";
        List<Module> moduleByRoleId = moduleService.findModuleByRoleId(id);
        System.out.println(moduleByRoleId);
    }

    @Test
    public void md5(){
        String userName = "LW@export.com";
        String password = "1";
        Md5Hash hash = new Md5Hash(password,userName);
        System.out.println(hash.toString());
    }

}
