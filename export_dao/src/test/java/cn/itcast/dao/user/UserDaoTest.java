package cn.itcast.dao.user;


import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    public void testUserDao(){
        String companyId = "1";
        List<User> list = userDao.findAll(companyId);
        System.out.println(list);
    }
}
