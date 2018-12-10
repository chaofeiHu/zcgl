package com.hz.demo.core;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class JSONUtil {
        public static Map parseXmlToMap(String xml) {
            //  Map retMap = new HashMap();
            SortedMap<String, String> retMap = new TreeMap<String, String>();
            try {
                StringReader read = new StringReader(xml);
                // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
                InputSource source = new InputSource(read);
                // 创建一个新的SAXBuilder
                SAXBuilder sb = new SAXBuilder();
                // 通过输入源构造一个Document
                Document doc =  sb.build(source);
                Element root = (Element) doc.getRootElement();// 指向根节点
                List<Element> es = root.getChildren();
                if (es != null && es.size() != 0) {
                    for (Element element : es) {
                        retMap.put(element.getName(), element.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return retMap;
        }
	}
