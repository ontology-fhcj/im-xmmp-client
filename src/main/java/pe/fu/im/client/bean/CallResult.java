package pe.fu.im.client.bean;

/**
 * 
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月6日
 * @since 
 * @version 
 */
public class CallResult {

	private int status;

	private String note;

	private Object returnValue;

	public CallResult(int status) {
		super();
		this.status = status;
	}

	public CallResult() {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

}
