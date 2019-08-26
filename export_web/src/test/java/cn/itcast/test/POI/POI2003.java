package cn.itcast.test.POI;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Excel 2003:new HSSFWorkbook(); / 2007:new XSSFWorkbook()
 */
public class POI2003 {

    /* 往excel写入内容 */
    @Test
    public void write() throws Exception {
        // 1.创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 2.创建工作表
        HSSFSheet sheet = workbook.createSheet();
        // 3.创建第一行
        HSSFRow row = sheet.createRow(0);
        // 4.创建第一行的第一列
        HSSFCell cell = row.createCell(0);
        // 5.往单元格设置内容
        cell.setCellValue("今天。。。");

        // 6.输出
        workbook.write(new FileOutputStream("F:\\test.xls"));
        workbook.close();
    }

    /* 读取excel内容 */
    @Test
    public void read() throws Exception {
        // 1.根据excel文件流获取工作簿
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("F:\\test.xls"));
        // 2.获取工作表
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 3.获取第一行
        HSSFRow row = sheet.getRow(0);
        // 4.获取第一行第一列
        HSSFCell cell = row.getCell(0);
        // 5.获取单元格（内容）
        System.out.println("单元格（内容）:"+cell.getStringCellValue());
        System.out.println("总行数:"+sheet.getPhysicalNumberOfRows());
        System.out.println("总列数:"+row.getPhysicalNumberOfCells());

        // 6.关闭流
        workbook.close();
    }
}

