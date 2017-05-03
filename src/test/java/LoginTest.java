/** 
 * 四川数码物联网络科技有限责任公司  (c)2013-2014  All right reserved. 
 */


import pe.fu.im.client.ServiceEngine;
import pe.fu.im.client.api.UserService;

/**
 * 
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月2日
 * @since 
 * @version 
 */
public class LoginTest {
	
	public static UserService login(String username,String password) throws Exception{
		UserService user = ServiceEngine.getUserService(username, password);
		user.login();
		return user;
	}
	
	public static void main(String[] args) {
		try {
			//--------
			login("admin", "123456");
			
			while (true) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
