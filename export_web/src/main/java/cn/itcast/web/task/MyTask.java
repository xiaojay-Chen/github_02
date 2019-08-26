package cn.itcast.web.task;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务
 */
public class MyTask {

    public void excete(){
        System.out.println("当前时间："+
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"
                ).format(new Date()));
    }
}
