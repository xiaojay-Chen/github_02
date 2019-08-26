package cn.itcast.web.cargo;


import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.BeanMapUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/cargo/export")
public class PdfController extends BaseController {

    /**
     * PDF导出1.入门案例：展示PDF
     * 请求地址：http://localhost:8080/cargo/export/exportPdf.do?id=
     * 测试案例按时不需要参数
     *
    @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        // 1.加载jasper文件
        InputStream inputStream = session.getServletContext().getResourceAsStream("/jasper/test01.jasper");
        // 2.创建jasperPrint对象
        /*
            参数1：模板文件输入流
            参数2：传递到模板文件中的key-value类型的参数
            参数3：数据列表参数
         *
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap<>(), new JREmptyDataSource());

        // 3.以PDF形式输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

    }*/


    /**
     * PDF导出2.入门案例：map填充 -- 对应jasper模板中paremeter
     *  请求地址：http://localhost:8080/cargo/export/exportPdf.do?id=
     *  测试案例按时不需要参数
     * @throws Exception
     *
    @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        // 1.加载jasper文件
        InputStream inputStream = session.getServletContext().getResourceAsStream("/jasper/test03_paremeter.jasper");


        // 2.构造map函数,map的key与模板文件paremeter中的参数名一致
        Map<String,Object> map = new HashMap<>();
        map.put("userName","一点点");
        map.put("companyName","壹滴滴");
        map.put("email","2408509920@qq.com");
        map.put("deptName","一丢丢");
        // 创建jasperPrint对象，传入参数map
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, new JREmptyDataSource());

        // 3.以PDF形式输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }*/


    /**
     * PDF导出3.入门案例：JDBC数据源填充数据
     * 请求地址：http://localhost:8080/cargo/export/exportPdf.do?id=
     * 测试案例按时不需要参数
     *
    // 注入数据源连接池
    @Autowired
    private DataSource dataSource;

    @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        // 1.加载jasper文件
        InputStream inputStream = session.getServletContext().getResourceAsStream("jasper/test04_jdbc.jasper");

        // 2.创建JasperiPrint对象，传入数据源的connection对象
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap<>(),dataSource.getConnection());

        // 3.以PDF的形式输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

    }*/

    /**
     * 导出PDF4：javabean填充数据
     *
    @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        // 1.加载jasper文件
        InputStream inputStream = session.getServletContext().getResourceAsStream("/jasper/test05_JavaBean.jasper");

        // 2.3 给JRBeanCollectionDataSource()构造集合函数
        // 2.3.1jasper查看的是用户表，参数需要与之对应。
        List<User> list = new ArrayList<>();
        // 2.3.2设置属性值
        for (int i = 0;i < 5;i++){
            User user = new User();
            user.setUserName("杰少");
            user.setEmail("jieshao@123.com");
            user.setCompanyName("黑马程序员");
            user.setDeptName("JavaEE113期");

            // 放入集合中
            list.add(user);
        }

        // 2.1 构造JavaBean对象
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
        // 2.创建JasperPrint对象
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap<>(), jrBeanCollectionDataSource);

        // 3.以PDF形式输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }*/

    /**
     * 导出PDF5：javabean填充数据:分组列表
     */
     @RequestMapping("/exportPdf")
     public void exportPdf() throws Exception {
     // 1.加载jasper文件
     InputStream inputStream = session.getServletContext().getResourceAsStream("/jasper/test06_group.jasper");

     // 2.3 给JRBeanCollectionDataSource()构造集合函数
     // 2.3.1jasper查看的是用户表，参数需要与之对应。

         // 集合数据
         List<User> list = new ArrayList<>();
         for (int j=1; j<=3;j++) {
             for (int i = 1; i <= 5; i++) {
             User user = new User();
             user.setUserName("杰少"+i);
             user.setEmail("jieshao@123.com");
             user.setCompanyName("黑马程序员"+j);
             user.setDeptName("JavaEE113期");
             // 放入集合中
             list.add(user);
         }
     }

     // 2.1 构造JavaBean对象
     JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
     // 2.创建JasperPrint对象
     JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap<>(), jrBeanCollectionDataSource);

     // 3.以PDF形式输出
     JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
     }


    /**
     * 导出PDF5：PDF饼图
     *
     @RequestMapping("/exportPdf")
     public void exportPdf() throws Exception {
         //
         InputStream inputStream = session.getServletContext().getResourceAsStream("/jasper/test07_chart.jasper");

         List list = new ArrayList<>();
         for (int i =0;i<6;i++){
             Map<Object, Object> map = new HashMap<>();
             map.put("title","标题"+i);
             map.put("value",new Random().nextInt(100));
             list.add(map);
         }

         JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);

         JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap<>(), jrBeanCollectionDataSource);

         JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
     }*/

    /**
     * 导出PDF5：导出，下载报表
     */
//    @Reference
//    private ExportService exportService;
//    @Reference
//    private ExportProductService exportProductService;
//
//     @RequestMapping("/exportPdf")
//     public void exportPdf(String id) throws Exception {
//         // 1.加载jasper文件
//         InputStream inputStream = session.getServletContext().getResourceAsStream("/jasper/export.jasper");
//
//         // 2.根据id查询报运单,并使用工具类将对象转成map集合
//         Export export = exportService.findById(id);
//         Map<String, Object> map = BeanMapUtils.beanToMap(export);
//
//         // 2.1 根据报运单id查询报运单下的商品
//         ExportProductExample exportProductExample = new ExportProductExample();
//         exportProductExample.createCriteria().andExportIdEqualTo(id);
//         List<ExportProduct> list = exportProductService.findAll(exportProductExample);
//
//         // 3.构造javaBean对象，作为创建JasperPrint对象的参数
//         JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
//
//         // 4.创建JasperPrint对象
//         JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, jrBeanCollectionDataSource);
//
//         // 5.设置下载PDF
//         // 设置响应类型,并转换成中文编码
//         response.setContentType("application/pdf;charset=utf-8");
//         // 设置下载响应头
//         response.setHeader("content-disposition","attachment;filename=export.pdf");
//         ServletOutputStream outputStream = response.getOutputStream();
//
//         // 6.以PDF的形式输出
//         JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
//
//         outputStream.close();
//     }
}
