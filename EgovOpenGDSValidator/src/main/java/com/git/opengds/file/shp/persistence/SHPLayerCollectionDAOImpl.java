package com.git.opengds.file.shp.persistence;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.git.opengds.common.DataSourceFactory;
import com.git.opengds.user.domain.UserVO;

@Repository("shpLayerCollectionDAO")
public class SHPLayerCollectionDAOImpl extends DataSourceFactory implements SHPLayerCollectionDAO {

	private static final String namespace = "com.git.SHPLayerCollectionMapper";

	@Override
	public int insertSHPLayerCollection(UserVO userVO, HashMap<String, Object> insertCollectionQuery) {
		super.getSqlSession(userVO.getId());
		insert(namespace + ".insertSHPLayerCollection", insertCollectionQuery);
		return (Integer) insertCollectionQuery.get("c_idx");
	}

	@Override
	public int createSHPLayerTb(UserVO userVO, HashMap<String, Object> createLayerQuery) {
		super.getSqlSession(userVO.getId());
		int suc = update(namespace + ".createSHPLayerTb", createLayerQuery);
		return suc;
	}

	@Override
	public int insertSHPLayer(UserVO userVO, HashMap<String, Object> insertLayerQuery) {
		super.getSqlSession(userVO.getId());
		return insert(namespace + ".insertSHPLayer", insertLayerQuery);
	}

	@Override
	public int insertSHPLayerMetadata(UserVO userVO, HashMap<String, Object> insertLayerMeteQuery) {
		super.getSqlSession(userVO.getId());
		return insert(namespace + ".insertSHPLayerMetadata", insertLayerMeteQuery);
	}

	@Override
	public int selectSHPFeatureIdx(UserVO userVO, HashMap<String, Object> selectIdxquery) {
		super.getSqlSession(userVO.getId());
		HashMap<String, Object> idxMap = selectOne(namespace + ".selectSHPFeatureIdx", selectIdxquery);
		int idx = (Integer) idxMap.get("f_idx");
		return idx;
	}

	@Override
	public int deleteSHPFeature(UserVO userVO, HashMap<String, Object> deleteFeature) {
		super.getSqlSession(userVO.getId());
		return delete(namespace + ".deleteSHPFeature", deleteFeature);
	}

	@Override
	public Integer selectSHPLayerCollectionIdx(UserVO userVO, HashMap<String, Object> selectLayerCollectionIdxQuery) {
		super.getSqlSession(userVO.getId());
		HashMap<String, Object> idxMap = selectOne(namespace + ".selectSHPLayerCollectionIdx",
				selectLayerCollectionIdxQuery);
		if(idxMap == null) {
			return null; 
		} else {
			return (Integer) idxMap.get("c_idx");
		}
	}

	@Override
	public Integer selectSHPLayerMetadataIdx(UserVO userVO, HashMap<String, Object> metadataIdxQuery) {
		super.getSqlSession(userVO.getId());
		HashMap<String, Object> idxMap = selectOne(namespace + ".selectSHPLayerMetadataIdx",
				metadataIdxQuery);
		return (Integer) idxMap.get("lm_idx");
	}

	@Override
	public int deleteSHPLayerMetadata(UserVO userVO, HashMap<String, Object> deleteLayerMetaQuery) {
		super.getSqlSession(userVO.getId());
		return delete(namespace + ".deleteSHPLayerMetedata", deleteLayerMetaQuery);
	}

	@Override
	public int dropSHPLayer(UserVO userVO, HashMap<String, Object> dropQuery) {
		super.getSqlSession(userVO.getId());
		return update(namespace + ".dropSHPLayer", dropQuery);
	}

	@Override
	public int deleteSHPLayerCollection(UserVO userVO, HashMap<String, Object> deleteLayerCollectionQuery) {
		super.getSqlSession(userVO.getId());
		return delete(namespace + ".deleteSHPLayerCollection", deleteLayerCollectionQuery);
	}
}
