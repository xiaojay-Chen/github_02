package cn.itcast.contractProduct;

import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class ContractProductTest {

    @Autowired
    private ContractProductDao contractProductDao;

    @Test
    public void Test(){
        ContractProductExample contractProductExample = new ContractProductExample();
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
        System.out.println(contractProducts);
    }
}
