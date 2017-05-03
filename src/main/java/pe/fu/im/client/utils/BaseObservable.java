package pe.fu.im.client.utils;

import java.util.Vector;

/**
 * 
 * <h3>观察者基类</h3>
 *
 * <P>
 * <span style='color:red'><em>填写类详细说明</em></span>
 * </P>
 * 
 * @author Huang Chen Jun Fu (<a href='mailto:475961393@qq.com'>请联系我^_^</a>)
 * @since 1.7
 * @version 1.0
 * @date 2017年5月3日
 * 
 */
public class BaseObservable {
	private boolean changed = false;
	private Vector<Object> obs;

	public BaseObservable() {
		this.obs = new Vector();
	}

	public synchronized void addObserver(Object paramObserver) {
		if (paramObserver == null) {
			throw new NullPointerException();
		}
		if (!this.obs.contains(paramObserver)) {
			this.obs.addElement(paramObserver);
		}
	}

	public synchronized void deleteObserver(Object paramObserver) {
		this.obs.removeElement(paramObserver);
	}

	public Object[] getObserversArray() {
		Object[] arrayOfObject = new Object[]{};
		synchronized (this) {
			setChanged();
			arrayOfObject = this.obs.toArray();
			clearChanged();
		}
		return arrayOfObject;
	}

	protected synchronized void setChanged() {
		this.changed = true;
	}

	protected synchronized void clearChanged() {
		this.changed = false;
	}

	public synchronized boolean hasChanged() {
		return this.changed;
	}

	public synchronized int countObservers() {
		return this.obs.size();
	}
}
