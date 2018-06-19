package com.git.opengds.file.dxf.dbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.git.gdsbuilder.type.dxf.feature.DTDXFFeature;
import com.git.gdsbuilder.type.dxf.feature.DTDXFFeatureList;
import com.git.gdsbuilder.type.dxf.layer.DTDXFLayer;

public class DXFDBQueryManager {

	public HashMap<String, Object> getInsertDXFLayerCollection(String collectionName) {

		String insertQuery = "insert into dxf_layercollection(collection_name) values('" + collectionName + "')";
		HashMap<String, Object> insertQueryMap = new HashMap<String, Object>();
		insertQueryMap.put("insertQuery", insertQuery);
		insertQueryMap.put("c_idx", 0);
		return insertQueryMap;
	}

	public HashMap<String, Object> getDXFLayerTbCreateQuery(String type, String collectionName, DTDXFLayer dxfLayer,
			String src) {

		String layerType = dxfLayer.getLayerType();
		String originLayerType = dxfLayer.getOriginLayerType();
		String layerId = dxfLayer.getLayerID();
		String tableName = "\"geo" + "_" + type + "_" + collectionName + "_" + layerId + "\"";
		String defaultCreateQuery = "create table " + tableName + "("
				+ "f_idx serial primary key, feature_id varchar(100), geom geometry(" + layerType + "," + src
				+ "), feature_type varchar(50)";

		if (originLayerType.equals("TEXT")) {
			defaultCreateQuery += ", text_value varchar(100), height double precision, rotate double precision";
		}
		if (originLayerType.equals("LWPOLYLINE") || originLayerType.equals("POLYLINE")) {
			defaultCreateQuery += ", elevation double precision, flag int";
		}
		if (originLayerType.equals("INSERT")) {
			defaultCreateQuery += ", rotate double precision";
		}
		defaultCreateQuery += ")";
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("createQuery", defaultCreateQuery);
		return query;
	}

	public List<HashMap<String, Object>> getDXFLayerTbInsertQuery(String type, String collectionName,
			DTDXFLayer dxfLayer, String src) {

		String originLayerType = dxfLayer.getOriginLayerType();
		String layerId = dxfLayer.getLayerID();
		String tableName = "\"geo" + "_" + type + "_" + collectionName + "_" + layerId + "\"";

		List<HashMap<String, Object>> dbLayers = new ArrayList<HashMap<String, Object>>();
		DTDXFFeatureList features = dxfLayer.getQa10FeatureList();
		for (int i = 0; i < features.size(); i++) {
			DTDXFFeature feature = features.get(i);
			String defaultInsertColumns = "insert into " + tableName + "(feature_id, geom, feature_type ";
			String values = "values ('" + feature.getFeatureID() + "', " + "ST_GeomFromText('"
					+ feature.getGeom().toString() + "'," + src + "), '" + feature.getFeatureType() + "'";

			if (originLayerType.equals("TEXT")) {
				defaultInsertColumns += ", text_value, height, rotate";
				values += ", '" + feature.getTextValue() + "'" + ", '" + feature.getHeight() + "'" + ", '"
						+ feature.getRotate() + "'";
			}

			if (originLayerType.equals("LWPOLYLINE") || originLayerType.equals("POLYLINE")) {
				defaultInsertColumns += ", elevation, flag";
				values += "," + feature.getElevation() + ", " + feature.getFlag();
			}

			if (originLayerType.equals("INSERT")) {
				defaultInsertColumns += ", rotate";
				values += "," + feature.getRotate();
			}

			defaultInsertColumns += ")";
			values += ")";
			HashMap<String, Object> query = new HashMap<String, Object>();
			query.put("insertQuery", defaultInsertColumns + values);
			dbLayers.add(query);
		}
		return dbLayers;
	}

	public HashMap<String, Object> getInsertDXFLayerMeataDataQuery(String type, String collectionName, int cIdx,
			DTDXFLayer dxfLayer) {

		String layerId = dxfLayer.getLayerID();
		String layerTableName = "geo" + "_" + type + "_" + collectionName + "_" + layerId;
		String insertQueryColumn = "insert into " + "\"dxf_layer_metadata" + "\""
				+ "(layer_id, layer_t_name, c_idx, current_layer_id)";
		String insertQueryValue = " values('" + layerId + "', '" + layerTableName + "', " + cIdx + "," + "'" + layerId
				+ "')";

		HashMap<String, Object> insertQueryMap = new HashMap<String, Object>();
		insertQueryMap.put("insertQuery", insertQueryColumn + insertQueryValue);
		// insertQueryMap.put("lm_idx", 0);

		return insertQueryMap;
	}

