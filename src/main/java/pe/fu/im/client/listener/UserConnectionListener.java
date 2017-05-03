package pe.fu.im.client.listener;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.ConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.fu.im.client.api.UserService;
import pe.fu.im.client.bean.CallResult;
import pe.fu.im.client.constant.CallStatus;

/**
 * 
 * 连接监听
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月9日
 * @since 
 * @version 
 */
public class UserConnectionListener implements ConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(UserConnectionListener.class);

	private Integer interval = 3000;

	private Integer reconnectConut = 1;

	private AtomicBoolean isReconnect = new AtomicBoolean(false);

	private ThreadPoolExecutor pool;

	private UserService userService;

	public UserConnectionListener(UserService userService) {
		this.userService = userService;
		pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				new ThreadFactory() {

					@Override
					public Thread newThread(Runnable runnable) {
						Thread thread = new Thread(runnable, "thread-reconnect");
						thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

							public void uncaughtException(Thread t, Throwable e) {
								startThread();
							}

						});
						return thread;
					}
				});
		this.startThread();
	}

	/**
	 * 重连任务
	 */
	class ReConnectTask implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(interval);
					if (isReconnect.get()) {
						reConnect();
					}
				}
			} catch (Exception e) {
				throw new RuntimeException();
			}

		}
	}

	/**
	 * 异常关闭
	 */
	@Override
	public void connectionClosedOnError(Exception exception) {
		LOG.info("与openfire服务器异常连接异常断开，准备重新登录...");
		boolean error = exception.getMessage().equals("stream:error (conflict)");
		if (!error) {
			startReConnect();
		}
	}

	/**
	 * 开始
	 */
	public void startThread() {
		pool.execute(new ReConnectTask());
	}

	/**
	 * 开始重连
	 */
	public void startReConnect() {
		isReconnect.set(true);
	}

	/**
	 * 结束重连
	 */
	public void endReConnect() {
		isReconnect.set(false);
		reconnectConut = 1;
	}

	/**
	 * 重连
	 */
	public void reConnect() {
		LOG.info("与openfire服务器尝试第{}次重新登录...", reconnectConut);
		reconnectConut++;
		try {
			userService.logout();
			CallResult result = userService.login();
			if(result.getStatus()==CallStatus.SUCCESS.getCode()){
				this.endReConnect();
				
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 正常关闭
	 */
	@Override
	public void connectionClosed() {
	}

	/**
	 * 重连成功
	 */
	@Override
	public void reconnectionSuccessful() {
	}

	/**
	 * 重连失败
	 */
	@Override
	public void reconnectionFailed(Exception exception) {
	}

	@Override
	public void reconnectingIn(int arg0) {
	}
}