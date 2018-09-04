package com.git.opengds.validator.persistence;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.git.opengds.common.DataSourceFactory;
import com.git.opengds.user.domain.UserVO;

@Repository("validateProgressDAO")
public class ValidateProgressDAOImpl extends DataSourceFactory implements ValidateProgressDAO {

	private static final String shpNamespace = "com.git.SHPLayerCollectionProgressMapper";

	@Override
	public Integer insertSHPRequestState(UserVO userVO, HashMap<String, Object> insertQuery) {
		super.getSqlSession(userVO.getId());
		insert(shpNamespace + ".insertSHPRequestState", insertQuery);
		return (Integer) insertQuery.get("p_idx");
	}

	@Override
	public Integer selectSHPValidateProgressPid(UserVO userVO, HashMap<String, Object> selectSHPValidateProgressPid) {
		super.getSqlSession(userVO.getId());
		HashMap<String, Object> idxMap = selectOne(shpNamespace + ".selectSHPValidateProgressPid",
				selectSHPValidateProgressPid);
		if (idxMap != null) {
			return (Integer) idxMap.get("p_idx");
		} else {
			return null;
		}
	}

	@Override
	public void updateSHPProgressingState(UserVO userVO, HashMap<String, Object> insertProgressingState) {
		super.getSqlSession(userVO.getId());
		update(shpNamespace + ".updateSHPProgressingState", insertProgressingState);
	}

	@Override
	public void updateSHPValidateSuccessState(UserVO userVO, HashMap<String, Object> insertProgressingState) {
		super.getSqlSession(userVO.getId());
		update(shpNamespace + ".updateSHPValidateSuccessState", insertProgressingState);
	}

	@Override
	public void updateSHPValidateFailState(UserVO userVO, HashMap<String, Object> insertProgressingState) {
		super.getSqlSession(userVO.getId());
		update(shpNamespace + ".updateSHPValidateFailState", insertProgressingState);
	}

	@Override
	public void updateSHPValidateErrLayerSuccess(UserVO userVO, HashMap<String, Object> updateProgressingState) {
		super.getSqlSession(userVO.getId());
		update(shpNamespace + ".updateSHPValidateErrLayerSuccess", updateProgressingState);
	}

	@Override
	public void updateSHPValidateErrLayerFail(UserVO userVO, HashMap<String, Object> updateProgressingState) {
		super.getSqlSession(userVO.getId());
		update(shpNamespace + ".updateSHPValidateErrLayerFail", updateProgressingState);
	}

	@Override
	public void insertSHPResponseState(UserVO userVO, HashMap<String, Object> insertQuery) {
		super.getSqlSession(userVO.getId());
		insert(shpNamespace + ".updateSHPResponseState", insertQuery);
	}

	@Override
	public void insertSHPErrorTableName(UserVO userVO, HashMap<String, Object> insertErrorTableName) {
		super.getSqlSession(userVO.getId());
		insert(shpNamespace + ".updateSHPErrorTableName", insertErrorTableName);
	}

	@Override
	public List<HashMap<String, Object>> selectAllSHPValidateProgress(UserVO userVO,
			Object selectAllSHPValidateProgress) {
		super.getSqlSession(userVO.getId());
		return selectList(shpNamespace + ".selectAllSHPValidateProgress", selectAllSHPValidateProgress);
	}

	@Override
	public Long selectErrorLayerTbNamesCount(UserVO userVO, String fileType, HashMap<String, Object> selectIdxQuery) {
		super.getSqlSession(userVO.getId());
		HashMap<String, Object> countMap = null;
		if (fileType.equals("shp")) {
			countMap = selectOne(shpNamespace + ".selectSHPErrorLayerTbNamesCount", selectIdxQuery);
		}
		return (Long) countMap.get("count");
	}
}
