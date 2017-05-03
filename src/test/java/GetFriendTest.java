/** 
 * 四川数码物联网络科技有限责任公司  (c)2013-2014  All right reserved. 
 */

import java.util.Collection;

import pe.fu.im.client.IMessage;
import pe.fu.im.client.api.ChatService;
import pe.fu.im.client.api.UserService;
import pe.fu.im.client.bean.MessageObject;
import pe.fu.im.client.bean.UserInfo;
import pe.fu.im.client.constant.PresenceMode;
import pe.fu.im.client.constant.PresenceType;

/**
 * 
 * 获取好友列表
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月2日
 * @since
 * @version
 */
public class GetFriendTest {
	public static void main(String[] args) {
		try {

			UserService admin = LoginTest.login("admin", "123456");
			LoginTest.login("test", "123456");
			Collection<UserInfo> allfriends = admin.getAllfriends();
			for (UserInfo userInfo : allfriends) {
				System.out.println("好友：" + userInfo.getJid());
			}
			while (true) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
