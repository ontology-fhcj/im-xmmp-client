package pe.fu.im.client.api;

import java.util.Collection;

import pe.fu.im.client.bean.CallResult;
import pe.fu.im.client.bean.UserInfo;
import pe.fu.im.client.constant.PresenceMode;

/**
 * 
 * 用户服务
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月2日
 * @since 
 * @version 
 */
public interface UserService {

	/**
	 * 登录
	 * 
	 * @return
	 * @throws Exception
	 */
	public CallResult login() throws Exception;


	/**
	 * 登录
	 * 
	 * <p>以何种模式登录，隐身、勿扰、在线等</p>
	 * 
	 * @param presenceMode
	 * @return
	 * @throws Exception
	 */
	public CallResult login(PresenceMode presenceMode) throws Exception;
	
	/**
	 * 登出
	 * 
	 * @return
	 * @throws Exception
	 */
	public CallResult logout() throws Exception;
	/**
	 * 修改密码
	 * 
	 * @param newpassword 新密码
	 * @return
	 * @throws Exception
	 */
	public CallResult modifyPassword(String newpassword) throws Exception;

	/**
	 * 删除账户
	 * 
	 * @return
	 * @throws Exception
	 */
	public CallResult remove() throws Exception;

	/**
	 * 添加好友
	 * 
	 * @param username 用户名
	 * @param nickname 昵称
	 * @return
	 * @throws Exception
	 */
	public CallResult friending(String username, String nickname) throws Exception;

	/**
	 * 添加好友
	 * 
	 * @param username 用户名
	 * @param nickname 昵称
	 * @param groupname 组名
	 * @return
	 * @throws Exception
	 */
	public CallResult friending(String username, String nickname, String groupname) throws Exception;

	/**
	 * 获取所有好友信息
	 * @return
	 * @throws Exception
	 */
	public Collection<UserInfo> getAllfriends() throws Exception;

	/**
	 * 与一位好友开始聊天
	 * 
	 * @param friendname 好友用户名
	 */
	public ChatService startChat(String friendname) throws Exception;

	/**
	 * 设置用户状态和模式
	 * 
	 * <p><b> 状态为PresenceType.available时，模式才有作用，状态为PresenceType.unavailable时，可当作隐身 </b></p>
	 * 
	 * @param userPresence
	 * @return
	 * @throws Exception
	 */
	public CallResult setPresence(UserInfo userPresence) throws Exception;

	public ChatRoomService createChatRoom() throws Exception;

}
