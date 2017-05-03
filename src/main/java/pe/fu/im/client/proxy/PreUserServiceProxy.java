package pe.fu.im.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jivesoftware.smack.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.fu.im.client.bean.CallResult;
import pe.fu.im.client.constant.CallStatus;
import pe.fu.im.client.utils.ReflectUtils;

/**
 * 
 * 用户服务代理类
 * 
 * <p>调用前置处理，检查连接状态等</p>
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月6日
 * @since 
 * @version 
 */
public class PreUserServiceProxy implements InvocationHandler {

	private static final Logger LOG = LoggerFactory.getLogger(PreUserServiceProxy.class);

	private Object userServiceImpl;//

	public PreUserServiceProxy(Object userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();

		// 获取连接
		Connection connection = ReflectUtils.getFieldValue("connection", userServiceImpl, Connection.class);
		// 获取用户名
		String username = ReflectUtils.getFieldValue("username", userServiceImpl, String.class);
		CallResult result = new CallResult();
		if (connection == null) {
			result.setStatus(CallStatus.ERROR.getCode());
			result.setNote("无连接！");
			LOG.warn("用户【{}】调用{}服务，但与服务器无连接，无法进行操作！", username, methodName);
			return result;
		} else {
			if (!"login".equals(methodName)) {
				if (!connection.isConnected()) {
					result.setStatus(CallStatus.ERROR.getCode());
					result.setNote("连接处于断开中！");
					LOG.warn("用户【{}】调用{}服务，但与服务器连接处于断开中，无法进行操作！", username, methodName);
					return result;
				}
				if (connection.isAnonymous()) {
					result.setStatus(CallStatus.ERROR.getCode());
					result.setNote("连接为匿名连接！");
					LOG.warn("用户【{}】调用{}服务，但连接为匿名连接，无法进行操作！", username, methodName);
					return result;
				}
			}
			return method.invoke(userServiceImpl, args);
		}

	}
}
