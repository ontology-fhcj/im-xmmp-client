package pe.fu.im.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;


final public class JaxbUtils {
	/**
	 * 对象转化为XML字符串
	 * 
	 * @param object
	 *            对象
	 * @return XML字符串
	 * @throws JAXBException
	 */
	public static String obj2Xml(Object object) throws JAXBException {
		StringWriter sw = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller ms = context.createMarshaller();
			ms.setProperty("jaxb.encoding", "UTF-8");
			// 需要标准格式化，如果需要取消下一句的注释
			// ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ms.marshal(object, sw);
			return sw.toString();
		} catch (JAXBException e) {
			throw e;
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 对象转化为XML字符串
	 * 
	 * @param object
	 *            对象
	 * @param encoding
	 *            编码
	 * @return XML字符串
	 * @throws JAXBException
	 */
	public static String obj2Xml(Object object, String encoding) throws JAXBException {
		StringWriter sw = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller ms = context.createMarshaller();
			if (!StringUtils.isEmpty(encoding)) {
				ms.setProperty("jaxb.encoding", encoding);
			}
			// 需要标准格式化，如果需要取消下一句的注释
			// ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ms.marshal(object, sw);
			return sw.toString();
		} catch (JAXBException e) {
			throw e;
		} finally {
			if (sw != null)
				try {
					sw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * XML字符串转化为对象
	 * 
	 * @param clazz
	 * @param xml
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2Obj(Class<T> clazz, String xml) throws JAXBException {
		StringReader sr = new StringReader(xml.trim());
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller ums = context.createUnmarshaller();
			return (T) ums.unmarshal(sr);
		} catch (JAXBException e) {
			throw e;
		} finally {
			sr.close();
		}
	}

	/**
	 * XML字符串转化为对象
	 * 
	 * @param clazz
	 * @param xml
	 * @param path
	 * @return
	 * @throws JAXBException
	 * @throws DocumentException 
	 */
	public static <T> T xml2Obj(Class<T> clazz, String xml, String path) throws JAXBException, DocumentException {
		try {
			Document doc = DocumentHelper.parseText(xml.trim());

			Node el = doc.selectSingleNode(path);
			return JaxbUtils.xml2Obj(clazz, el.asXML());
		} catch (DocumentException e) {
			throw e;
		}
	}

	/**
	 * Xml流转换为对象
	 * 
	 * @param clazz
	 * @param is
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static <T> T xml2Obj(Class<T> clazz, InputStream is) throws IOException, JAXBException {
		String xml = StringUtils.inputStreamToString(is, null);
		return xml2Obj(clazz, xml.trim());
	}
}
