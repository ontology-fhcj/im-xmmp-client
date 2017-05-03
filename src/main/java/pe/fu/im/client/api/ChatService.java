package pe.fu.im.client.api;

import pe.fu.im.client.IMessage;

/**
 * 聊天服务
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月2日
 * @since 
 * @version 
 */
public interface ChatService {
	/**
	 * 发送信息
	 * 
	 * @param content
	 * @throws Exception
	 */
	public void sendMessage(String content) throws Exception;

	/**
	 * 添加接收消息的监听
	 * 
	 * @param messageListener
	 * @throws Exception
	 */
	public void addMessageListener(IMessage iMessage) throws Exception;
	
	
	
}
