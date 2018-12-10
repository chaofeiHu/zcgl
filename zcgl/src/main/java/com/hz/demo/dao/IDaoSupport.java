package com.hz.demo.dao;

import java.util.List;

public interface IDaoSupport {
	/**
	 * 添加对象
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Object insert(String str, Object object) throws Exception;

	/**
	 * 批量插入数据
	 * 
	 * @param str
	 * @param objects
	 * @throws Exception
	 */
	public void batchInsert(String str, List<Object> objects) throws Exception;

	/**
	 * 修改对象
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Object update(String str, Object object) throws Exception;

	/**
	 * 批量更新
	 * 
	 * @param str
	 * @param objects
	 * @throws Exception
	 */
	public void batchUpdate(String str, List<Object> objects) throws Exception;

	/**
	 * 删除对象
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Object delete(String str, Object object) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @param str
	 * @param objects
	 * @throws Exception
	 */
	public void batchDelete(String str, List<Object> objects) throws Exception;

	/**
	 * 查找对象
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Object findForObject(String str, Object object) throws Exception;

	/**
	 * 查找对象封装成List
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Object findForList(String str, Object object) throws Exception;

	/**
	 * 查找对象封装成Map
	 * 
	 * @param str
	 * @param object
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public Object findForMap(String str, Object object, String key, String value) throws Exception;

}
