package com.git.gdsbuilder.FileRead.dxf.parser;

import org.geotools.feature.SchemaException;
import org.kabeja.dxf.DXFArc;
import org.kabeja.dxf.DXFCircle;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFInsert;
import org.kabeja.dxf.DXFLWPolyline;
import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.DXFPolyline;
import org.kabeja.dxf.DXFSolid;
import org.kabeja.dxf.DXFText;

import com.git.gdsbuilder.type.dxf.feature.DTDXFFeature;
import com.git.gdsbuilder.type.dxf.feature.style.DTDXFArcStyle;
import com.git.gdsbuilder.type.dxf.feature.style.DTDXFCircleStyle;
import com.git.gdsbuilder.type.dxf.feature.style.DTDXFInsertStyle;
import com.git.gdsbuilder.type.dxf.feature.style.DTDXFLWPolylineStyle;
import com.git.gdsbuilder.type.dxf.feature.style.DTDXFStyle;
import com.git.gdsbuilder.type.dxf.feature.style.DTDXFTextSyle;
import com.vividsolutions.jts.geom.Geometry;

public class DXFFileFeatureParser {

	public static DTDXFFeature parseDTLineFeaeture(DXFEntity dxfEntity) throws SchemaException {

		Geometry geom = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("LINE")) {
			DXFLine dxfLine = (DXFLine) dxfEntity;
			geom = DXFFileGeomParser.parseDTLine(dxfLine.getStartPoint(), dxfLine.getEndPoint());

			String entityID = dxfLine.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			return dxfFeature;
		} else {
			return null;
		}
	}

	public static DTDXFFeature parseDTPolylineFeature(DXFEntity dxfEntity) {

		Geometry geom = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("POLYLINE")) {
			DXFPolyline dxfPolyline = (DXFPolyline) dxfEntity;
			boolean flag = dxfPolyline.isClosed();
			geom = DXFFileGeomParser.parseDTLineString(flag, dxfPolyline.getVertexIterator(),
					dxfPolyline.getVertexCount());
			double elevation = geom.getCoordinate().z;
			String entityID = dxfPolyline.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			dxfFeature.setElevation(elevation);
			dxfFeature.setFlag(dxfEntity.getFlags());
			return dxfFeature;
		} else {
			return null;
		}
	}

	public static DTDXFFeature parseDTLWPolylineFeature(DXFEntity dxfEntity) {

		Geometry geom = null;
		DTDXFLWPolylineStyle style = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("LWPOLYLINE")) {
			DXFLWPolyline dxfLwPolyline = (DXFLWPolyline) dxfEntity;
			double elevation = dxfLwPolyline.getElevation();
			boolean flag = dxfLwPolyline.isClosed();
			geom = DXFFileGeomParser.parseDTLineString(flag, dxfLwPolyline.getVertexIterator(),
					dxfLwPolyline.getVertexCount());
			String entityID = dxfLwPolyline.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			dxfFeature.setElevation(elevation);
			dxfFeature.setFlag(dxfEntity.getFlags());
			// dxfFeature.setProperties(EnDXFPolyline.getProperties(style));
			return dxfFeature;
		} else {
			return null;
		}
	}

	public static DTDXFFeature parseDTInsertFeature(DXFEntity dxfEntity) {

		Geometry geom = null;
		DTDXFInsertStyle style = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("INSERT")) {
			DXFInsert dxfInsert = (DXFInsert) dxfEntity;
			// attribute
			// style = QA10FileStyleParser.parseInsertStyle(dxfEntity);
			// gemo
			geom = DXFFileGeomParser.parseDTPoint(dxfInsert.getPoint());
			double rotate = dxfInsert.getRotate();
			String entityID = dxfInsert.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			dxfFeature.setRotate(rotate);
			// dxfFeature.setProperties(EnDXFInsert.getProperties(style));
			return dxfFeature;
		} else {
			return null;
		}
	}

	public static DTDXFFeature parseDTCircleFeature(DXFEntity dxfEntity) {

		Geometry geom = null;
		DTDXFCircleStyle style = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("CIRCLE")) {
			DXFCircle dxfCircle = (DXFCircle) dxfEntity;
			// attribute
			// style = QA10FileStyleParser.parseCircleStyle(dxfCircle);
			// gemo
			geom = DXFFileGeomParser.parseDTCircle(dxfCircle.getCenterPoint(), dxfCircle.getRadius());
			String entityID = dxfCircle.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			// dxfFeature.setProperties(EnDXFCircle.getProperties(style));
			return dxfFeature;
		} else {
			return null;
		}

	}

	public static DTDXFFeature parseDTSolidFeature(DXFEntity dxfEntity) {

		Geometry geom = null;
		DTDXFStyle style = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("SOLID")) {
			DXFSolid dxfSolid = (DXFSolid) dxfEntity;
			// attribute
			// style = QA10FileStyleParser.parseSolidStyle(dxfEntity);
			// gemo
			geom = DXFFileGeomParser.parseDTPolygon(dxfSolid.getPoint1(), dxfSolid.getPoint2(), dxfSolid.getPoint3(),
					dxfSolid.getPoint4());

			String entityID = dxfSolid.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			// dxfFeature.setProperties(EnDXFCommon.getProperties(style));
			return dxfFeature;
		} else {
			return null;
		}

	}

	public static DTDXFFeature parseDTTextFeature(DXFEntity dxfEntity) {

		Geometry geom = null;
		DTDXFTextSyle style = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("TEXT")) {
			DXFText dxfText = (DXFText) dxfEntity;
			// attribute
			// style = QA10FileStyleParser.parseTextStyle(dxfText);
			// gemo
			geom = DXFFileGeomParser.parseDTPoint(dxfText.getInsertPoint());

			String entityID = dxfText.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setHeight(dxfText.getHeight());
			dxfFeature.setRotate(dxfText.getRotation());
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			dxfFeature.setTextValue(dxfText.getText());
			// dxfFeature.setProperties(EnDXFText.getProperties(style));
			return dxfFeature;
		} else {
			return null;
		}
	}

	public static DTDXFFeature parseDTArcFeature(DXFEntity dxfEntity) {

		Geometry geom = null;
		DTDXFArcStyle style = null;
		String entityType = dxfEntity.getType();
		if (entityType.equals("ARC")) {
			DXFArc dxfArc = (DXFArc) dxfEntity;
			// attribute
			// style = QA10FileStyleParser.parseArcStyle(dxfArc);
			// gemo
			geom = DXFFileGeomParser.parseDTArc(dxfArc.getCenterPoint(), dxfArc.getRadius(), dxfArc.getStartAngle(),
					dxfArc.getTotalAngle());

			String entityID = dxfArc.getID();
			DTDXFFeature dxfFeature = new DTDXFFeature(entityID);
			dxfFeature.setFeatureType(entityType);
			dxfFeature.setGeom(geom);
			// dxfFeature.setProperties(EnDXFArc.getProperties(style));
			return dxfFeature;
		} else {
			return null;
		}
	}

}
