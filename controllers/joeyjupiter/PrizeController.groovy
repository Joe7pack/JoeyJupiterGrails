package joeyjupiter

import grails.converters.JSON
import java.io.BufferedWriter

class PrizeController {
	
	def getPrizes  = {
		def criteria = Prize.createCriteria()
		def jsonPrizes = criteria.list {
			ne("latitude", 0d)
			eq("longitude", 2d)
		}
		def result = [PrizeList: jsonPrizes]
		render result as JSON
	}

	PrizeService prizeService
	
	def builder = new groovy.json.JsonBuilder()
	
	def getPrizesByDistance = {
		Map out = prizeService.prizeDistance(Double.valueOf(params['longitude']), Double.valueOf(params['latitude']))
		def jsonResult = out
		def result = [PrizeList: jsonResult]
		render result as JSON
	}
}
