## IM即时通讯客户端
- 基于XMMP协议
- 通过smack.jar,smackx.jar封装-[http://www.igniterealtime.org/projects/smack/](http://www.igniterealtime.org/projects/smack/)

### 使用

1. 配置xmpp-server-config.xml
    

```
<?xml version="1.0" encoding="UTF-8"?>
<xmppServer>
	<!-- openfire 服务器 -->
	<openfire ip="127.0.0.1" port="5222" domain="127.0.0.1" serviceName="test"></openfire>

	<!-- 其他通讯服务器实现  -->
</xmppServer>
```
2、登录

```
UserService user = ServiceEngine.getUserService("admin", "123456");
user.login();
```
3、开始聊天

```
//1、登录
UserService admin = ServiceEngine.getUserService("admin", "123456");
admin.login();
//2、选择与谁聊天
ChatService chatservice = admin.startChat("test");
//3、添加聊天接受聊天消息监听
chatservice.addMessageListener(new IMessage() {
	@Override
	public void handle(MessageObject messageObject) throws Exception {
	    System.out.println(messageObject.getFrom()+":"+messageObject.getContent());
	}
});
//3、发送消息
chatservice.sendMessage("你好，我是admin用户！");
```
