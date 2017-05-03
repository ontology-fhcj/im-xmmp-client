package pe.fu.im.client.utils;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import pe.fu.im.client.constant.PresenceMode;
import pe.fu.im.client.constant.PresenceType;

/**
 * 
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2016年8月4日
 * @since 
 * @version 
 */
public class TransUtil {
	/**
	 * 
	 * @param presenceMode
	 * @return
	 */
	public static Presence.Mode transSmackMode(PresenceMode presenceMode) {
		for (Presence.Mode mode : Presence.Mode.values()) {
			if (mode.name().equals(presenceMode.name())) {
				return mode;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param presenceType
	 * @return
	 */
	public static Presence.Type transSmackType(PresenceType presenceType) {
		for (Type type_ : Presence.Type.values()) {
			if (type_.name().equals(presenceType.name())) {
				return type_;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static PresenceType transType(Presence.Type type) {
		for (PresenceType presenceType : PresenceType.values()) {
			if (type.name().equals(presenceType.name())) {
				return presenceType;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param mode
	 * @return
	 */
	public static PresenceMode transMode(Presence.Mode mode) {
		for (PresenceMode presenceMode : PresenceMode.values()) {
			if (mode.name().equals(presenceMode.name())) {
				return presenceMode;
			}
		}
		return null;
	}
}