	public List<String> getLayerColumns() {
		List<String> columns = new ArrayList<String>();
		columns.add("layer_id");
		columns.add("feature_id");
		columns.add("geom");
		columns.add("feature_type");
		return columns;
	}

	public HashMap<String, Object> getInertDXFFeatureQuery(String tableName, DTDXFFeature createFeature) {

		String src = "5186";
		String featureType = createFeature.getFeatureType();

		String defaultInsertColumns = "insert into \"" + tableName + "\"(feature_id, geom, feature_type ";
		String values = "values ('" + createFeature.getFeatureID() + "', " + "ST_GeomFromText('"
				+ createFeature.getGeom().toString() + "'," + src + "), '" + createFeature.getFeatureType() + "'";

		if (featureType.equals("TEXT")) {
			defaultInsertColumns += ", text_value, height, rotate";
			values += ", '" + createFeature.getTextValue() + "'" + ", '" + createFeature.getHeight() + "'" + ", '"
					+ createFeature.getRotate() + "'";
		}

		if (featureType.equals("LWPOLYLINE") || featureType.equals("POLYLINE")) {
			defaultInsertColumns += ", elevation, flag";
			values += "," + createFeature.getElevation() + ", " + createFeature.getFlag();
		}

		if (featureType.equals("INSERT")) {
			defaultInsertColumns += ", rotate";
			values += "," + createFeature.getRotate();
		}

		defaultInsertColumns += ")";
		values += ")";
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", defaultInsertColumns + values);

		return query;
	}

	public HashMap<String, Object> getSelectDXFFeatureIdxQuery(String tableName, String featureID) {

		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String querytStr = "select f_idx from \"" + tableName + "\" where feature_id = '" + featureID + "'";
		selectQuery.put("selectQuery", querytStr);

		return selectQuery;
	}

	public HashMap<String, Object> getDeleteDXFFeatureQuery(String tableName, int fIdx) {

		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String queryStr = "delete from \"" + tableName + "\" where f_idx = '" + fIdx + "'";
		deleteQuery.put("deleteQuery", queryStr);

		return deleteQuery;
	}

	public HashMap<String, Object> getSelectDXFLayerCollectionIdxQuery(String collectionName) {

		String tableName = "dxf_layercollection";
		String selectQuery = "select c_idx from " + tableName + " where collection_name = '" + collectionName + "'";
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		selectQueryMap.put("selectQuery", selectQuery);

		return selectQueryMap;
	}

	public HashMap<String, Object> getInsertTablesQuery(int cIdx, Map<String, Object> tables) {

		LinkedHashMap<String, Object> commons = (LinkedHashMap<String, Object>) tables.get("common");

		String tableName = "\"" + "dxf_layercollection_table_common" + "\"";
		String tableColumnQuery = "insert into " + tableName + "(";
		String tableValuesQuery = "values(";

		Iterator tableIt = commons.keySet().iterator();
		while (tableIt.hasNext()) {
			String code = (String) tableIt.next();
			Object value = commons.get(code);
			tableColumnQuery += "\"" + code + "\"" + ",";
			tableValuesQuery += "'" + value + "', ";
		}
		tableColumnQuery += "c_idx" + ")";
		tableValuesQuery += cIdx + ")";

		String returnQuery = tableColumnQuery + tableValuesQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);
		query.put("tc_idx", null);

