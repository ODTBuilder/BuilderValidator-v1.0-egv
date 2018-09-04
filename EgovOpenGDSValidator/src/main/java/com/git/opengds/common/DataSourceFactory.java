package com.git.opengds.common;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

abstract public class DataSourceFactory extends EgovAbstractMapper {

	@Resource(name = "userSqlSession")
	SqlSessionFactory userSqlSession;
		
	@Resource(name = "adminSqlSession")
	SqlSessionFactory adminSession;

/*	@Resource(name = "admin2SqlSession")
	SqlSessionFactory admin2Session;

	@Resource(name = "admin3SqlSession")
	SqlSessionFactory admin3Session;
*/
	public void getSqlSession(String id) {
		switch (id) {
		case "admin":
			super.setSqlSessionFactory(adminSession);
			break;
		/*case "admin2":
			super.setSqlSessionFactory(admin2Session);
			break;
		case "admin3":
			super.setSqlSessionFactory(admin3Session);
			break;*/
		case "user":
			super.setSqlSessionFactory(userSqlSession);
			break;
		}
	}

	/*
	 * @Autowired
	 * 
	 * @Inject
	 * 
	 * @Qualifier("adminSqlSession") private SqlSession adminSqlSession;
	 * 
	 * @Autowired
	 * 
	 * @Inject
	 * 
	 * @Qualifier("admin2SqlSession") private SqlSession admin2SqlSession;
	 * 
	 * @Autowired
	 * 
	 * @Inject
	 * 
	 * @Qualifier("admin3SqlSession") private SqlSession admin3SqlSession;
	 * 
	 * public SqlSession getSqlSession(String id) { switch (id) { case "admin":
	 * return adminSqlSession; case "admin2": return admin2SqlSession; case
	 * "admin3": return admin3SqlSession; default: return null; } }
	 */
}
