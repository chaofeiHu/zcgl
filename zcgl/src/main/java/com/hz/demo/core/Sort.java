package com.hz.demo.core;


import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Sort {

	private String sortName;
	private String sortType;
	private String attributeName;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Sort() {
	}

	public Sort(HttpServletRequest request) {
		request.setAttribute("sort", this);
	}

	public Sort(HttpServletRequest request, String attributeName) {
		request.setAttribute(attributeName, this);
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	/**
	 * 返回指定类型的排序结果
	 * 
	 * @param list
	 * @param property
	 * @param sortType
	 * @param start
	 * @param size
	 * @return
	 */
	public static List sort(List list, String property, String sortType,
			int start, int size) {
		if (list == null || list.size() == 0)
			return list;
		if (StringUtils.equalsIgnoreCase(sortType,"asc"))
			return sort(list, property, start, size);
		else
			return reversedSort(list, property, start, size);
	}

	/**
	 * 返回指定类型的排序结果
	 * 
	 * @param list
	 * @param property
	 * @param sortType
	 * @return
	 */
	public static List sort(List list, String property, String sortType) {
		if (list == null || list.size() == 0)
			return list;
		if (StringUtils.equalsIgnoreCase(sortType,"asc"))
			return sort(list, property, 0, Integer.MAX_VALUE);
		else
			return reversedSort(list, property, 0, Integer.MAX_VALUE);
	}

	/**
	 * 正序排序方法
	 * 
	 * @param list
	 * @param property
	 * @return
	 */
	public static List sort(List list, String property) {
		Comparator mycmp = ComparableComparator.getInstance();
		mycmp = ComparatorUtils.nullLowComparator(mycmp);
		Comparator cmp = new BeanComparator(property, mycmp);
		Collections.sort(list, cmp);

		return list;
	}

	/**
	 * 正序排序，返回指定长度
	 * 
	 * @param list
	 * @param property
	 * @param size
	 * @return
	 */
	public static List sort(List list, String property, int start, int size) {
		list = sort(list, property);
		return subList(list, start, size);
	}

	/**
	 * 逆序排序，返回指定长度
	 * 
	 * @param list
	 * @param property
	 * @param size
	 * @return
	 */
	public static List reversedSort(List list, String property, int start,
			int size) {
		list = reversedSort(list, property);
		return subList(list, start, size);
	}

	/**
	 * 返回指定长度列表
	 * 
	 * @param list
	 * @param size
	 * @return
	 */
	public static List subList(List list, int start, int size) {
		int length = start + size;
		if (length >= list.size())
			length = list.size();

		ArrayList sizeList = new ArrayList();
		for (int i = start; i < length; i++) {
			sizeList.add(list.get(i));
		}
		return sizeList;
	}

	/**
	 * 逆序排序方法
	 * 
	 * @param list
	 * @param property
	 * @return
	 */
	public static List reversedSort(List list, String property) {
		Comparator mycmp = ComparableComparator.getInstance();
		mycmp = ComparatorUtils.nullLowComparator(mycmp);
		mycmp = ComparatorUtils.reversedComparator(mycmp);
		Comparator cmp = new BeanComparator(property, mycmp);
		Collections.sort(list, cmp);

		return list;
	}

}
