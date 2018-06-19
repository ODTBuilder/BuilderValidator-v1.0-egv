package com.git.opengds.validator.dbManager;

import java.util.HashMap;

public class ValidateProgressDBQueryManager {

	public HashMap<String, Object> getSelectNGIValidateProgressPid(String collectionName) {

		String tableName = "\"" + "qa20_layercollection_qa_progress" + "\"";
		String selectQuery = "select p_idx from " + tableName + " where collection_name = '" + collectionName + "'";
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		selectQueryMap.put("selectQuery", selectQuery);

		return selectQueryMap;
	}

	public HashMap<String, Object> getInsertNGIRequestState(int validateStart, String collectionName, String fileType,
			int cidx) {
		String tableName = "\"" + "qa20_layercollection_qa_progress" + "\"";
		String insertQueryStr = " insert into " + tableName
				+ "(collection_name, file_type, state, request_time , c_idx) values ('" + collectionName + "'," + "'"
				+ fileType + "', " + validateStart + ", " + "CURRENT_TIMESTAMP" + "," + cidx + ")";
		HashMap<String, Object> insertQueryMap = new HashMap<String, Object>();
		insertQueryMap.put("insertQuery", insertQueryStr);
		return insertQueryMap;
	}

	public HashMap<String, Object> getUpdateNGIProgressingState(int pIdx, int validateStart) {
		String tableName = "\"" + "qa20_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set state = " + validateStart + " where p_idx = " + pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getInsertNGIErrorTableName(int pIdx, String errTableName) {
		String tableName = "\"" + "qa20_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set err_layer_name = '" + errTableName + "' where p_idx = "
				+ pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getInsertNGIResponseState(int pIdx) {
		String tableName = "\"" + "qa20_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set response_time = " + "CURRENT_TIMESTAMP"
				+ " where p_idx = " + pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getSelectDXFValidateProgressPIdx(String collectionName) {
		String tableName = "\"" + "qa10_layercollection_qa_progress" + "\"";
		String selectQuery = "select p_idx from " + tableName + " where collection_name = '" + collectionName + "'";
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		selectQueryMap.put("selectQuery", selectQuery);
		return selectQueryMap;
	}

	public HashMap<String, Object> getSelectDXFValidateProgressIdx(int cIdx) {

		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		String tableName = "\"qa10_layercollection_qa_progress\"";
		String selectQuertStr = "select p_idx from " + tableName + "where c_idx = " + cIdx;
		selectQueryMap.put("selectQuery", selectQuertStr);
		return selectQueryMap;
	}

	public HashMap<String, Object> getInsertDXFRequestState(int validateStart, String collectionName, String fileType,
			int cidx) {
		String tableName = "\"" + "qa10_layercollection_qa_progress" + "\"";
		String insertQueryStr = " insert into " + tableName
				+ "(collection_name, file_type, state, request_time , c_idx) values ('" + collectionName + "'," + "'"
				+ fileType + "', " + validateStart + ", " + "CURRENT_TIMESTAMP" + ", " + cidx + ")";
		HashMap<String, Object> insertQueryMap = new HashMap<String, Object>();
		insertQueryMap.put("insertQuery", insertQueryStr);
		return insertQueryMap;
	}

	public HashMap<String, Object> getUpdateDXFProgressingState(int pIdx, int validateFail) {
		String tableName = "\"" + "qa10_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set state = " + validateFail + " where p_idx = " + pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getInsertDXFErrorTableName(int pIdx, String errTableName) {
		String tableName = "\"" + "qa10_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set err_layer_name = '" + errTableName + "' where p_idx = "
				+ pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getInsertDXFResponseState(int pIdx) {
		String tableName = "\"" + "qa10_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set response_time = " + "CURRENT_TIMESTAMP"
				+ " where p_idx = " + pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getSelectAllDXFValidateProgress() {
		String tableName = "\"" + "qa10_layercollection_qa_progress" + "\"";
		String selectQuery = "select * from " + tableName + " order by request_time DESC";
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		selectQueryMap.put("selectAllQuery", selectQuery);
		return selectQueryMap;
	}

	public HashMap<String, Object> getSelectAllNGIValidateProgress() {
		String tableName = "\"" + "qa20_layercollection_qa_progress" + "\"";
		String selectQuery = "select * from " + tableName + " order by request_time DESC";
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		selectQueryMap.put("selectAllQuery", selectQuery);
		return selectQueryMap;
	}

	public HashMap<String, Object> getUpdateSHPProgressingState(int pIdx, int validateStart) {
		String tableName = "\"" + "shp_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set state = " + validateStart + " where p_idx = " + pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getInsertSHPErrorTableName(int pIdx, String errTableName) {
		String tableName = "\"" + "shp_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set err_layer_name = '" + errTableName + "' where p_idx = "
				+ pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public HashMap<String, Object> getInsertSHPResponseState(int pIdx) {
		String tableName = "\"" + "shp_layercollection_qa_progress" + "\"";
		String updateQueryStr = "update " + tableName + " set response_time = " + "CURRENT_TIMESTAMP"
				+ " where p_idx = " + pIdx;
		HashMap<String, Object> updateQueryQueryMap = new HashMap<String, Object>();
		updateQueryQueryMap.put("updateQuery", updateQueryStr);
		return updateQueryQueryMap;
	}

	public Object getSelectAllSHPValidateProgress() {
		String tableName = "\"" + "shp_layercollection_qa_progress" + "\"";
		String selectQuery = "select * from " + tableName + " order by request_time DESC";
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		selectQueryMap.put("selectAllQuery", selectQuery);
		return selectQueryMap;
	}

	public HashMap<String, Object> getSelectNGIValidateProgressIdx(int cIdx) {
		HashMap<String, Object> selectQueryMap = new HashMap<String, Object>();
		String tableName = "\"qa20_layercollection_qa_progress\"";
		String selectQuertStr = "select p_idx from " + tableName + "where c_idx = " + cIdx;
		selectQueryMap.put("selectQuery", selectQuertStr);
		return selectQueryMap;
	}

}
