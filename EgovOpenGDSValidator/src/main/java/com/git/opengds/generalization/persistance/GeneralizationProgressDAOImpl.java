package com.git.opengds.generalization.persistance;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.git.opengds.common.DataSourceFactory;
import com.git.opengds.user.domain.UserVO;

@Repository("generalizationProgressDAO")
public class GeneralizationProgressDAOImpl extends DataSourceFactory implements GeneralizationProgressDAO {

	private static final String genNamespace = "com.git.GeneralizationProgressMapper";

	@Override
	public Long selectGenLayerTablesCount(UserVO userVO, HashMap<String, Object> selectTbCountQuery) {
		super.getSqlSession(userVO.getId());
		HashMap<String, Object> countMap = selectOne(genNamespace + ".selectGenTbNamesCount",
				selectTbCountQuery);
		return (Long) countMap.get("count");
	}

	@Override
	public Integer insertRequestState(UserVO userVO, HashMap<String, Object> insertQuery) {
		super.getSqlSession(userVO.getId());
		insert(genNamespace + ".insertGenRequestState", insertQuery);
		return (Integer) insertQuery.get("p_idx");
	}

	@Override
	public void updateProgressingState(UserVO userVO, HashMap<String, Object> updateQuery) {
		super.getSqlSession(userVO.getId());
		update(genNamespace + ".updateGenProgressingState", updateQuery);
	}

	@Override
	public void insertGenResponseState(UserVO userVO, HashMap<String, Object> insertQuery) {
		super.getSqlSession(userVO.getId());
		update(genNamespace + ".updateGenResponseState", insertQuery);
	}
}
