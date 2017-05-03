package pe.fu.im.client.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * xmpp协议 服务器配置
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月2日
 * @since 
 * @version 
 */
@XmlRootElement(name = "xmppServer")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmppServerConfig {
	/**
	 * openfire
	 */
	@XmlElement
	private OpenfireConfig openfire;

	public OpenfireConfig getOpenfire() {
		return openfire;
	}

	public void setOpenfire(OpenfireConfig openfire) {
		this.openfire = openfire;
	}

}
