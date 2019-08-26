package cn.itcast.web.cargo;

import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.vo.ContractProductVo;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.DownloadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 出货表统计控制器
 */
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController{

    @Reference
    private ContractProductService contractProductService;


    /**
     * 导出出货表（1）：进入出货表页面
     * @return
     */
    @RequestMapping("/print")
    public String print(){

        return "/cargo/print/contract-print";
    }

    /**
     * 导出出货表（1）：导出，下载
     * 请求地址：http://localhost:8080/cargo/contract/printExcel.do
     * @param inputDate 年-月
     * @return
     */
//    @RequestMapping("/printExcel")
//    public void printExcel(String inputDate) throws Exception {
//        // 第一步：导出第一行
//        // 1.创建工作簿
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        // 2.创建工作表
//        XSSFSheet sheet = workbook.createSheet("导出出货表");
//        // 设置列宽
//        sheet.setColumnWidth(0, 256 * 5);
//        sheet.setColumnWidth(1, 256 * 15);
//        sheet.setColumnWidth(2, 256 * 26);
//        sheet.setColumnWidth(3, 256 * 15);
//        sheet.setColumnWidth(4, 256 * 29);
//        sheet.setColumnWidth(5, 256 * 15);
//        sheet.setColumnWidth(6, 256 * 15);
//        sheet.setColumnWidth(7, 256 * 15);
//        sheet.setColumnWidth(8, 256 * 15);
//        // 合并单元格，开始0，结束0.开始列1，结束列8
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
//        // 3.创建第一行
//        XSSFRow row = sheet.createRow(0);
//        // 设置行高
//        row.setHeightInPoints(36);
//        // 4.创建第一行第二列
//        XSSFCell cell = row.createCell(1);
//        // 5.设置单元格内容
//        // 2019-06 --》2019年6月份出货表，2019-11
//        String result = inputDate.replaceAll("-0", "-").replaceAll("-", "年") + "月份出货表";
//        cell.setCellValue(result);
//        // 设置单元格样式
//        cell.setCellStyle(this.bigTitle(workbook));
//
//        // 第二部：导出第二行
//        String[] titles = {"客户", "订单号", "货号", "数量", "工厂", "工厂交期", "船期", "贸易条款"};
//        row = sheet.createRow(1);
//        row.setHeightInPoints(26);
//        // 创建第二行的每一列
//        for (int i = 0;i < titles.length;i++){
//            cell = row.createCell(i + 1);
//            // 设置列内容
//            cell.setCellValue(titles[1]);
//            // 设置列样式
//            cell.setCellStyle(this.title(workbook));
//        }
//        // 第三步：导出数据行,从第三行开始
//        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
//        if (list != null && list.size() > 0){
//            int num = 2;
//            for (ContractProductVo contractProductVo : list) {
//                row = sheet.createRow(num++);
//                cell = row.createCell(1);
//
//                cell.setCellValue(contractProductVo.getCustomName());
//                cell.setCellStyle(this.text(workbook));
//
//                cell = row.createCell(2);
//                cell.setCellValue(contractProductVo.getContractNo());
//                cell.setCellStyle(this.text(workbook));
//
//                cell = row.createCell(3);
//                cell.setCellValue(contractProductVo.getProductNo());
//                cell.setCellStyle(this.text(workbook));
//
//
//                cell = row.createCell(4);
//                if (contractProductVo.getCnumber() == null) {
//                    cell.setCellValue("");
//                } else {
//                    cell.setCellValue(contractProductVo.getCnumber());
//                }
//                cell.setCellStyle(this.text(workbook));
//
//                cell = row.createCell(5);
//                cell.setCellValue(contractProductVo.getFactoryName());
//                cell.setCellStyle(this.text(workbook));
//
//                cell = row.createCell(6);
//                cell.setCellValue(contractProductVo.getDeliveryPeriod());
//                cell.setCellStyle(this.text(workbook));
//
//                cell = row.createCell(7);
//                cell.setCellValue(contractProductVo.getShipTime());
//                cell.setCellStyle(this.text(workbook));
//
//                cell = row.createCell(8);
//                cell.setCellValue(contractProductVo.getTradeTerms());
//                cell.setCellStyle(this.text(workbook));
//            }
//        }
//    /**
//     * 导出出货表（2）导出，下载， 模板打印
//     */
//    @RequestMapping("/printExcel")
//    public void printExcel(String inputDate) throws Exception {
//        // 获取excel模板文件流
//        InputStream inputStream = session.getServletContext().getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx");
//        // 获取工作簿
//        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//        // 获取工作表
//        XSSFSheet sheetAt = workbook.getSheetAt(0);
//        // 获取第一行
//        XSSFRow row = sheetAt.getRow(0);
//        XSSFCell cell = row.getCell(1);
//        String result = inputDate.replaceAll("-0","-").replaceAll("-","年")+"月份出货表";
//        cell.setCellValue(result);
//
//        // 获取第三行,主要是为了获取第三行的样式
//        row = sheetAt.getRow(2);
//        CellStyle[] styles = new CellStyle[8];
//        for (int i = 0;i<styles.length;i++){
//            cell = row.getCell(i + 1);
//            styles[i] = cell.getCellStyle();
//        }
//
//        // 导出数据行，从第三行开始
//        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(),inputDate);
//        if (list != null && list.size() >0){
//            int num = 2;
//            for (ContractProductVo contractProductVo : list) {
//                row = sheetAt.createRow(num++);
//
//                cell = row.createCell(1);
//                cell.setCellValue(contractProductVo.getCustomName());
//                cell.setCellStyle(styles[0]);
//
//                cell = row.createCell(2);
//                cell.setCellValue(contractProductVo.getContractNo());
//                cell.setCellStyle(styles[1]);
//
//                cell = row.createCell(3);
//                cell.setCellValue(contractProductVo.getProductNo());
//                cell.setCellStyle(styles[2]);
//
//
//                cell = row.createCell(4);
//                if (contractProductVo.getCnumber() == null) {
//                    cell.setCellValue("");
//                } else {
//                    cell.setCellValue(contractProductVo.getCnumber());
//                }
//                cell.setCellStyle(styles[3]);
//
//                cell = row.createCell(5);
//                cell.setCellValue(contractProductVo.getFactoryName());
//                cell.setCellStyle(styles[4]);
//
//                cell = row.createCell(6);
//                cell.setCellValue(contractProductVo.getDeliveryPeriod());
//                cell.setCellStyle(styles[5]);
//
//                cell = row.createCell(7);
//                cell.setCellValue(contractProductVo.getShipTime());
//                cell.setCellStyle(styles[6]);
//
//                cell = row.createCell(8);
//                cell.setCellValue(contractProductVo.getTradeTerms());
//                cell.setCellStyle(styles[7]);
//            }
//        }


    /**
     * 导出出货表（2）:SXSSFWorkbook处理百万数据报表打印
     * 导出，下载， 模板打印
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        // 第一步：导出第一行
        // 1.创建工作簿
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 2.创建工作表
        Sheet sheet = workbook.createSheet("导出出货表");
        // 设置列宽
        sheet.setColumnWidth(0, 256 * 5);
        sheet.setColumnWidth(1, 256 * 15);
        sheet.setColumnWidth(2, 256 * 26);
        sheet.setColumnWidth(3, 256 * 15);
        sheet.setColumnWidth(4, 256 * 29);
        sheet.setColumnWidth(5, 256 * 15);
        sheet.setColumnWidth(6, 256 * 15);
        sheet.setColumnWidth(7, 256 * 15);
        sheet.setColumnWidth(8, 256 * 15);
        // 合并单元格，开始0，结束0.开始列1，结束列8
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
        // 3.创建第一行
        Row row = sheet.createRow(0);
        // 设置行高
        row.setHeightInPoints(36);
        // 4.创建第一行第二列
        Cell cell = row.createCell(1);
        // 5.设置单元格内容
        // 2019-06 --》2019年6月份出货表，2019-11
        String result = inputDate.replaceAll("-0", "-").replaceAll("-", "年") + "月份出货表";
        cell.setCellValue(result);
        // 设置单元格样式
        cell.setCellStyle(this.bigTitle(workbook));

        // 第二部：导出第二行
        String[] titles = {"客户", "订单号", "货号", "数量", "工厂", "工厂交期", "船期", "贸易条款"};
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        // 创建第二行的每一列
        for (int i = 0;i < titles.length;i++){
            cell = row.createCell(i + 1);
            // 设置列内容
            cell.setCellValue(titles[1]);
            // 设置列样式
            cell.setCellStyle(this.title(workbook));
        }
        // 第三步：导出数据行,从第三行开始
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
        if (list != null && list.size() > 0) {
            int num = 2;
            for (ContractProductVo contractProductVo : list) {
                for (int i = 0; i < 20000; i++) {
                    row = sheet.createRow(num++);
                    cell = row.createCell(1);

                    cell.setCellValue(contractProductVo.getCustomName());

                    cell = row.createCell(2);
                    cell.setCellValue(contractProductVo.getContractNo());

                    cell = row.createCell(3);
                    cell.setCellValue(contractProductVo.getProductNo());


                    cell = row.createCell(4);
                    if (contractProductVo.getCnumber() == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(contractProductVo.getCnumber());
                    }

                    cell = row.createCell(5);
                    cell.setCellValue(contractProductVo.getFactoryName());
                    cell.setCellStyle(this.text(workbook));

                    cell = row.createCell(6);
                    cell.setCellValue(contractProductVo.getDeliveryPeriod());

                    cell = row.createCell(7);
                    cell.setCellValue(contractProductVo.getShipTime());

                    cell = row.createCell(8);
                    cell.setCellValue(contractProductVo.getTradeTerms());
                }
            }
        }

        /* 第四步：下载 */
    DownloadUtil downloadUtil = new DownloadUtil();
    // 缓冲流
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    // excel文件流 -- 》缓冲流
    workbook.write(outputStream);
    // 下载（缓冲流 -- 》response输出流）
    downloadUtil.download(outputStream,response,"出货表.xlsx");
    workbook.close();

}

    //大标题的样式 bigTitle(Workbook wb){
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }
}
