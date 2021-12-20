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
- 如果使用 RabbitMQ 代理消息，需要在 RabbitMQ 里打开 plugin `rabbitmq_stomp` （会监听 61613 端口）

【以上是 tag 1.0_standalone 的内容】

## tag2.0_cluster
集群版本，改为2个服务器，共用一个 redis作消息队列代理，
- 改动内容
> redis 开启消息队列
> 2个服务器都订阅 redis 的消息队列
> 2个服务器收到消息，都发到 redis 的消息队列
- 使用方法
> IDEA 在 Run/Debug Configurations 里设置 Allow mutiple instances（允许多个实例同时运行）  
> 分别设置服务器为 8080， 8081 端口，运行  
> 用户 A 和 B 分别登录 2 个服务器，发消息都能看到