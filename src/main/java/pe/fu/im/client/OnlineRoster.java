package pe.fu.im.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pe.fu.im.client.bean.UserInfo;
import pe.fu.im.client.constant.PresenceMode;
import pe.fu.im.client.constant.PresenceType;
import pe.fu.im.client.observable.UserObserver;

/**
 * 
 * 在线登记薄，上线的用户会存起来
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月3日
 * @since 
 * @version 
 */
public class OnlineRoster implements UserObserver {

	private static OnlineRoster onlineRoster = new OnlineRoster();

	private static Map<String, UserInfo> userStatusList = new ConcurrentHashMap<String, UserInfo>();

	/**
	 * 查找用户
	 * @param jid
	 * @return
	 */
	public UserInfo findUserInfo(String jid) {
		if (userStatusList.containsKey(jid)) {
			return (UserInfo) userStatusList.get(jid);

		}
		return null;
	}

	/**
	 * 
	 * @param jid
	 * @return
	 */
	public UserInfo cloneUserInfo(String jid) {
		UserInfo org = findUserInfo(jid);
		UserInfo copy = new UserInfo();
		if (org == null) {
			copy.setJid(jid);
			copy.setMode(getPresenceMode(jid));
			copy.setStatus(getPresenceType(jid));
		} else {
			copy.setJid(org.getJid());
			copy.setMode(org.getMode());
			copy.setNickname(org.getNickname());
			copy.setStatus(org.getStatus());
		}

		return copy;
	}

	/**
	 * 是否在线
	 * @param username
	 * @return
	 */
	public Boolean isAvailable(String jid) {
		PresenceType type = getPresenceType(jid);
		if (type == PresenceType.available) {
			return true;
		} else if (type == PresenceType.unavailable) {
			return false;
		}
		return false;
	}

	/**
	 * 获取用户状态
	 * 
	 * @param username
	 * @return
	 */
	public PresenceType getPresenceType(String jid) {
		if (userStatusList.containsKey(jid)) {
			UserInfo userInfo = userStatusList.get(jid);
			return userInfo.getStatus();

		}
		return PresenceType.unavailable;
	}

	/**
	 * 获取用户模式
	 * @param username
	 * @return
	 */
	public PresenceMode getPresenceMode(String jid) {
		if (userStatusList.containsKey(jid)) {
			UserInfo userInfo = userStatusList.get(jid);
			return userInfo.getMode();
		}
		return null;
	}

	@Override
	public void online(UserInfo userInfo) {
		userStatusList.put(userInfo.getJid(), userInfo);

	}

	@Override
	public void offline(String jid) {
		if (userStatusList.containsKey(jid)) {
			userStatusList.remove(jid);
		}
	}

	@Override
	public void removeFriend(String friendJid) {

	}

	@Override
	public void friending(String jid, UserInfo friend) {
		UserInfo findUserInfo = findUserInfo(jid);
		if (findUserInfo != null) {
			UserInfo exist = null;
			List<UserInfo> friends = findUserInfo.getFriends();
			for (UserInfo userInfo : friends) {
				if (userInfo.getJid().equals(friend.getJid())) {
					exist = userInfo;
				}
			}
			if (exist != null) {
				friends.remove(exist);
			}
			friends.add(friend);

		}
	}

	public static OnlineRoster getInstance() {
		if (onlineRoster == null) {
			onlineRoster = new OnlineRoster();
		}
		return onlineRoster;
	}

	private OnlineRoster() {
	}

}
