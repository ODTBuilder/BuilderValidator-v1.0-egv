package com.git.gdsbuilder.edit.qa10;

import java.util.Map;

import com.git.gdsbuilder.type.qa10.feature.QA10Feature;
import com.git.gdsbuilder.type.qa10.feature.QA10FeatureList;

public class EditQA10Layer {

	String layerName;
	String orignName;
	String layerType;
	String originLayerType;
	QA10FeatureList createFeatureList;
	QA10FeatureList modifiedFeatureList;
	QA10FeatureList deletedFeatureList;

	Map<String, Object> geoServerLayer;

	boolean isCreated = false;
	boolean isModified = false;
	boolean isDeleted = false;

	public String getOriginLayerType() {
		return originLayerType;
	}

	public void setOriginLayerType(String originLayerType) {
		this.originLayerType = originLayerType;
	}

	public Map<String, Object> getGeoServerLayer() {
		return geoServerLayer;
	}

	public void setGeoServerLayer(Map<String, Object> geoServerLayer) {
		this.geoServerLayer = geoServerLayer;
	}

	public String getOrignName() {
		return orignName;
	}

	public void setOrignName(String orignName) {
		this.orignName = orignName;
	}

	public String getLayerType() {
		return layerType;
	}

	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public QA10FeatureList getCreateFeatureList() {
		return createFeatureList;
	}

	public void setCreateFeatureList(QA10FeatureList createFeatureList) {
		this.createFeatureList = createFeatureList;
	}

	public QA10FeatureList getModifiedFeatureList() {
		return modifiedFeatureList;
	}

	public void setModifiedFeatureList(QA10FeatureList modifiedFeatureList) {
		this.modifiedFeatureList = modifiedFeatureList;
	}

	public QA10FeatureList getDeletedFeatureList() {
		return deletedFeatureList;
	}

	public void setDeletedFeatureList(QA10FeatureList deletedFeatureList) {
		this.deletedFeatureList = deletedFeatureList;
	}

	public void addCreateFeature(QA10Feature qa20Feature) {
		this.createFeatureList.add(qa20Feature);
	}

	public void addAllCreateFeature(QA10FeatureList createFeatureList) {
		this.createFeatureList.addAll(createFeatureList);
	}

	public void addmodifiedFeature(QA10Feature qa20Feature) {
		this.modifiedFeatureList.add(qa20Feature);
	}

	public void addAllmodifiedFeature(QA10FeatureList modifiedFeatureList) {
		this.modifiedFeatureList.addAll(modifiedFeatureList);
	}

	public void addDeleteFeature(QA10Feature qa20Feature) {
		this.deletedFeatureList.add(qa20Feature);
	}

	public void addAllDeleteFeature(QA10FeatureList deletedFeatureList) {
		this.deletedFeatureList.addAll(deletedFeatureList);
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	public boolean isModified() {
		return isModified;
	}

	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
