package pe.fu.im.client.bean;

import java.util.List;

import pe.fu.im.client.constant.PresenceMode;
import pe.fu.im.client.constant.PresenceType;

/**
 * 
 * 用户信息
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月3日
 * @since 
 * @version 
 */
public class UserInfo {
	/**
	 * jid 
	 */
	private String jid;
	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 用户状态
	 */
	private PresenceType status;
	/**
	 * 用户模式
	 */
	private PresenceMode mode;
	/**
	 * 说明
	 */
	private String note;
	/**
	 * 所有好友
	 */
	private List<UserInfo> friends;

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public PresenceType getStatus() {
		return status;
	}

	public void setStatus(PresenceType status) {
		this.status = status;
	}

	public PresenceMode getMode() {
		return mode;
	}

	public void setMode(PresenceMode mode) {
		this.mode = mode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<UserInfo> getFriends() {
		return friends;
	}

	public void setFriends(List<UserInfo> friends) {
		this.friends = friends;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


}
