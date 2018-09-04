package com.git.opengds.generalization.persistance;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.git.opengds.common.DataSourceFactory;
import com.git.opengds.user.domain.UserVO;

@Repository("generalizationLayerDAO")
public class GeneralizationLayerDAOImpl extends DataSourceFactory implements GeneralizationLayerDAO {


	private static final String namespace = "com.git.GenLayerMapper";

	@Override
	public void createGenLayerTb(UserVO userVO, HashMap<String, Object> createTbQuery) {
		super.getSqlSession(userVO.getId());
		update(namespace + ".createGenLayerTb", createTbQuery);
	}

	@Override
	public void insertGenFeature(UserVO userVO, HashMap<String, Object> insertQuery) {
		super.getSqlSession(userVO.getId());
		insert(namespace + ".insertGenFeature", insertQuery);
	}

}
