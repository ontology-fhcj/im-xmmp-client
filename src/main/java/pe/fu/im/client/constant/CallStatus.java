package pe.fu.im.client.constant;

/**
 * 
 * 调用状态
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月6日
 * @since
 * @version
 */
public enum CallStatus {
	/**
	 * 调用成功
	 */
	SUCCESS {
		@Override
		public int getCode() {
			return 1;
		}
	},
	/**
	 * 调用错误
	 */
	ERROR {
		@Override
		public int getCode() {
			return -1;
		}
	};

	public abstract int getCode();

}
