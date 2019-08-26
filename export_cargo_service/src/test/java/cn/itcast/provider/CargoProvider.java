package cn.itcast.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * main函数启动服务提供者
 */
public class CargoProvider {

    public static void main(String[] args) throws IOException {
        // 1.加载配置文件
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        // 启动
        ac.start();
        System.in.read();

    }
}
