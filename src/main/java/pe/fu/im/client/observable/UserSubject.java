package pe.fu.im.client.observable;

import java.util.List;

import pe.fu.im.client.bean.UserInfo;
import pe.fu.im.client.constant.PresenceMode;
import pe.fu.im.client.constant.PresenceType;
import pe.fu.im.client.utils.BaseObservable;

/**
 * 
 * 用户主题（可被观察的）
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月3日
 * @since
 * @version
 */
public class UserSubject extends BaseObservable {

	/**
	 * 上线通知
	 * 
	 * @param jid
	 * @param presenceMode
	 *            上线模式
	 */
	public void onlineNotify(String jid, List<UserInfo> friends, PresenceMode presenceMode) {
		UserInfo userInfo = new UserInfo();
		userInfo.setJid(jid);
		userInfo.setStatus(PresenceType.available);
		userInfo.setFriends(friends);
		userInfo.setMode(presenceMode);

		Object[] arrayOfObject = getObserversArray();
		if (arrayOfObject.length > 0)
			for (int i = arrayOfObject.length - 1; i >= 0; i--)
				((UserObserver) arrayOfObject[i]).online(userInfo);
	}

	/**
	 * 下线通知
	 * 
	 * @param jid
	 */
	public void offlineNotify(String jid) {
		Object[] arrayOfObject = getObserversArray();
		if (arrayOfObject.length > 0)
			for (int i = arrayOfObject.length - 1; i >= 0; i--)
				((UserObserver) arrayOfObject[i]).offline(jid);
	}

	/**
	 * 添加好友通知
	 * 
	 * @param jid
	 * @param friend
	 */
	public void friending(String jid, UserInfo friend) {
		Object[] arrayOfObject = getObserversArray();
		if (arrayOfObject.length > 0)
			for (int i = arrayOfObject.length - 1; i >= 0; i--)
				((UserObserver) arrayOfObject[i]).friending(jid, friend);
	};

	/**
	 * 删除好友通知
	 * 
	 * @param friendJid
	 */
	public void removeFriendNotify(String friendJid) {

	}

}
