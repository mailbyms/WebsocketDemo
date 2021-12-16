# WebSocket 多人在线聊天室（SpringBoot + WebSocket）

来源：<https://blog.csdn.net/qqxx6661/article/details/98883166>  
来源的来源：<https://www.callicoder.com/spring-boot-websocket-chat-example/>

## 客户端流程
- 连接上服务器
- 发送 /app/chat.addUser 汇报客户端的名字
- 订阅 /topic/public 频道
- 【可选】发送一条消息

## 服务器流程
- WebSocketConfig 
  - 定义 /ws 端点，提供给客户端连接
  - 定义 /app 端点，提供给客户端发消息
  - 代理 /topic 频道消息的订阅发布（可选，可替换成 RabbitMQ 或者 ActiveMQ）
- ChatController
  - 当收到客户端发送到 /app/chat.addUser 的消息时，增加一个 username 属性，记录发送者的名字，再把消息路由到 /topic/public ，即订阅的所有人都能收到
  - 当收到客户端发送到 /app/chat.sendMessage 的消息时，把消息路由到 /topic/public ，即订阅的所有人都能收到

## 注意
- 目前消息都广播发送，并没有一对一发消息