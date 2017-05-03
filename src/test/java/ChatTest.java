/** 
 * 四川数码物联网络科技有限责任公司  (c)2013-2014  All right reserved. 
 */


import pe.fu.im.client.IMessage;
import pe.fu.im.client.api.ChatService;
import pe.fu.im.client.api.UserService;
import pe.fu.im.client.bean.MessageObject;

/**
 * 
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月2日
 * @since 
 * @version 
 */
public class ChatTest {
	public static void main(String[] args) {
		try {
			
			UserService admin = LoginTest.login("admin", "123456");
			
			ChatService startChat = admin.startChat("test");
			startChat.addMessageListener(new IMessage() {
				
				@Override
				public void handle(MessageObject messageObject) throws Exception {
					System.out.println(messageObject.getFrom()+":"+messageObject.getContent());
				}
			});
			
			//--------
			UserService test = LoginTest.login("test", "123456");
			
			ChatService startChat2 = test.startChat("admin");
			startChat2.addMessageListener(new IMessage() {
				
				@Override
				public void handle(MessageObject messageObject) throws Exception {
					System.out.println(messageObject.getFrom()+":"+messageObject.getContent());
				}
			});
			
			startChat2.sendMessage("你好，我是测试用户！");
			
	
			while (true) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
