package pe.fu.im.client;

import pe.fu.im.client.bean.MessageObject;

/**
 * 
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月4日
 * @since 
 * @version 
 */
public interface IMessage {
	public void handle(MessageObject messageObject) throws Exception;
}
