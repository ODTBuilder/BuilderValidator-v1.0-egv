package com.git.opengds.generalization.persistance;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.git.opengds.common.DataSourceFactory;
import com.git.opengds.user.domain.UserVO;

@Repository("generalizationReportDAO")
public class GeneralizationReportDAOImpl extends DataSourceFactory implements GeneralizationReportDAO {


	private static final String namespace = "com.git.GeneralizationResult";

	@Override
	public void insertGenResult(UserVO userVO, HashMap<String, Object> insertQuery) {
		super.getSqlSession(userVO.getId());
		insert(namespace + ".insertGenResult", insertQuery);
	}
}