		return query;
	}

	public List<HashMap<String, Object>> getInsertTablesLayersQuery(int tbIdx, Map<String, Object> tables) {

		List<HashMap<String, Object>> layerQuerys = new ArrayList<HashMap<String, Object>>();
		List<LinkedHashMap<String, Object>> layers = (List<LinkedHashMap<String, Object>>) tables.get("layers");

		String tableName = "\"" + "dxf_layercollection_table_layer" + "\"";
		for (int i = 0; i < layers.size(); i++) {
			LinkedHashMap<String, Object> layer = layers.get(i);
			String layerColumnQuery = "insert into " + tableName + "(";
			String layerValuesQuery = "values(";
			Iterator layerIt = layer.keySet().iterator();
			while (layerIt.hasNext()) {
				String code = (String) layerIt.next();
				Object value = layer.get(code);
				layerColumnQuery += "\"" + code + "\"" + ",";
				layerValuesQuery += "'" + value + "', ";
			}
			layerColumnQuery += "tc_idx" + ")";
			layerValuesQuery += tbIdx + ")";

			String returnQuery = layerColumnQuery + layerValuesQuery;
			HashMap<String, Object> query = new HashMap<String, Object>();
			query.put("insertQuery", returnQuery);

			layerQuerys.add(query);
		}
		return layerQuerys;
	}

	public HashMap<String, Object> getInsertTablesLayerQuery(int tbIdx, Map<String, Object> tables) {

		String tableName = "\"" + "dxf_layercollection_table_layer" + "\"";
		String layerColumnQuery = "insert into " + tableName + "(";
		String layerValuesQuery = "values(";
		Iterator layerIt = tables.keySet().iterator();
		while (layerIt.hasNext()) {
			String code = (String) layerIt.next();
			Object value = tables.get(code);
			layerColumnQuery += "\"" + code + "\"" + ",";
			layerValuesQuery += "'" + value + "', ";
		}
		layerColumnQuery += "tc_idx" + ")";
		layerValuesQuery += tbIdx + ")";

		String returnQuery = layerColumnQuery + layerValuesQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);

		return query;
	}

	public List<HashMap<String, Object>> getInsertBlocksCommonQuery(int cIdx,
			List<LinkedHashMap<String, Object>> blocks) {

		List<HashMap<String, Object>> blockQuerys = new ArrayList<HashMap<String, Object>>();

		String tableName = "\"" + "dxf_layercollection_block_common" + "\"";
		for (int i = 0; i < blocks.size(); i++) {
			String blockColumnQuery = "insert into " + tableName + "(";
			String blockValuesQuery = "values(";
			LinkedHashMap<String, Object> blockMap = blocks.get(i);
			// block
			LinkedHashMap<String, Object> block = (LinkedHashMap<String, Object>) blockMap.get("block");
			Iterator blockIt = block.keySet().iterator();
			while (blockIt.hasNext()) {
				String code = (String) blockIt.next();
				Object value = block.get(code);
				blockColumnQuery += "\"" + code + "\"" + ",";
				blockValuesQuery += "'" + value + "', ";
			}
			blockColumnQuery += "c_idx" + ")";
			blockValuesQuery += cIdx + ")";

			String returnQuery = blockColumnQuery + blockValuesQuery;
			HashMap<String, Object> query = new HashMap<String, Object>();
			query.put("insertQuery", returnQuery);
			query.put("bc_idx", null);

			blockQuerys.add(query);
		}
		return blockQuerys;
	}

	public HashMap<String, Object> getInsertBlockArcQuery(int bIdx, LinkedHashMap<String, Object> entity) {

		String tableName = "\"" + "dxf_layercollection_block_arc" + "\"";

		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";

		Iterator entityIt = entity.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = entity.get(code);
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx" + ")";
		insertValueQuery += bIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);
		return query;
	}

	public HashMap<String, Object> getInsertBlockCircleQuery(int bIdx, LinkedHashMap<String, Object> entity) {

		String tableName = "\"" + "dxf_layercollection_block_circle" + "\"";

		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";

		Iterator entityIt = entity.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = entity.get(code);
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx" + ")";
		insertValueQuery += bIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);
		return query;
	}

	public HashMap<String, Object> getInsertBlockTextQuery(int bIdx, LinkedHashMap<String, Object> entity) {

		String tableName = "\"" + "dxf_layercollection_block_text" + "\"";

		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";

		Iterator entityIt = entity.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = entity.get(code);
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx" + ")";
		insertValueQuery += bIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);
		return query;
	}

	public HashMap<String, Object> getInsertBlockPolylineQuery(int bIdx, LinkedHashMap<String, Object> entity) {

		String tableName = "\"" + "dxf_layercollection_block_polyline" + "\"";
		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";
		Iterator entityIt = entity.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = entity.get(code);
			if (code.equals("vertexs")) {
				continue;
			}
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx" + ")";
		insertValueQuery += bIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);
		query.put("bp_idx", 0);
		return query;
	}

	public HashMap<String, Object> getInsertBlockVertexQuery(int dcIdx, int dpIdx,
			LinkedHashMap<String, Object> vertexObj) {

		String tableName = "\"" + "dxf_layercollection_block_vertex" + "\"";
		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";
		Iterator entityIt = vertexObj.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = vertexObj.get(code);
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx, bp_idx" + ")";
		insertValueQuery += dcIdx + ", " + dpIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);

		return query;
	}

	public HashMap<String, Object> getInsertBlockVertexQuery(int dcIdx, LinkedHashMap<String, Object> vertexObj) {

		String tableName = "\"" + "dxf_layercollection_block_vertex" + "\"";
		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";
		Iterator entityIt = vertexObj.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = vertexObj.get(code);
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx)";
		insertValueQuery += dcIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);

		return query;
	}

	public HashMap<String, Object> getSelectLayerMetaDataIdxQuery(Integer cIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layer_metadata" + "\"";
		String selectQueryStr = "select lm_idx from " + tableName + " where c_idx = " + cIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectLayerTableNameQuery(Integer mIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layer_metadata" + "\"";
		String selectQueryStr = "select layer_t_name from " + tableName + " where lm_idx = " + mIdx;
		selectQuery.put("selectQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getDropDXFLayerQuery(String layerTbName) {
		HashMap<String, Object> dropQueryMap = new HashMap<String, Object>();
		String queryStr = "drop table " + "\"" + layerTbName + "\"";
		dropQueryMap.put("dropQuery", queryStr);
		return dropQueryMap;
	}

	public HashMap<String, Object> getDeleteDXFLayerMetaQuery(Integer cIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layer_metadata" + "\"";
		String deleteQueryStr = "delete from " + tableName + " where lm_idx = " + cIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteDXFLayerCollectionQuery(Integer cIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection" + "\"";
		String deleteQueryStr = "delete from " + tableName + " where c_idx = " + cIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getSelectTableCommonIdxQuery(Integer cIdx) {
		HashMap<String, Object> selectIdxQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_table_common";
		String selectQueryStr = "select tc_idx from " + tableName + " where c_idx = " + cIdx;
		selectIdxQuery.put("selectQuery", selectQueryStr);
		return selectIdxQuery;
	}

	public HashMap<String, Object> getDeleteTableLayersQuery(Integer tcIdx, String layerId) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_table_layer";
		String deleteQueryStr = "delete from " + tableName + " where tc_idx = " + tcIdx + " AND " + "\"" + 2 + "\""
				+ "= '" + layerId + "'";
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteTableLayersQuery(Integer tcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_table_layer";
		String deleteQueryStr = "delete from " + tableName + " where tc_idx = " + tcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteTablesQuery(Integer cIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_table_common";
		String deleteQueryStr = "delete from " + tableName + " where c_idx = " + cIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getSelectBlockCommonIdxQuery(Integer cIdx) {
		HashMap<String, Object> selectIdxQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_common";
		String selectQueryStr = "select bc_idx from " + tableName + " where c_idx = " + cIdx;
		selectIdxQuery.put("selectQuery", selectQueryStr);
		return selectIdxQuery;
	}

	public HashMap<String, Object> getSelectBlockCommonIdxQuery(Integer cIdx, String id) {
		HashMap<String, Object> selectIdxQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_common";
		String selectQueryStr = "select bc_idx from " + tableName + " where c_idx = " + cIdx + " AND " + "\"" + 2 + "\""
				+ "= '" + id + "'";
		selectIdxQuery.put("selectQuery", selectQueryStr);
		return selectIdxQuery;
	}

	public HashMap<String, Object> getDeleteBlockLineQuery(Integer bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_line";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteBlockLWPolylineQuery(Integer bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_lwpolyline";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteBlockArcQuery(Integer bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_arc";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteBlockCircleQuery(Integer bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_circle";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteBlockPolylineQuery(Integer bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_polyline";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteBlockTextQuery(Integer bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_text";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteBlockVertexQuery(Integer bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_vertex";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getDeleteBlocksQuery(int bcIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_common";
		String deleteQueryStr = "delete from " + tableName + " where bc_idx = " + bcIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getSelectQA10LayerMetaDataIdxQuery(Integer cIdx, String layerID) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layer_metadata" + "\"";
		String selectQueryStr = "select lm_idx from " + tableName + " where c_idx = " + cIdx + " and "
				+ "current_layer_id = '" + layerID + "'";
		selectQuery.put("selectQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getUpdateQA10LayerMeataLayerIDQuery(Integer lmIdx, String currentID) {
		HashMap<String, Object> updateQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layer_metadata" + "\"";
		String updateQueryStr = "update " + tableName + " set current_layer_id = '" + currentID + "'"
				+ "where lm_idx = " + lmIdx;
		updateQuery.put("updateQuery", updateQueryStr);
		return updateQuery;
	}

	public HashMap<String, Object> getSelectTableLayerIdxQuery(int tcIdx, String orignId) {

		String originIdNotType = orignId.substring(0, orignId.indexOf("_"));

		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_table_layer" + "\"";
		String selectQueryStr = "select tl_idx from " + tableName + " where tc_idx = " + tcIdx + " and " + "\"" + 2
				+ "\" = '" + originIdNotType + "'";
		selectQuery.put("selectQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getUpdateTableLayerIdQuery(int tlIdx, String currentId) {

		String currentIdNotType = currentId.substring(0, currentId.indexOf("_"));

		HashMap<String, Object> updateQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_table_layer" + "\"";
		String updateQueryStr = "update " + tableName + " set " + "\"" + 2 + "\" = '" + currentIdNotType + "'"
				+ "where tl_idx = " + tlIdx;
		updateQuery.put("updateQuery", updateQueryStr);
		return updateQuery;
	}

	public HashMap<String, Object> getSelectQA10LayerMetaDataQuery(int lmIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layer_metadata" + "\"";
		String selectQueryStr = "select * from " + tableName + " where lm_idx = " + lmIdx;
		selectQuery.put("selectQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectFeatureQuery(String layerTbName, String layerType) {
		String selectQueryStr = "select f_idx, feature_id, feature_type, ST_AsText(geom) as geom";
		if (layerType.equals("TEXT")) {
			selectQueryStr += ", text_value, height, rotate ";
		}
		if (layerType.equals("LWPOLYLINE") || layerType.equals("POLYLINE")) {
			selectQueryStr += ", elevation, flag ";
		}
		if (layerType.equals("INSERT")) {
			selectQueryStr += ", rotate ";
		}
		selectQueryStr += "from " + "\"" + layerTbName + "\"";
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		selectQueryMap.put("selectQuery", selectQueryStr);
		return selectQueryMap;
	}

	public HashMap<String, Object> getSelectTableCommonQuery(int cIdx) {
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		String selectQueryStr = "select * from dxf_layercollection_table_common where c_idx = " + cIdx;
		selectQueryMap.put("selectQuery", selectQueryStr);
		return selectQueryMap;
	}

	public HashMap<String, Object> getSelectTableLayerQuery(int tcIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_table_layer" + "\"";
		String selectQueryStr = "select \"0\", \"2\", \"70\", \"62\", \"6\" from " + tableName + " where tc_idx = "
				+ tcIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getDeleteDXFProgressQuery(int cIdx) {
		HashMap<String, Object> deleteQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_qa_progress";
		String deleteQueryStr = "delete from " + tableName + " where c_idx = " + cIdx;
		deleteQuery.put("deleteQuery", deleteQueryStr);
		return deleteQuery;
	}

	public HashMap<String, Object> getSelectBlockCommonQuery(Integer cIdx, String id) {
		HashMap<String, Object> selectIdxQuery = new HashMap<String, Object>();
		String tableName = "dxf_layercollection_block_common";
		String selectQueryStr = "select * from " + tableName + " where c_idx = " + cIdx + " AND " + "\"2\"" + " = '"
				+ id + "'";
		selectIdxQuery.put("selectQuery", selectQueryStr);
		return selectIdxQuery;
	}

	public HashMap<String, Object> getSelectBlockArcQuery(int bcIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_arc" + "\"";
		String selectQueryStr = "select \"0\", \"8\", \"330\", \"10\", \"20\", \"30\", \"40\", \"50\", \"51\" from "
				+ tableName + " where bc_idx = " + bcIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectBlockCircleQuery(int bcIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_circle" + "\"";
		String selectQueryStr = "select \"0\", \"8\", \"330\", \"10\", \"20\", \"30\", \"40\" from " + tableName
				+ " where bc_idx = " + bcIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectBlockPolylineQuery(int bcIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_polyline" + "\"";
		String selectQueryStr = "select bp_idx, \"0\", \"8\", \"330\", \"66\", \"10\", \"20\", \"30\", \"70\" from "
				+ tableName + " where bc_idx = " + bcIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectBlockTextQuery(int bcIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_text" + "\"";
		String selectQueryStr = "select \"0\", \"8\", \"330\", \"10\", \"20\", \"30\", \"40\", \"1\", \"7\" from "
				+ tableName + " where bc_idx = " + bcIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectBlockPolylineVertexQuery(int bpIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_vertex" + "\"";
		String selectQueryStr = "select \"0\", \"8\", \"330\",\"10\", \"20\", \"30\" from " + tableName
				+ " where bp_idx = " + bpIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getInsertBlockLWPolylineQuery(int bIdx, LinkedHashMap<String, Object> entity) {

		String tableName = "\"" + "dxf_layercollection_block_lwpolyline" + "\"";
		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";
		Iterator entityIt = entity.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = entity.get(code);
			if (code.equals("vertexs")) {
				continue;
			}
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx" + ")";
		insertValueQuery += bIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);
		query.put("blp_idx", 0);
		return query;
	}

	public HashMap<String, Object> getInsertBlockLWVertexQuery(int dcIdx, int dlpIdx,
			LinkedHashMap<String, Object> vertexObj) {

		String tableName = "\"" + "dxf_layercollection_block_vertex" + "\"";
		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";
		Iterator entityIt = vertexObj.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = vertexObj.get(code);
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx, blp_idx" + ")";
		insertValueQuery += dcIdx + ", " + dlpIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);

		return query;
	}

	public HashMap<String, Object> getInsertBlockLineQuery(int bIdx, LinkedHashMap<String, Object> lineObj) {

		String tableName = "\"" + "dxf_layercollection_block_line" + "\"";
		String insertColumnQuery = "insert into " + tableName + "(";
		String insertValueQuery = "values(";
		Iterator entityIt = lineObj.keySet().iterator();
		while (entityIt.hasNext()) {
			String code = (String) entityIt.next();
			Object value = lineObj.get(code);
			insertColumnQuery += "\"" + code + "\", ";
			insertValueQuery += "'" + value + "', ";
		}
		insertColumnQuery += "bc_idx)";
		insertValueQuery += bIdx + ")";

		String returnQuery = insertColumnQuery + insertValueQuery;
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("insertQuery", returnQuery);

		return query;
	}

	public HashMap<String, Object> getSelectBlockLineQuery(int bcIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_line" + "\"";
		String selectQueryStr = "select \"0\", \"8\", \"330\", \"10\", \"20\", \"30\", \"11\", \"21\", \"31\" from "
				+ tableName + " where bc_idx = " + bcIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectBlockLWPolylineQuery(int bcIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_lwpolyline" + "\"";
		String selectQueryStr = "select blp_idx, \"0\", \"8\", \"330\", \"90\", \"70\", \"43\" from " + tableName
				+ " where bc_idx = " + bcIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

	public HashMap<String, Object> getSelectBlockLWPolylineVertexQuery(int blpIdx) {
		HashMap<String, Object> selectQuery = new HashMap<String, Object>();
		String tableName = "\"" + "dxf_layercollection_block_vertex" + "\"";
		String selectQueryStr = "select \"0\", \"8\", \"330\",\"10\", \"20\", \"30\" from " + tableName
				+ " where blp_idx = " + blpIdx;
		selectQuery.put("selectAllQuery", selectQueryStr);
		return selectQuery;
	}

}
