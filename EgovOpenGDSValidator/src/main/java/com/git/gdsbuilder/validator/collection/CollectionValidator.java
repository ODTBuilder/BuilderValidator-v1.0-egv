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

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    OpenGDS_2018
 *    Lesser General Public License for more details.
 */

package com.git.gdsbuilder.validator.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.json.simple.JSONObject;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.git.gdsbuilder.type.geoserver.collection.GeoLayerCollection;
import com.git.gdsbuilder.type.geoserver.layer.GeoLayer;
import com.git.gdsbuilder.type.geoserver.layer.GeoLayerList;
import com.git.gdsbuilder.type.validate.collection.close.ValidateCloseCollectionLayer;
import com.git.gdsbuilder.type.validate.error.ErrorLayer;
import com.git.gdsbuilder.type.validate.error.ErrorLayerList;
import com.git.gdsbuilder.type.validate.layer.ValidateLayerType;
import com.git.gdsbuilder.type.validate.layer.ValidateLayerTypeList;
import com.git.gdsbuilder.type.validate.option.AttributeFix;
import com.git.gdsbuilder.type.validate.option.ConBreak;
import com.git.gdsbuilder.type.validate.option.ConIntersected;
import com.git.gdsbuilder.type.validate.option.ConOverDegree;
import com.git.gdsbuilder.type.validate.option.EdgeMatchMiss;
import com.git.gdsbuilder.type.validate.option.EntityDuplicated;
import com.git.gdsbuilder.type.validate.option.EntityNone;
import com.git.gdsbuilder.type.validate.option.NodeMiss;
import com.git.gdsbuilder.type.validate.option.OutBoundary;
import com.git.gdsbuilder.type.validate.option.OverShoot;
import com.git.gdsbuilder.type.validate.option.PointDuplicated;
import com.git.gdsbuilder.type.validate.option.RefAttributeMiss;
import com.git.gdsbuilder.type.validate.option.SelfEntity;
import com.git.gdsbuilder.type.validate.option.SmallArea;
import com.git.gdsbuilder.type.validate.option.SmallLength;
import com.git.gdsbuilder.type.validate.option.UselessPoint;
import com.git.gdsbuilder.type.validate.option.ValidatorOption;
import com.git.gdsbuilder.type.validate.option.ZValueAmbiguous;
import com.git.gdsbuilder.validator.collection.opt.ValCollectionOption;
import com.git.gdsbuilder.validator.collection.rule.MapSystemRule;
import com.git.gdsbuilder.validator.collection.rule.MapSystemRule.MapSystemRuleType;
import com.git.gdsbuilder.validator.layer.LayerValidator;
import com.git.gdsbuilder.validator.layer.LayerValidatorImpl;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * ValidateLayerCollectionList를 검수하는 클래스
 * 
 * @author DY.Oh
 * @Date 2017. 4. 18. 오후 3:30:17
 */
public class CollectionValidator {

	/**
	 * 선형이 면형 객체 침범 (m) tolerance
	 */
	protected static double lineInvadedTolorence = 0.01;
	/**
	 * 면형이 면형 객체 침범 (m2) tolerance
	 */
	protected static double polygonInvadedTolorence = 0.01;
	/**
	 * 중심선이 경계면 초과 (m2)
	 */
	protected static double lineOverTolorence = 0.01;
	/**
	 * 지류계와 경지계 불일치 (%)
	 */
	protected static double areaRatioTolorence = 0.1;
	/**
	 * 공간분석 정밀도 설정 (m)
	 */
	protected static double spatialAccuracyTolorence = 0.01;

