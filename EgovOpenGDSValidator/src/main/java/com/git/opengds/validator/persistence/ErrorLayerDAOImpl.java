/*
 *    OpenGDS/Builder
 *    http://git.co.kr
 *
 *    (C) 2014-2017, GeoSpatial Information Technology(GIT)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package com.git.opengds.validator.persistence;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.git.opengds.common.DataSourceFactory;
import com.git.opengds.user.domain.UserVO;

@Repository("errorLayerDAO")
public class ErrorLayerDAOImpl extends DataSourceFactory implements ErrorLayerDAO {


	private static final String namespace = "com.git.ErrorLayerMapper";

	@Override
	public void createErrorLayerTb(UserVO userVO, HashMap<String, Object> createQuery) {
		super.getSqlSession(userVO.getId());
		update(namespace + ".createErrorLayerTb", createQuery);
	}

	@Override
	public void insertErrorFeature(UserVO userVO, HashMap<String, Object> insertQuery) {
		super.getSqlSession(userVO.getId());
		insert(namespace + ".insertErrorFeature", insertQuery);
	}

	@Override
	public List<HashMap<String, Object>> selectErrorFeatures(UserVO userVO, HashMap<String, Object> selectQuery) {
		super.getSqlSession(userVO.getId());
		return selectList(namespace + ".selectErrorFeatures", selectQuery);
	}

	@Override
	public List<HashMap<String, Object>> selectAllErrorFeatures(UserVO userVO, HashMap<String, Object> selectAllQuery) {
		super.getSqlSession(userVO.getId());
		return selectList(namespace + ".selectAllErrorFeatures", selectAllQuery);
	}

	@Override
	public void dropErrorLayerTb(UserVO userVO, HashMap<String, Object> dropQuery) {
		super.getSqlSession(userVO.getId());
		update(namespace + ".dropErrorLayerTb", dropQuery);
	}
}
