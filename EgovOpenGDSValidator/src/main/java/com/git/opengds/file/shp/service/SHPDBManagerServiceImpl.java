package com.git.opengds.file.shp.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.git.gdsbuilder.type.geoserver.layer.GeoLayerInfo;
import com.git.gdsbuilder.type.shp.collection.DTSHPLayerCollection;
import com.git.gdsbuilder.type.shp.layer.DTSHPLayer;
import com.git.gdsbuilder.type.shp.layer.DTSHPLayerList;
import com.git.opengds.file.shp.dbManager.SHPDBQueryManager;
import com.git.opengds.file.shp.persistence.SHPLayerCollectionDAO;
import com.git.opengds.user.domain.UserVO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("shpDBManagerService")
//@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*.xml" })
public class SHPDBManagerServiceImpl extends EgovAbstractServiceImpl implements SHPDBManagerService {

	@Resource(name="shpLayerCollectionDAO")
	private SHPLayerCollectionDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	public GeoLayerInfo insertSHPLayerCollection(UserVO userVO, DTSHPLayerCollection dtCollection,
			GeoLayerInfo layerInfo) throws Exception {

		try {
			SHPDBQueryManager dbManager = new SHPDBQueryManager();
			String collectionName = dtCollection.getCollectionName();
			String type = layerInfo.getFileType();

			// insert layerCollection tb
			HashMap<String, Object> insertCollectionQuery = dbManager.getInsertSHPLayerCollectionQuery(collectionName);
			int cIdx = dao.insertSHPLayerCollection(userVO, insertCollectionQuery);

			String src = layerInfo.getOriginSrc();
			DTSHPLayerList layerList = dtCollection.getShpLayerList();
			for (int i = 0; i < layerList.size(); i++) {
				DTSHPLayer shpLayer = layerList.get(i);
				// create layer tb
				HashMap<String, Object> createLayerQuery = dbManager.getSHPLayerTbCreateQuery(type, collectionName,
						shpLayer, src);
				dao.createSHPLayerTb(userVO, (HashMap<String, Object>) createLayerQuery.get("createQueryMap"));

				// insert layer tb
				List<String> attriKeyList = (List<String>) createLayerQuery.get("attriKeyList");
				List<HashMap<String, Object>> insertLayerQueryList = dbManager.getSHPLayerInsertQuery(type,
						collectionName, attriKeyList, shpLayer, src);

				for (int j = 0; j < insertLayerQueryList.size(); j++) {
					HashMap<String, Object> insertLayerQuery = insertLayerQueryList.get(j);
					dao.insertSHPLayer(userVO, insertLayerQuery);
				}

				// inert layer meta tb
				HashMap<String, Object> insertLayerMeteQuery = dbManager.getSHPLayerMetaInertQuery(type, collectionName,
						shpLayer, cIdx);
				dao.insertSHPLayerMetadata(userVO, insertLayerMeteQuery);

				// geoLayerInfo
				String layerName = shpLayer.getLayerName();
				layerInfo.putLayerName(layerName);
				String layerType = shpLayer.getLayerType();
				layerInfo.putLayerType(layerName, layerType);
				layerInfo.putLayerColumns(layerName, attriKeyList);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		if (layerInfo != null) {
			// txManager.commit(status);
			layerInfo.setDbInsertFlag(true);
		}
		return layerInfo;
	}
}
