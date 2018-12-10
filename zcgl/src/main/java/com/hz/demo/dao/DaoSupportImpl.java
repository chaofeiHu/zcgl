package com.hz.demo.dao;

import java.util.List;
import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("daoSupportImpl")
public class DaoSupportImpl implements IDaoSupport {

	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public Object insert(String str, Object object) throws Exception {
		return sqlSessionTemplate.insert(str, object);
	}

	@Override
	public void batchInsert(String str, List<Object> objects) throws Exception {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		// 批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		try {
			if (objects != null) {
				for (int i = 0, size = objects.size(); i < size; i++) {
					sqlSession.insert(str, objects.get(i));
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Object update(String str, Object object) throws Exception {
		return sqlSessionTemplate.update(str, object);
	}

	@Override
	public void batchUpdate(String str, List<Object> objects) throws Exception {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		// 批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		try {
			if (objects != null) {
				for (int i = 0, size = objects.size(); i < size; i++) {
					sqlSession.update(str, objects.get(i));
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		} finally {
			sqlSession.close();
		}
	}
	@Override
	public Object delete(String str, Object object) throws Exception {
		return sqlSessionTemplate.delete(str,object);
	}

	@Override
	public void batchDelete(String str, List<Object> objects) throws Exception {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		// 批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		try {
			if (objects != null) {
				for (int i = 0, size = objects.size(); i < size; i++) {
					sqlSession.delete(str, objects.get(i));
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Object findForObject(String str, Object object) throws Exception {
		return sqlSessionTemplate.selectOne(str, object);
	}

	@Override
	public Object findForList(String str, Object object) throws Exception {
		return sqlSessionTemplate.selectList(str, object);
	}

	@Override
	public Object findForMap(String str, Object object, String key, String value) throws Exception {		
		return sqlSessionTemplate.selectMap(str, object, key);
	}


}
