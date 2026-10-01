package com.alert360.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    // SRID 4326 = Système de coordonnées WGS 84 utilisé par le GPS mondial (Latitude, Longitude)
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    /**
     * Convertit une latitude et une longitude en un objet Point JTS / PostGIS.
     * Note : JTS utilise l'ordre des axes (X, Y) ce qui correspond à (Longitude, Latitude).
     */
    public static Point createPoint(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }
        return GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
    }
}