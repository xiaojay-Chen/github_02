package cn.itcast.stat;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 基于main方法开启提供者
 */
public class StatProvider {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");

        applicationContext.start();
        System.in.read();
    }
}
