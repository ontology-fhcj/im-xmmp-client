
package pe.fu.im.client;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.fu.im.client.api.AnonymousService;
import pe.fu.im.client.api.ChatRoomService;
import pe.fu.im.client.api.ChatService;
import pe.fu.im.client.api.UserService;
import pe.fu.im.client.bean.CallResult;
import pe.fu.im.client.bean.MessageObject;
import pe.fu.im.client.bean.UserInfo;
import pe.fu.im.client.config.OpenfireConfig;
import pe.fu.im.client.config.XmppServerConfig;
import pe.fu.im.client.constant.CallStatus;
import pe.fu.im.client.constant.PresenceMode;
import pe.fu.im.client.constant.PresenceType;
import pe.fu.im.client.listener.UserConnectionListener;
import pe.fu.im.client.observable.UserSubject;
import pe.fu.im.client.proxy.PreUserServiceProxy;
import pe.fu.im.client.utils.JaxbUtils;
import pe.fu.im.client.utils.StringUtils;
import pe.fu.im.client.utils.TransUtil;

/**
 * 
 * 服务引擎
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月3日
 * @since 
 * @version 
 */
public class ServiceEngine {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceEngine.class);
	private static XmppServerConfig xmppServerConfig;

	private static final String AT = "@";

	private static final String CONFIG_PATH = "/xmpp-server-config.xml";

	/**
	 * 获取用户服务
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static UserService getUserService(String username, String password) throws Exception {
		ensure();
		UserServiceImpl userServiceImpl = new UserServiceImpl(username, password);
		userServiceImpl.addObserver(OnlineRoster.getInstance());// 添加观察者

		InvocationHandler ih = new PreUserServiceProxy(userServiceImpl);

		Class<? extends UserServiceImpl> clazz = userServiceImpl.getClass();
		// 返回代理对象
		return (UserService) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), ih);
	}

	/**
	 * 获取匿名服务
	 * @return
	 * @throws Exception
	 */
	public static AnonymousService getAnonymousService() throws Exception {
		return new AnonymousServiceImpl();
	}

	/**
	 * 获取IM服务器配置
	 * @return
	 */
	protected static final XmppServerConfig getXmppServerConfig() {
		ensure();
		return xmppServerConfig;

	}

	/**
	 * 获取openfire服务器配置
	 * @return
	 */
	protected static final OpenfireConfig getOpenfireConfig() {
		ensure();
		return xmppServerConfig.getOpenfire();
	}

	/**
	 * 确保配置不能为空
	 */
	private static void ensure() {
		if (xmppServerConfig == null) {
			loadXmppServerConfig();
		}
	}

	/**
	 * 初始化IM服务器配置
	 */
	private static void loadXmppServerConfig() {
		try {
			InputStream resourceAsStream = ServiceEngine.class.getResourceAsStream(CONFIG_PATH);
			xmppServerConfig = JaxbUtils.xml2Obj(XmppServerConfig.class, resourceAsStream);
		} catch (Exception e) {
			LOG.error("初始化IM服务器失败！:", e);
		}
	}

	/**
	 * 获取JID
	 * @param username
	 * @return
	 */
	private static String getJID(String username) {
		return username + AT + getOpenfireConfig().getDomain();
	}

	/**
	 * 
	 * 匿名服务实现
	 * 
	 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
	 *         2016年8月2日
	 * @since 
	 * @version 
	 */
	private static class AnonymousServiceImpl implements AnonymousService {
		/**
		 * 注册一个用户
		 * 
		 * @param username
		 * @param password
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult regist(String username, String password) throws Exception {
			CallResult result = new CallResult();
			if (StringUtils.isEmpty(username)) {
				throw new IllegalArgumentException("用户名不能空!");
			}
			if (StringUtils.isEmpty(password)) {
				throw new IllegalArgumentException("密码不能空!");
			}
			OpenfireConfig openfireConfig = getOpenfireConfig();
			String ip = openfireConfig.getIp();
			if (StringUtils.isEmpty(ip)) {
				throw new IllegalArgumentException("配置的IP不能空!");
			}
			Integer port = openfireConfig.getPort();
			if (port == null) {
				throw new IllegalArgumentException("配置的端口号不能空!");
			}
			try {
				Connection connection = new XMPPConnection(new ConnectionConfiguration(ip, port));
				connection.getAccountManager().createAccount(username, password);

				result.setStatus(CallStatus.SUCCESS.getCode());
				result.setNote("注册成功！");
			} catch (Exception e) {
				LOG.error("注册用户失败：{}", e);
				result.setStatus(CallStatus.ERROR.getCode());
				result.setNote("注册失败！" + e.getMessage());
			}
			return result;
		}

	}

	/**
	 * 
	 * 用户服务实现
	 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
	 *         2016年8月2日
	 * @since 
	 * @version 
	 */
	private static class UserServiceImpl extends UserSubject implements UserService {

		private Connection connection;
		private AccountManager accountManager;
		
		private UserConnectionListener connectionListener;
		
		private String username;
		private String password;

		public UserServiceImpl(String username, String password) throws Exception {
			if (StringUtils.isEmpty(username)) {
				throw new IllegalArgumentException("用户名不能空!");
			}
			if (StringUtils.isEmpty(password)) {
				throw new IllegalArgumentException("密码不能空!");
			}
			OpenfireConfig openfireConfig = getOpenfireConfig();
			String ip = openfireConfig.getIp();
			if (StringUtils.isEmpty(ip)) {
				throw new IllegalArgumentException("配置的IP不能空!");
			}
			Integer port = openfireConfig.getPort();
			if (port == null) {
				throw new IllegalArgumentException("配置的端口号不能空!");
			}
			ConnectionConfiguration connectionConfig = new ConnectionConfiguration(ip, port);
			connectionConfig.setReconnectionAllowed(true);// 设置可重连
			connectionConfig.setSendPresence(true);
			//connectionConfig.setSASLAuthenticationEnabled(false);
			this.connection = new XMPPConnection(connectionConfig);
			this.connection.connect();
			this.connectionListener = new UserConnectionListener(this);
			
			this.accountManager = connection.getAccountManager();
			this.username = username;
			this.password = password;
		}

		/**
		 * 登录
		 * 
		 * <p>以何种模式登录，隐身、勿扰、在线等</p>
		 * 
		 * @param presenceMode
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult login(PresenceMode presenceMode) throws Exception {
			CallResult result = new CallResult();
			try {
				if (!connection.isConnected()) {
					connection.connect();
				}
				this.connection.addConnectionListener(connectionListener);
				this.connection.login(username, password, getOpenfireConfig().getServiceName());
				setTypeMode(null, presenceMode, "");

				List<UserInfo> friends = new ArrayList<UserInfo>();
				Collection<RosterEntry> entries = this.connection.getRoster().getEntries();
				if (entries != null) {
					for (RosterEntry entry : entries) {
						String jid = entry.getUser();
						UserInfo userInfo = OnlineRoster.getInstance().cloneUserInfo(jid);
						friends.add(userInfo);
					}
				}
				// 通知所有观察者, 某用户已经上线
				super.onlineNotify(getJID(username), friends, presenceMode);
				result.setStatus(CallStatus.SUCCESS.getCode());
				result.setNote("登录成功！");
				LOG.info("用户{}已上线...", username);
			} catch (XMPPException e) {
				result.setStatus(CallStatus.ERROR.getCode());
				result.setNote("登录失败！");
				LOG.error("用户{}登录失败:{}", username, e.getMessage());
			}
			return result;
		}

		/**
		 * 登录
		 * 
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult login() throws Exception {
			return login(null);
		}

		@Override
		public CallResult logout() throws Exception {
			CallResult result = new CallResult();
			try {
				if (connection.isConnected()) {
					connection.disconnect();
					connection.removeConnectionListener(connectionListener);
					offlineNotify(getJID(username));// 通知所有观察者 某用户已经下线
					result.setStatus(CallStatus.SUCCESS.getCode());
					result.setNote("下线成功！");
					LOG.info("用户{}已下线...", username);
				}
			} catch (Exception e) {
				result.setStatus(CallStatus.ERROR.getCode());
				result.setNote("下线失败！");
				LOG.error("用户{}下线失败：{}", username, e.getMessage());
			}

			return result;
		}

		/**
		 * 删除账户
		 * 
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult remove() throws Exception {
			CallResult result = new CallResult();
			try {
				accountManager.deleteAccount();
				result.setStatus(CallStatus.SUCCESS.getCode());
				result.setNote("删除成功！");
			} catch (Exception e) {
				result.setStatus(CallStatus.ERROR.getCode());
				result.setNote("删除失败！");
				LOG.error("用户{}删除失败：{}", username, e);
			}
			return result;
		}

		/**
		 * 修改密码
		 * 
		 * @param newpassword 新密码
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult modifyPassword(String newpassword) throws Exception {
			CallResult result = new CallResult();
			try {
				accountManager.changePassword(newpassword);
				result.setStatus(CallStatus.SUCCESS.getCode());
				result.setNote("密码修改成功！");
			} catch (Exception e) {
				result.setStatus(CallStatus.ERROR.getCode());
				result.setNote("密码修改失败！");
				LOG.error("用户{}密码修改失败：{}", username, e);
			}

			return result;
		}

		/**
		 * 添加好友
		 * 
		 * @param username 用户名
		 * @param nickname 昵称
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult friending(String username, String nickname) throws Exception {
			return friending(username, nickname, null);
		}

		/**
		 * 获取所有好友信息
		 * @return
		 * @throws Exception
		 */
		@Override
		public Collection<UserInfo> getAllfriends() throws Exception {
			OnlineRoster instance = OnlineRoster.getInstance();
			UserInfo findUserInfo = instance.findUserInfo(getJID(username));
			if (findUserInfo != null) {
				return findUserInfo.getFriends();
			}
			return null;
		}

		/**
		 * 添加好友
		 * 
		 * @param username 用户名
		 * @param nickname 昵称
		 * @param groupname 组名
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult friending(String fusername, String nickname, String groupname) throws Exception {
			CallResult result = new CallResult();
			try {
				Roster roster = connection.getRoster();
				String fjid = getJID(fusername);
				if (StringUtils.isEmpty(groupname)) {
					roster.createEntry(fjid, nickname, null);
				} else {
					roster.createEntry(fjid, nickname, new String[] { groupname });
				}

				Presence presence = roster.getPresence(fjid);
				UserInfo friend = new UserInfo();
				friend.setJid(fjid);
				friend.setNickname(nickname);
				friend.setMode(TransUtil.transMode(presence.getMode()));
				friend.setStatus(TransUtil.transType(presence.getType()));
				friend.setNote(presence.getStatus());
				// 通知
				super.friending(getJID(username), friend);
				result.setNote("添加好友成功！");
				result.setStatus(CallStatus.SUCCESS.getCode());
			} catch (Exception e) {
				result.setNote("添加好友失败！");
				result.setStatus(CallStatus.ERROR.getCode());
				LOG.error("用户{}添加好友失败：{}", username, e.getMessage());
			}

			return result;
		}

		/**
		 * 与一位好友开始聊天
		 * 
		 * @param friendname 好友用户名
		 */
		@Override
		public ChatService startChat(String friendname) throws Exception {
			if (connection == null) {
				throw new NullPointerException("连接为null！");
			}

			Chat chat = connection.getChatManager().createChat(getJID(friendname), null);

			return new ChatServiceImpl(chat, friendname, connection);
		}

		/**
		 * 设置用户状态和模式
		 * 
		 * @param userPresence
		 * @return
		 * @throws Exception
		 */
		@Override
		public CallResult setPresence(UserInfo userPresence) throws Exception {
			CallResult result = new CallResult();
			setTypeMode(userPresence.getStatus(), userPresence.getMode(), userPresence.getNote());
			return result;
		}

		@Override
		public ChatRoomService createChatRoom() throws Exception {
			if (connection == null) {
				throw new NullPointerException("连接为null！");
			}

			MultiUserChat chatRoom = new MultiUserChat(connection, "testgroup@fhcj-pc");
			chatRoom.create("房间1");

			return new ChatRoomServiceImpl(chatRoom);
		}

		/**
		 * 设置模式状态
		 * @param presenceType
		 * @param presenceMode
		 * @param note
		 * @return
		 */
		private Boolean setTypeMode(PresenceType presenceType, PresenceMode presenceMode, String note) {
			if (presenceType == null) {
				presenceType = PresenceType.available;// 默认在线
			}
			if (presenceMode == null) {
				presenceMode = PresenceMode.available;// 默认为在线模式
			}
			Type transSmackType = TransUtil.transSmackType(presenceType);
			Mode transSmackMode = TransUtil.transSmackMode(presenceMode);

			if (transSmackType != null) {
				Presence presence = new Presence(transSmackType);
				presence.setStatus(note);
				presence.setMode(transSmackMode);
				connection.sendPacket(presence);
			}
			return null;
		}

	}

	/**
	 * 
	 * 聊天服务实现
	 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
	 *         2016年8月2日
	 * @since 
	 * @version 
	 */
	private static class ChatServiceImpl implements ChatService {

		private Chat chat;

		private Connection connection;

		private String targetUsername;

		public ChatServiceImpl(Chat chat, String targetUsername, Connection connection) throws XMPPException {
			this.chat = chat;
			this.connection = connection;
			this.targetUsername = targetUsername;
		}

		@Override
		public void sendMessage(String content) throws Exception {

			Message message = new Message();
			message.setBody(content);
			chat.sendMessage(content);
			// C_LOG.info("向{}发送了消息：{}", targetUsername, content);
		}

		@Override
		public void addMessageListener(final IMessage iMessage) throws Exception {

			chat.addMessageListener(new MessageListener() {
				@Override
				public void processMessage(Chat arg0, Message arg1) {
					MessageObject messageObject = new MessageObject();
					messageObject.setContent(arg1.getBody());
					messageObject.setFrom(arg1.getFrom());
					messageObject.setTo(arg1.getTo());
					try {
						iMessage.handle(messageObject);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

	}

	private static class ChatRoomServiceImpl implements ChatRoomService {

		public ChatRoomServiceImpl(MultiUserChat chatRoom) {
		}

	}
}
