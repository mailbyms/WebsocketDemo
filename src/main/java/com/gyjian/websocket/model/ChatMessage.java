package com.gyjian.websocket.model;

public class ChatMessage {
    private MessageType type;   //type:消息类型
    private String content;     //content：消息内容
    private String sender;      //sender：发送者

    /** 消息类型
     * CHAT: 消息
     * JOIN：加入
     * LEAVE：离开
     */
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
