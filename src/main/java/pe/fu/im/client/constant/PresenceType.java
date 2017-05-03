package pe.fu.im.client.constant;

/**
 * 
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月4日
 * @since 
 * @version 
 */
public enum PresenceType {
	/**
	 * 表示处于在线状态
	 */
	available,
	/**
	 * 表示处于离线状态
	 */
	unavailable,
	/**
	 * 表示发出添加好友的申请
	 */
	subscribe,
	/**
	 * 表示发出删除好友的申请
	 */
	unsubscribe,
	/**
	 * 表示拒绝添加对方为好友
	 */
	unsubscribed;
	
}
