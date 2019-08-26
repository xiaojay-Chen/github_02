package cn.itcast.rabbit.listener;


import cn.itcast.rabbit.utils.MailUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 发送邮件
 */
@Component
public class EmailListener implements MessageListener{

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        try {
            JsonNode jsonNode = MAPPER.readTree(message.getBody());

            // 获取队列中的消息
            String to = jsonNode.get("to").asText();
            String subject = jsonNode.get("subject").asText();
            String content = jsonNode.get("content").asText();
            // 打印
            System.out.println("获取队列中的消息:"+ to);
            System.out.println("获取队列中的消息:"+ subject);
            System.out.println("获取队列中的消息:"+ content);

            // 发送邮件
            MailUtil.sendMsg(to,subject,content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
