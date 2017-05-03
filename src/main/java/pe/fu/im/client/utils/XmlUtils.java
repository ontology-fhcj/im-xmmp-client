package pe.fu.im.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;

public abstract class XmlUtils {

	/**
	 * 创建一个XML文档
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static Document parseDocument(String xml) throws DocumentException {
		if (StringUtils.isEmpty(xml)) {
			return DocumentHelper.createDocument();
		}
		return DocumentHelper.parseText(xml);
	}

	/**
	 * 创建一个XML文档
	 * 
	 * @param file
	 * @param charsetName
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Document parseDocument(File file, String charsetName) throws DocumentException, IOException {
		return parseDocument(FileUtils.readFile(file, charsetName));
	}

	/**
	 * 创建一个XML文档
	 * 
	 * @param is
	 * @param charsetName
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Document parseDocument(InputStream is, String charsetName) throws DocumentException, IOException {
		return parseDocument(StringUtils.inputStreamToString(is, charsetName));
	}

	/**
	 * 解析XML字符串生成XML元素对象
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static Element parseElement(String xml) throws DocumentException {
		return parseDocument(xml).getRootElement();
	}

	/**
	 * 根据文件名读取XML元素对象
	 * 
	 * @param file
	 * @param charsetName
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Element parseElement(File file, String charsetName) throws DocumentException, IOException {
		return parseElement(FileUtils.readFile(file, charsetName));
	}

	/**
	 * 根据XML字符流生成XML元素对象
	 * 
	 * @param is
	 * @param charsetName
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Element parseElement(InputStream is, String charsetName) throws DocumentException, IOException {
		return parseElement(StringUtils.inputStreamToString(is, charsetName));
	}

	/**
	 * 获取元素属性值
	 * 
	 * @param element
	 * @param attributeNames
	 *            为空则获取所有属性
	 * @return
	 */
	public static Map<String, String> getAttributes(Element element, List<String> attributeNames,
			boolean isIgnoreCase) {
		if (isIgnoreCase && attributeNames != null) {
			List<String> tempAttributeNames = new ArrayList<String>();
			for (String attributeName : attributeNames) {
				tempAttributeNames.add(attributeName.toUpperCase());
			}
			attributeNames.clear();
			attributeNames.addAll(tempAttributeNames);
		}
		Map<String, String> attrMap = new HashMap<String, String>();
		if (element != null) {
			List<?> objects = element.attributes();
			for (Object o : objects) {
				if (o instanceof Attribute) {
					Attribute attr = (Attribute) o;
					if (attributeNames == null || attributeNames.isEmpty()) {
						attrMap.put(attr.getName(), attr.getValue());
					} else if (attributeNames.contains(isIgnoreCase ? attr.getName().toUpperCase() : attr.getName())) {
						attrMap.put(attr.getName(), attr.getValue());
					}
				}
			}
		}
		return attrMap;
	}

	/**
	 * 获取子节点属性
	 * 
	 * @param element
	 * @param childElementNames
	 * @param attributeNames
	 * @return
	 */
	public static List<Map<String, String>> getChildrenAttributes(Element element, List<String> childElementNames,
			List<String> attributeNames, boolean isIgnoreCase4AttributeName) {
		List<Map<String, String>> rt = new ArrayList<Map<String, String>>();

		if (element == null) {
			return rt;
		}
		List<?> elements = element.elements();
		if (elements == null) {
			return rt;
		}

		for (Object o : elements) {
			if (o instanceof Element) {
				Element el = (Element) o;
				if (childElementNames == null || childElementNames.isEmpty()) {
					rt.add(getAttributes(el, attributeNames, isIgnoreCase4AttributeName));
				} else if (childElementNames.contains(el.getName())) {
					rt.add(getAttributes(el, attributeNames, isIgnoreCase4AttributeName));
				}
			}
		}
		return rt;
	}

	/**
	 * 将xml文件格式化成XSLT描述的格式
	 * 
	 * @param sourceText
	 * @param styleSheetText
	 * @return
	 * @throws DocumentException
	 * @throws TransformerException
	 */
	public static Document xsltTransform(String sourceText, String styleSheetText)
			throws DocumentException, TransformerException {
		return xsltTransform(parseDocument(sourceText), parseDocument(styleSheetText));
	}

	/**
	 * 将xml文件格式化成XSLT描述的格式
	 * 
	 * @param sourceDocument
	 * @param styleSheetDocument
	 * @return
	 * @throws TransformerException
	 */
	public static Document xsltTransform(Document sourceDocument, Document styleSheetDocument)
			throws TransformerException {
		// 资源
		DocumentSource sourceDs = new DocumentSource(sourceDocument);
		DocumentSource styleSheetDs = new DocumentSource(styleSheetDocument);
		// 结果
		DocumentResult result = new DocumentResult();

		// 创建转换器
		Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSheetDs);
		transformer.transform(sourceDs, result);

		return result.getDocument();
	}

}