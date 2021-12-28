package joeyjupiter

import groovy.sql.Sql
import groovy.sql.GroovyRowResult
import org.json.simple.JSONValue

class PrizeService {
	
	def dataSource

	def serviceMethod() {
		println "Prize service - Joe was here"
	}
		
	def prizeDistance(double playerLongitude, double playerLatitude) {
		def sql = new groovy.sql.Sql(dataSource)
		def prize = sql.dataSet("Prize")
		def prizeRows = (ArrayList)prize.rows("select latitude, longitude, location, prize_id from tictacshmo_prizes where available is true")
		def rowsReturned = prizeRows.size()
		Map distanceMap	= new HashMap()	
		double prizeLongitude, prizeLatitude, distanceInMiles 
		
		for (int x; x < rowsReturned; x++) {
			prizeLongitude = prizeRows.get(x).get("longitude")
			prizeLatitude = prizeRows.get(x).get("latitude")
			distanceInMiles = calculateDistanceToPrize(playerLatitude, playerLongitude, prizeLatitude, prizeLongitude)
			def prizeId = prizeRows.get(x).get("prize_id")
			GroovyRowResult row =  prize.firstRow("select name, url, location, image, image_width, image_height from tictacshmo_prizes where prize_id = ?", prizeId)
			Map prizeValues = new HashMap();
			prizeValues.put("name", row.get("name"))
			prizeValues.put("url", row.get("url"))
			prizeValues.put("location", row.get("location"))
			
			prizeValues.put("distance", String.valueOf(distanceInMiles))
			prizeValues.put("image", row.get("image"))
			prizeValues.put("imageWidth", row.get("image_width"))
			prizeValues.put("imageHeight", row.get("image_height"))
			prizeValues.put("id", prizeId)
			distanceMap.put("prize" + ":" + prizeId, prizeValues)
		}
		return distanceMap
	}
	
	def calculateDistanceToPrizeOrig(double playerLongitude, double playerLatitude, double prizeLongitude, double prizeLatitude) {
		char unit = "M"
		double theta = playerLongitude - prizeLongitude
		double dist = Math.sin(deg2rad(playerLatitude)) * Math.sin(deg2rad(prizeLatitude)) + Math.cos(deg2rad(playerLatitude)) * Math.cos(deg2rad(prizeLatitude)) * Math.cos(deg2rad(theta))
		dist = Math.acos(dist)
		dist = rad2deg(dist)
		dist = dist * 60 * 1.1515
		if (unit == "K") {
		  dist = dist * 1.609344
		} else if (unit == "N") {
			dist = dist * 0.8684
		}
		return dist
	}
	  
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	double deg2rad(double deg) {
		return (deg * Math.PI / 180.0)
	}
	  
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI)
	}
	
        def calculateDistanceToPrize(double lat1, double lon1, double lat2, double lon2) {
            
            lat1 = Math.toRadians(lat1)
            lon1 = Math.toRadians(lon1)
            lat2 = Math.toRadians(lat2)
            lon2 = Math.toRadians(lon2)
	    
            //double earthRadius = 6371.01; //Kilometers
            double earthRadius = 3958.8; //Miles
            
            return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2))
        }
}

