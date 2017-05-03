package pe.fu.im.client.api;

import pe.fu.im.client.bean.CallResult;

/**
 * 
 * 匿名服务
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月4日
 * @since 
 * @version 
 */
public interface AnonymousService {
	/**
	 * 注册一个用户
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public CallResult regist(String username, String password) throws Exception;
	
	
}
