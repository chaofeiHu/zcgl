package com.hz.demo.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class PageData extends HashMap<Object, Object> implements Map<Object, Object> {

	private static final long serialVersionUID = 1L;

	Map<Object, Object> map = null;
	HttpServletRequest request;

	public PageData(HttpServletRequest request) {
		this.request = request;
		Map<String, String[]> properties = request.getParameterMap();
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		Iterator<Entry<String, String[]>> entries = properties.entrySet().iterator();
		Entry<String, String[]> entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		map = returnMap;
	}

	public PageData() {
		map = new HashMap<Object, Object>();
	}

	@Override
	public Object get(Object key) {
		Object obj = null;
		if (map.get(key) instanceof Object[]) {
			Object[] arr = (Object[]) map.get(key);
			obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
		} else {
			obj = map.get(key);
		}
		return obj;
	}

	public String getString(Object key) {
		return (String) get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<Object> keySet() {
		return map.keySet();
	}

	public void putAll(Map<?, ?> t) {
		map.putAll(t);
	}

	public int size() {
		return map.size();
	}

	public Collection<Object> values() {
		return map.values();
	}

	public Object clone() {
		PageData pageData = new PageData();
		for (Iterator<Object> keyIt = this.keySet().iterator(); keyIt.hasNext();) {
			Object key = keyIt.next();
			pageData.put(key, this.get(key));
		}
		return pageData;
	}

	/**
	 * 判断两个对象指定字段值是否一致
	 * 
	 * @param pd
	 * @param fields
	 * @return
	 */
	public boolean equals(PageData pd, List<String> fields) {
		boolean isEquals = true;
		if (pd != null && fields != null && !fields.isEmpty()) {
			for (String field : fields) {
				isEquals = isEquals && this.isEqual(this.getString(field), pd.getString(field));
			}
		}
		return isEquals;
	}

	/**
	 * 判断两个字符串是否相等
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	private boolean isEqual(String str1, String str2) {
		boolean isAllNull = (str1 == null && str2 == null);
		boolean isAllEmpty = (str1 != null && str1.trim().equals("") && str2 != null && str2.trim().equals(""));
		return isAllEmpty || isAllNull || (str1 != null && str2 != null && str2.trim().equals(str1.trim()));
	}
}