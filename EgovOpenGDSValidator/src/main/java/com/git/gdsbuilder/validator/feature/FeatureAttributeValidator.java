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
 *    Lesser General Public License for more details.
 */

package com.git.gdsbuilder.validator.feature;

import java.util.List;
import java.util.Map;

import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;

import com.git.gdsbuilder.type.validate.error.ErrorFeature;

/**
 * SimpleFeature를 속성 검수하는 클래스
 * 
 * @author DY.Oh
 * @Date 2017. 4. 18. 오후 3:32:00
 */
public interface FeatureAttributeValidator {

	/**
	 * 검수 항목 중 “속성 오류(Attribute Fix)” 검수. 검수 대상
	 * 
	 * @param simpleFeature
	 *            검수 대상 Simplefeature 객체
	 * @param notNullAtt
	 *            속성 컬럼명 및 속성 값
	 * @return ErrorFeature
	 * @throws SchemaException
	 */
	public ErrorFeature validateAttributeFix(SimpleFeature simpleFeature, Map<String, List<String>> notNullAtt)
			throws SchemaException;

	/**
	 * 검수 항목 중 “고도값 오류 (Z-Value Abmiguous)” 검수
	 * 
	 * @param simpleFeature
	 *            검수 대상 Simplefeature 객체
	 * @param attributeKey
	 *            고도값 속성 컬럼명
	 * @return ErrorFeature
	 * @throws SchemaException
	 */
	public ErrorFeature validateZvalueAmbiguous(SimpleFeature simpleFeature, Map<String, List<String>> attributeKey)
			throws SchemaException;

	/**
	 * 검수 항목 중 “요소 중복 오류(EntityDuplicaated)” 속성 중복 검수
	 * 
	 * @param simpleFeatureI
	 *            검수 대상 Simplefeature 객체
	 * @param simpleFeatureJ
	 *            비교 대상 Simplefeature 객체
	 * @return ErrorFeature
	 */
	public ErrorFeature validateEntityDuplicated(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ);

}
