package Utils.DistanceUtil;


import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class DistanceUtil {

    public static double getDistance(String fromLon,String fromLat, String toLon,String toLat) {

        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(fromLat), Double.parseDouble(fromLon));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(toLat), Double.parseDouble(toLon));
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, source, target);
        //获取距离

        return geoCurve.getEllipsoidalDistance()*1.2;
    }
}
