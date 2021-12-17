package com.gyjian.websocket.controller;

import com.gyjian.websocket.model.ChatMessage;
import com.gyjian.websocket.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


/**
 * 我们在websocket配置中，从目的地以/app开头的客户端发送的所有消息都将路由到这些使用@MessageMapping注释的消息处理方法。
 * 例如，具有目标/app/chat.sendMessage的消息将路由到sendMessage（）方法，并且具有目标/app/chat.addUser的消息将路由到addUser（）方法
 */
@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Value("${redis.set.onlineUsers}")
    private String onlineUsers;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 客户端发送到 /app/chat.sendMessage 时，把消息路由到 /topic/public ，即所有人都能收到
     * @param chatMessage
     * @return
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 客户端发送到 /app/chat.addUser 时，增加一个 username 属性，再把消息路由到 /topic/public ，即所有人都能收到
     * @param chatMessage
     * @return
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        logger.info("message addUser reveived, sender:{}", chatMessage.getSender());

        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        // 往redis中广播用户上线的消息，并把用户名username写入redis的set中（websocket.onlineUsers
        redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
        redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));

        return chatMessage;
    }

}