	/**
	 * 오류 레이어 목록
	 */
	ErrorLayerList errLayerList;
	/**
	 * 검수 영역 GeoLayerCollection 객체
	 */
	GeoLayerCollection collection;
	/**
	 * 인접 영역 GeoLayerCollection 목록
	 */
	List<GeoLayerCollection> nearCollections;
	/**
	 * 검수 항목 옵션 목록
	 */
	ValidateLayerTypeList types;
	/**
	 * 인접 검수 영역 Index Rule
	 */
	MapSystemRule mapSystemRule;
	/**
	 * 검수 진행 사항
	 */
	Map<String, Object> progress;
	/**
	 * 파일 타입
	 */
	String collectionType;

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionValidator.class);

	/**
	 * CollectionValidator 생성자
	 * 
	 * @param collection
	 *            검수 영역 GeoLayerCollection 객체
	 * @param nearCollections
	 *            인접 영역 GeoLayerCollection 목록
	 * @param types
	 *            검수 항목 옵션 목록
	 * @param mapSystemRule
	 *            인접 검수 영역 Index Rule
	 * @param fileType
	 *            검수 파일 타입
	 * @throws NoSuchAuthorityCodeException
	 * @throws SchemaException
	 * @throws FactoryException
	 * @throws TransformException
	 * @throws IOException
	 */
	public CollectionValidator(GeoLayerCollection collection, List<GeoLayerCollection> nearCollections,
			ValidateLayerTypeList types, MapSystemRule mapSystemRule, String fileType)
			throws NoSuchAuthorityCodeException, SchemaException, FactoryException, TransformException, IOException {
		this.collection = collection;
		this.nearCollections = nearCollections;
		this.types = types;
		this.mapSystemRule = mapSystemRule;
		this.collectionType = fileType;
		this.errLayerList = new ErrorLayerList();
		this.progress = new HashMap<String, Object>();
		collectionValidate();
	}

	/**
	 * 검수 진행 상태 반환
	 * 
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getProgress() {
		return this.progress;
	}

	/**
	 * 오류 레이어 목록 반환
	 * 
	 * @return ErrorLayerList
	 */
	public ErrorLayerList getErrLayerList() {
		return this.errLayerList;
	}

	/**
	 * 
	 */
	public void collectionValidate() {

		this.errLayerList = new ErrorLayerList();

		GeoLayerCollection collection = this.collection;
		ValidateLayerTypeList types = this.types;
		MapSystemRule mapSystemRule = this.mapSystemRule;

		try {
			ErrorLayer errorLayer = new ErrorLayer();
			errorLayer.setCollectionName(collection.getCollectionName());
			errorLayer.setCollectionType(this.collectionType);

			GeoLayer neatLineLayer = collection.getNeatLine();

			// neatLineMiss 검수
			if (neatLineLayer == null) {
				throw new Exception();
			}
			// layerMiss 검수
			layerMissValidate(types, collection, errorLayer);

			// geometric 검수
			geometricValidate(types, collection, errorLayer);

			// attribute 검수
			attributeValidate(types, collection, errorLayer);

			// 인접도엽 검수
			if (nearCollections.size() != 0) {
				closeCollectionValidate(types, mapSystemRule, collection, "", errorLayer);
			}
			errLayerList.add(errorLayer);
			progress.put(collection.getCollectionName(), 2);
		} catch (Exception e) {
			e.printStackTrace();
			progress.put(collection.getCollectionName(), 3);
		}
		System.out.println("검수완료");
	}

	@SuppressWarnings("unused")
	private void attributeValidate(ValidateLayerTypeList types, GeoLayerCollection layerCollection,
			ErrorLayer errorLayer) throws SchemaException {
		String collectionName = layerCollection.getCollectionName();

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		AttResult attResult = new AttResult();

		class Task implements Runnable {
			AttResult attResult;
			GeoLayer typeLayer;
			List<ValidatorOption> options;

			Task(AttResult attResult, List<ValidatorOption> options, GeoLayer typeLayer) {
				this.attResult = attResult;
				this.typeLayer = typeLayer;
				this.options = options;
			}

			@Override
			public void run() {
				ErrorLayer typeErrorLayer = null;
				LayerValidatorImpl layerValidator = new LayerValidatorImpl(typeLayer);
				for (int k = 0; k < options.size(); k++) {
					ValidatorOption option = options.get(k);
					if (option instanceof AttributeFix) {
						HashMap<String, Object> attributeNames = ((AttributeFix) option).getRelationType();
						String typeLayerName = typeLayer.getLayerName();
						JSONObject attrJson = (JSONObject) attributeNames.get(typeLayerName);
						try {
							typeErrorLayer = layerValidator.validateAttributeFix(attrJson);
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							attResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof ZValueAmbiguous) {
						HashMap<String, Object> hashMap = ((ZValueAmbiguous) option).getRelationType();
						String typeLayerName = typeLayer.getLayerName();
						JSONObject zValue = (JSONObject) hashMap.get(typeLayerName);
						try {
							typeErrorLayer = layerValidator.validateZValueAmbiguous(zValue);
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							attResult.mergeErrorLayer(typeErrorLayer);
						}
					}
				}
			}
		}
		List<Future<AttResult>> futures = new ArrayList<Future<AttResult>>();
		for (int j = 0; j < types.size(); j++) {
			ValidateLayerType type = types.get(j);
			GeoLayerList typeLayers = types.getTypeLayers(type.getTypeName(), layerCollection);
			List<ValidatorOption> options = type.getOptionList();
			if (options != null) {
				for (int a = 0; a < typeLayers.size(); a++) {
					GeoLayer typeLayer = typeLayers.get(a);
					if (typeLayer == null) {
						continue;
					}
					Runnable task = new Task(attResult, options, typeLayer);
					Future<AttResult> future = executorService.submit(task, attResult);

					futures.add(future);
				}

				for (Future<AttResult> future : futures) {
					try {
						attResult = future.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		executorService.shutdown();
		errorLayer.mergeErrorLayer(attResult.treadErrorLayer);
	}

	@SuppressWarnings("unused")
	private void geometricValidate(ValidateLayerTypeList types, GeoLayerCollection layerCollection,
			ErrorLayer errorLayer)
			throws SchemaException, NoSuchAuthorityCodeException, FactoryException, TransformException, IOException {

		GeoLayer neatLayer = layerCollection.getNeatLine();

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		GeometricResult geometricResult = new GeometricResult();

		class Task implements Runnable {
			GeometricResult geometricResult;
			GeoLayer typeLayer;
			List<ValidatorOption> options;

			Task(GeometricResult geometricResult, List<ValidatorOption> options, GeoLayer typeLayer) {
				this.geometricResult = geometricResult;
				this.typeLayer = typeLayer;
				this.options = options;
			}

			@Override
			public void run() {

				ErrorLayer typeErrorLayer = null;
				LayerValidatorImpl layerValidator = new LayerValidatorImpl(typeLayer);

				String layerFullName = typeLayer.getLayerName();

				System.out.println(layerFullName);

				int dash = layerFullName.indexOf("_");
				String layerType = layerFullName.substring(dash + 1);
				String upperLayerType = layerType.toUpperCase();
				LayerValidator layerValidator1 = new LayerValidatorImpl(typeLayer);

				for (int k = 0; k < options.size(); k++) {
					ValidatorOption option = options.get(k);
					if (option instanceof ConBreak) {
						try {
							typeErrorLayer = layerValidator.validateConBreakLayers(neatLayer, spatialAccuracyTolorence);
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof ConIntersected) {
						try {
							typeErrorLayer = layerValidator.validateConIntersected();
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof ConOverDegree) {
						double degree = ((ConOverDegree) option).getDegree();
						try {
							typeErrorLayer = layerValidator.validateConOverDegree(degree);
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof UselessPoint) {
						try {
							typeErrorLayer = layerValidator.validateUselessPoint();
						} catch (NoSuchAuthorityCodeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FactoryException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TransformException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof EntityDuplicated) {
						try {
							typeErrorLayer = layerValidator.validateEntityDuplicated();
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof OutBoundary) {
						List<String> relationNames = ((OutBoundary) option).getRelationType();
						for (int r = 0; r < relationNames.size(); r++) {
							try {
								typeErrorLayer = layerValidator.validateOutBoundary(
										types.getTypeLayers(relationNames.get(r), layerCollection),
										spatialAccuracyTolorence);
							} catch (SchemaException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (typeErrorLayer != null) {
								geometricResult.mergeErrorLayer(typeErrorLayer);
							}
						}
					}
					if (option instanceof SmallArea) {
						double area = ((SmallArea) option).getArea();
						try {
							typeErrorLayer = layerValidator.validateSmallArea(area);
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof SmallLength) {
						double length = ((SmallLength) option).getLength();
						try {
							typeErrorLayer = layerValidator.validateSmallLength(length);
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					if (option instanceof SelfEntity) {
						List<String> relationNames = ((SelfEntity) option).getRelationType();
						for (int r = 0; r < relationNames.size(); r++) {
							try {
								typeErrorLayer = layerValidator.validateSelfEntity(
										types.getTypeLayers(relationNames.get(r), layerCollection),
										spatialAccuracyTolorence, polygonInvadedTolorence);
							} catch (SchemaException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (typeErrorLayer != null) {
								geometricResult.mergeErrorLayer(typeErrorLayer);
							}
						}
					}
					if (option instanceof OverShoot) {
						double tolerence = ((OverShoot) option).getTolerence();
						try {
							typeErrorLayer = layerValidator.validateOverShoot(neatLayer, tolerence);
						} catch (SchemaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
					// if (option instanceof UnderShoot) {
					// double tolerence = ((UnderShoot)
					// option).getTolerence();
					// typeErrorLayer =
					// layerValidator.validateUnderShoot(neatLayer,
					// tolerence);
					// if (typeErrorLayer != null) {
					// errorLayer.mergeErrorLayer(typeErrorLayer);
					// }
					// }
					if (option instanceof NodeMiss) {
						List<String> relationNames = ((NodeMiss) option).getRelationType();
						for (int l = 0; l < relationNames.size(); l++) {
							try {
								typeErrorLayer = layerValidator.validateNodeMiss(
										types.getTypeLayers(relationNames.get(l), layerCollection), "",
										spatialAccuracyTolorence);
							} catch (SchemaException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (typeErrorLayer != null) {
								geometricResult.mergeErrorLayer(typeErrorLayer);
							}
						}
					}
					if (option instanceof PointDuplicated) {
						typeErrorLayer = layerValidator.validatePointDuplicated();
						if (typeErrorLayer != null) {
							geometricResult.mergeErrorLayer(typeErrorLayer);
						}
					}
				}
			}
		}

		List<Future<GeometricResult>> futures = new ArrayList<Future<GeometricResult>>();
		for (int i = 0; i < types.size(); i++) {
			// getType
			ValidateLayerType type = types.get(i);
			// getTypeLayers
			GeoLayerList typeLayers = types.getTypeLayers(type.getTypeName(), layerCollection);
			// getTypeOption
			List<ValidatorOption> options = type.getOptionList();
			if (options != null) {
				// typeLayerValidate
				for (int a = 0; a < typeLayers.size(); a++) {
					GeoLayer typeLayer = typeLayers.get(a);
					if (typeLayer == null) {
						continue;
					}
					Runnable task = new Task(geometricResult, options, typeLayer);
					Future<GeometricResult> future = executorService.submit(task, geometricResult);
					futures.add(future);
				}

				for (Future<GeometricResult> future : futures) {
					try {
						geometricResult = future.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				continue;
			}
		}
		executorService.shutdown();
		errorLayer.mergeErrorLayer(geometricResult.treadErrorLayer);
	}

	@SuppressWarnings("unused")
	private void layerMissValidate(ValidateLayerTypeList types, GeoLayerCollection layerCollection,
			ErrorLayer errorLayer) throws SchemaException {
		// TODO Auto-generated method stub
		List<GeoLayer> collectionList = layerCollection.getLayers();
		for (int j = 0; j < types.size(); j++) {
			ValidateLayerType type = types.get(j);
			GeoLayerList typeLayers = types.getTypeLayers(type.getTypeName(), layerCollection);
			List<ValidatorOption> options = type.getOptionList();
			if (options != null) {
				ErrorLayer typeErrorLayer = null;
				for (int k = 0; k < options.size(); k++) {
					ValidatorOption option = options.get(k);
					for (int l = 0; l < typeLayers.size(); l++) {
						GeoLayer typeLayer = typeLayers.get(l);
						if (typeLayer == null) {
							continue;
						}
					}
				}
			}
		}
	}

	private void closeCollectionValidate(ValidateLayerTypeList types, MapSystemRule mapSystemRule,
			GeoLayerCollection layerCollection, String geomColumn, ErrorLayer errorLayer) throws SchemaException {
		// TODO Auto-generated method stub
		String geomCol = "geom"; // 임시선언 geometry 컬럼

		if (layerCollection != null) {
			SimpleFeatureCollection neatLineCollection = layerCollection.getNeatLine().getSimpleFeatureCollection();
			SimpleFeatureIterator featureIterator = neatLineCollection.features();

			// 인접도엽 변수선언
			GeoLayerCollection topGeoCollection = null;
			GeoLayerCollection bottomGeoCollection = null;
			GeoLayerCollection leftGeoCollection = null;
			GeoLayerCollection rightGeoCollection = null;

			// GeoLayerCollectionList collectionList =
			// this.getLayerCollectionList();
			List<GeoLayerCollection> collectionList = this.nearCollections;

			if (collectionList != null) {
				String collectionName = layerCollection.getCollectionName();
				try {
					// 인접도엽 GET
					int collectionNum = Integer.parseInt(collectionName); // 대상도엽번호

					int topColltionNum = collectionNum + mapSystemRule.getMapSystemlRule(MapSystemRuleType.TOP);
					int bottomColltionNum = collectionNum + mapSystemRule.getMapSystemlRule(MapSystemRuleType.BOTTOM);
					int leftColltionNum = collectionNum + mapSystemRule.getMapSystemlRule(MapSystemRuleType.LEFT);
					int rightColltionNum = collectionNum + mapSystemRule.getMapSystemlRule(MapSystemRuleType.RIGHT);

					for (GeoLayerCollection nearCollection : collectionList) {
						try {
							String nearCollectionName = nearCollection.getCollectionName();
							int nearCollectionNum = Integer.parseInt(nearCollectionName);

							if (topColltionNum == nearCollectionNum) {
								topGeoCollection = nearCollection;
							}
							if (bottomColltionNum == nearCollectionNum) {
								bottomGeoCollection = nearCollection;
							}
							if (leftColltionNum == nearCollectionNum) {
								leftGeoCollection = nearCollection;
							}
							if (rightColltionNum == nearCollectionNum) {
								rightGeoCollection = nearCollection;
							}
						} catch (NumberFormatException e) {
							// TODO: handle exception
							LOGGER.info("인접도엽이름 정수아님");
						}

					}
				} catch (NumberFormatException e) {
					LOGGER.info("대상도엽 숫자아님");
					return;
				}
			} else {
				LOGGER.info("도엽리스트 Null");
				return;
			}

			Coordinate firstPoint = null;
			Coordinate secondPoint = null;
			Coordinate thirdPoint = null;
			Coordinate fourthPoint = null;

			int i = 0;
			while (featureIterator.hasNext()) {
				if (i == 0) {
					SimpleFeature feature = featureIterator.next();
					Geometry geometry = (Geometry) feature.getDefaultGeometry();

					Coordinate[] coordinateArray = this.getSortCoordinate(geometry.getCoordinates());

					firstPoint = coordinateArray[0];
					secondPoint = coordinateArray[1];
					thirdPoint = coordinateArray[2];
					fourthPoint = coordinateArray[3];

					i++;
				} else {
					LOGGER.info("도곽레이어 객체 1개이상");
					return;
				}
			}

			GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

			// 도엽라인객체 생성
			Coordinate[] topLineCoords = new Coordinate[] { firstPoint, secondPoint };
			Coordinate[] bottomLineCoords = new Coordinate[] { thirdPoint, fourthPoint };
			Coordinate[] leftLineCoords = new Coordinate[] { firstPoint, fourthPoint };
			Coordinate[] rightLineCoords = new Coordinate[] { secondPoint, thirdPoint };

			LineString topLineString = geometryFactory.createLineString(topLineCoords);
			LineString bottomLineString = geometryFactory.createLineString(bottomLineCoords);
			LineString leftLineString = geometryFactory.createLineString(leftLineCoords);
			LineString rightLineString = geometryFactory.createLineString(rightLineCoords);
			Polygon topBuffer = (Polygon) topLineString.buffer(spatialAccuracyTolorence);
			Polygon bottomBuffer = (Polygon) bottomLineString.buffer(spatialAccuracyTolorence);
			Polygon leftBuffer = (Polygon) leftLineString.buffer(spatialAccuracyTolorence);
			Polygon rightBuffer = (Polygon) rightLineString.buffer(spatialAccuracyTolorence);

			Map<MapSystemRuleType, LineString> collectionBoundary = new HashMap<MapSystemRule.MapSystemRuleType, LineString>();

			collectionBoundary.put(MapSystemRuleType.TOP, topLineString);
			collectionBoundary.put(MapSystemRuleType.BOTTOM, bottomLineString);
			collectionBoundary.put(MapSystemRuleType.LEFT, leftLineString);
			collectionBoundary.put(MapSystemRuleType.RIGHT, rightLineString);

			// 인접도엽 top, bottom, left, right 객체로드를 위한 Map 생성
			Map<MapSystemRuleType, Polygon> targetFeaturesGetBoundary = new HashMap<MapSystemRuleType, Polygon>();
			targetFeaturesGetBoundary.put(MapSystemRuleType.TOP, topBuffer);
			targetFeaturesGetBoundary.put(MapSystemRuleType.BOTTOM, bottomBuffer);
			targetFeaturesGetBoundary.put(MapSystemRuleType.LEFT, leftBuffer);
			targetFeaturesGetBoundary.put(MapSystemRuleType.RIGHT, rightBuffer);

			// 인접도엽 top, bottom, left, right 객체로드를 위한 Map 생성
			Map<MapSystemRuleType, Polygon> nearFeaturesGetBoundary = new HashMap<MapSystemRuleType, Polygon>();
			nearFeaturesGetBoundary.put(MapSystemRuleType.TOP, topBuffer);
			nearFeaturesGetBoundary.put(MapSystemRuleType.BOTTOM, bottomBuffer);
			nearFeaturesGetBoundary.put(MapSystemRuleType.LEFT, leftBuffer);
			nearFeaturesGetBoundary.put(MapSystemRuleType.RIGHT, rightBuffer);

			ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			CloseCollectionResult closeCollectionResult = new CloseCollectionResult();

			class Task implements Runnable {
				CloseCollectionResult closeCollectionResult;
				GeoLayer geoLayer;
				List<ValidatorOption> options;
				double tolorence = 0.0;
				GeoLayerCollection taskTopGeoCollection;
				GeoLayerCollection taskBottomGeoCollection;
				GeoLayerCollection taskLeftGeoCollection;
				GeoLayerCollection taskRightGeoCollection;

				Map<MapSystemRuleType, LineString> collectionBoundary;
				Map<MapSystemRuleType, Polygon> targetFeaturesGetBoundary;
				Map<MapSystemRuleType, Polygon> nearFeaturesGetBoundary;

				Task(CloseCollectionResult closeCollectionResult, List<ValidatorOption> options, GeoLayer geoLayer,
						double tolorence, Map<MapSystemRuleType, LineString> collectionBoundary,
						Map<MapSystemRuleType, Polygon> targetFeaturesGetBoundary,
						Map<MapSystemRuleType, Polygon> nearFeaturesGetBoundary, GeoLayerCollection topGeoCollection,
						GeoLayerCollection bottomGeoCollection, GeoLayerCollection leftGeoCollection,
						GeoLayerCollection rightGeoCollection) {
					this.closeCollectionResult = closeCollectionResult;
					this.geoLayer = geoLayer;
					this.options = options;
					this.tolorence = tolorence;
					this.collectionBoundary = collectionBoundary;
					this.targetFeaturesGetBoundary = targetFeaturesGetBoundary;
					this.nearFeaturesGetBoundary = nearFeaturesGetBoundary;
					this.taskTopGeoCollection = topGeoCollection;
					this.taskBottomGeoCollection = bottomGeoCollection;
					this.taskLeftGeoCollection = leftGeoCollection;
					this.taskRightGeoCollection = rightGeoCollection;
				}

				@Override
				public void run() {
					LayerValidatorImpl layerValidator = new LayerValidatorImpl();
					ErrorLayer typeErrorLayer = null;
					layerValidator.setValidatorLayer(geoLayer);

					Map<MapSystemRuleType, GeoLayer> collctionMap = new HashMap<MapSystemRule.MapSystemRuleType, GeoLayer>();
					GeoLayer topLayer = null;
					GeoLayer bottomLayer = null;
					GeoLayer leftLayer = null;
					GeoLayer rightLayer = null;

					if (geoLayer != null) {
						String layerName = geoLayer.getLayerName();

						// 인접도엽 검수옵션 생성 - 레이어단위
						ValCollectionOption collectionOptions = new ValCollectionOption();
						for (ValidatorOption option : options) {
							if (option instanceof EntityNone) {
								collectionOptions.putEntityNoneOption(true);
							}
							if (option instanceof RefAttributeMiss) {
								List<String> colunms = (List<String>) ((RefAttributeMiss) option)
										.getRefAttributeMaissOpt(layerName);
								collectionOptions.putRefAttributeMissOption(colunms);
							}
							if (option instanceof EdgeMatchMiss) {
								collectionOptions.putEdgeMatchMissOption(true);
							}
						}

						if (taskTopGeoCollection != null) {
							topLayer = taskTopGeoCollection.getLayer(layerName, taskTopGeoCollection);
							collctionMap.put(MapSystemRuleType.TOP, topLayer);
						}
						if (taskBottomGeoCollection != null) {
							bottomLayer = taskBottomGeoCollection.getLayer(layerName, taskBottomGeoCollection);
							collctionMap.put(MapSystemRuleType.BOTTOM, bottomLayer);
						}
						if (taskLeftGeoCollection != null) {
							leftLayer = taskLeftGeoCollection.getLayer(layerName, taskLeftGeoCollection);
							collctionMap.put(MapSystemRuleType.LEFT, leftLayer);
						}
						if (taskRightGeoCollection != null) {
							rightLayer = taskRightGeoCollection.getLayer(layerName, taskRightGeoCollection);
							collctionMap.put(MapSystemRuleType.RIGHT, rightLayer);
						}

						ValidateCloseCollectionLayer closeCollectionLayer = new ValidateCloseCollectionLayer(geoLayer,
								collctionMap, tolorence, collectionOptions, collectionBoundary,
								targetFeaturesGetBoundary, nearFeaturesGetBoundary);

						typeErrorLayer = layerValidator.validateCloseCollection(closeCollectionLayer, geomCol);
						if (typeErrorLayer != null) {
							closeCollectionResult.mergeErrorLayer(typeErrorLayer);
						}
					}
				}
			}
			;

			List<Future<CloseCollectionResult>> futures = new ArrayList<Future<CloseCollectionResult>>();
			for (ValidateLayerType layerType : types) {
				GeoLayerList typeLayers = types.getTypeLayers(layerType.getTypeName(), layerCollection);
				List<ValidatorOption> options = layerType.getOptionList();
				if (options != null) {
					for (GeoLayer geoLayer : typeLayers) {
						if (geoLayer != null) {
							Runnable task = new Task(closeCollectionResult, options, geoLayer, spatialAccuracyTolorence,
									collectionBoundary, targetFeaturesGetBoundary, nearFeaturesGetBoundary,
									topGeoCollection, bottomGeoCollection, leftGeoCollection, rightGeoCollection);

							Future<CloseCollectionResult> future = executorService.submit(task, closeCollectionResult);

							futures.add(future);
						}
					}
					for (Future<CloseCollectionResult> future : futures) {
						try {
							closeCollectionResult = future.get();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			executorService.shutdown();
			errorLayer.mergeErrorLayer(closeCollectionResult.treadErrorLayer);
		}
	}

	private Coordinate[] getSortCoordinate(Coordinate[] coordinates) {
		Coordinate[] returncoordinate = coordinates;
		if (coordinates.length == 5) {
			double fPointY = 0.0;
			double sPointY = 0.0;

			for (int a = 0; a < returncoordinate.length - 2; a++) {
				for (int j = 0; j < returncoordinate.length - 2; j++) {
					fPointY = returncoordinate[j].y;
					sPointY = returncoordinate[j + 1].y;

					Coordinate jCoordinate = returncoordinate[j];
					Coordinate kCoordinate = returncoordinate[j + 1];

					if (fPointY < sPointY) {
						returncoordinate[j + 1] = jCoordinate;
						returncoordinate[j] = kCoordinate;
					}
				}
			}

			Coordinate firstPoint = returncoordinate[0];
			Coordinate secondPoint = returncoordinate[1];
			Coordinate thirdPoint = returncoordinate[2];
			Coordinate fourthPoint = returncoordinate[3];

			if (firstPoint.x > secondPoint.x) {
				returncoordinate[0] = secondPoint;
				returncoordinate[1] = firstPoint;
			}

			if (thirdPoint.x < fourthPoint.x) {
				returncoordinate[2] = fourthPoint;
				returncoordinate[3] = thirdPoint;
			}

			returncoordinate[4] = returncoordinate[0];
		} else
			return null;

		return returncoordinate;
	}

}

/**
 * 쓰레드 AttResult 클래스
 * 
 * @author SG.Lee
 * @Date 2017. 9. 6. 오후 3:09:38
 */
class AttResult {
	ErrorLayer treadErrorLayer = new ErrorLayer();

	synchronized void mergeErrorLayer(ErrorLayer typeErrorLayer) {
		treadErrorLayer.mergeErrorLayer(typeErrorLayer);
	}
}

/**
 * 쓰레드 GeomResult 클래스
 * 
 * @author SG.Lee
 * @Date 2017. 9. 6. 오후 3:09:38
 */
class GeometricResult {
	ErrorLayer treadErrorLayer = new ErrorLayer();

	synchronized void mergeErrorLayer(ErrorLayer typeErrorLayer) {
		treadErrorLayer.mergeErrorLayer(typeErrorLayer);
	}
}

/**
 * 쓰레드 CloseCollectionResult 클래스
 * 
 * @author SG.Lee
 * @Date 2017. 9. 6. 오후 3:09:38
 */
class CloseCollectionResult {
	ErrorLayer treadErrorLayer = new ErrorLayer();

	synchronized void mergeErrorLayer(ErrorLayer typeErrorLayer) {
		treadErrorLayer.mergeErrorLayer(typeErrorLayer);
	}
}
