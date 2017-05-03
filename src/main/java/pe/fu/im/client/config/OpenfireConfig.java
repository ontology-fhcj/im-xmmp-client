package pe.fu.im.client.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * openfire服务器配置
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月2日
 * @since 
 * @version 
 */
@XmlRootElement(name = "openfire")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenfireConfig {
	/**
	 * 服务器IP
	 * 
	 * <p><b>127.0.0.1</b></p>
	 * 
	 */
	@XmlAttribute
	private String ip;
	/**
	 * 服务器端口
	 * 
	 * <p><b>5222</b></p>
	 * 
	 */
	@XmlAttribute
	private Integer port;
	/**
	 * 服务器名称(域)
	 */
	@XmlAttribute
	private String domain;

	/**
	 * 服务名
	 */
	@XmlAttribute
	private String serviceName;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
