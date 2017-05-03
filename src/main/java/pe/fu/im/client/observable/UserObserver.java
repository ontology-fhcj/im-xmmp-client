package pe.fu.im.client.observable;

import pe.fu.im.client.bean.UserInfo;

/**
 * 
 * 用户观察者接口
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月4日
 * @since 
 * @version 
 */
public interface UserObserver {

	public void online(UserInfo userInfo);

	public void offline(String jid);

	public void removeFriend(String friendJid);

	public void friending(String jid, UserInfo friend);
}
