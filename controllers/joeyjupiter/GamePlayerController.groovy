package joeyjupiter

import grails.converters.JSON
import grails.converters.XML;

import groovy.sql.Sql

import grails.gorm.transactions.Transactional

@Transactional
class GamePlayerController {

	def scaffold = GamePlayer
	def authorizedPassword = "forkInTheSpoon"
	def authorizationKey = "accessAuthorized"
	def redirectUrl = "http://onion.com"
	GamePlayerService gamePlayerService
	
    // def index = { }
	
	def list  = {
		println "params in list = " + params;
		def accessAuthorized = params[authorizationKey]
		if (accessAuthorized != null && accessAuthorized.equals(authorizedPassword)) {
             [ gamePlayerInstanceList : GamePlayer.list(params), gamePlayerInstanceTotal : GamePlayer.count() ]
		} else
			redirect(url:redirectUrl)
	}
	
	def listx = {
		def jsonGamePlayer = new GamePlayer(
				userName: params['userName'],
				userPassword: params['userPassword'],
//				ipAddress:  params['ipAddress'],
//				ipAddress: request.getRemoteHost(),
//				userPortNumber: request.getRemotePort(),
		)
		
//		def jsonBook = Book.list()
		def result = [jsonGamePlayer]
		
		render result as JSON
//		render result as XML
		//redirect(action:list)
		
	}

	def listUsers = {
		def criteria = GamePlayer.createCriteria()
		def jsonGamePlayer = criteria.list {
			eq("onlineNow", true) 
			eq("playingNow", false)
		}
		// def jsonGamePlayer = GamePlayer.list(sort:"userName", order:"asc")
		// def result = [jsonGamePlayer]
		def result = [UserList: jsonGamePlayer]
		render result as JSON
//		render result as XML
	}

	def create = {
		//println "calling create"
		def accessAuthorized = params[authorizationKey]
		if (accessAuthorized != null && accessAuthorized.equals(authorizedPassword)) {
			//println "accessAuthorized is: " + accessAuthorized;
			def gamePlayerInstance = new GamePlayer(
				userName: params['userName'],
				//lastAccessDate: new Date(),
				//ipAddress: request.getRemoteHost(),
				//ipAddress:  params['ipAddress'],
				//ipAddress: process,
				//portNumber: params['portNumber'],
			)
			return [gamePlayerInstance: gamePlayerInstance]
		
		} else {
			redirect(url:redirectUrl)
		}
	}

	def getDistancBetweenPlayers = {
	    def gamePlayer = GamePlayer.get(params.player1)
	    def gamePlayer2 = GamePlayer.get(params.player2)
            if (gamePlayer && gamePlayer2) {
		double latitudePlayer1 = gamePlayer.latitude
		double latitudePlayer2 = gamePlayer2.latitude			
		double longitudePlayer1 = gamePlayer.longitude
		double longitudePlayer2 = gamePlayer2.longitude 
		double distancBetweenPlayers = gamePlayerService.calculateDistanceBetweenPlayers(latitudePlayer1, longitudePlayer1, latitudePlayer2, longitudePlayer2)
		println "got the latitude, both of them!" 
		def result = [Distance: distancBetweenPlayers]
		render result as JSON
            } else {
		def result = [Distance: "unableToDetermine"]
		render result as JSON		
            }		
	}

	def update = {
		println "hi from GamePlayerController update!"
		int highScore = 0
		def gamePlayer = GamePlayer.get(params['id'])
		if (gamePlayer && params['userName'] != null) {
			if (gamePlayer.userName.equals(params['userName'])) {
				highScore = gamePlayer.getHighScore()
			}
			gamePlayer.setUserName(params['userName'])
			gamePlayer.setHighScore(highScore);
		}
		
		if (params['onlineNow']) {
			gamePlayer.setOnlineNow(new Boolean(params['onlineNow']))
			println "online now set"
		}

		if (params['playingNow']) {
			gamePlayer.setPlayingNow(new Boolean(params['playingNow']))
			println "set playing now"
		}

		if (params['opponentId']) {
			gamePlayer.setOpponentId(new Integer(params['opponentId']))
		}

		if (params['longitude']) {
			gamePlayer.setLongitude(new Double(params['longitude']))
			//println "longitude reset"
			//gamePlayer.validate()
			//println "new longitude: " + gamePlayer.getLongitude()
			//gamePlayer.save(flush:true)
			//println "longitude saved"
			
                        if (!gamePlayer.validate()) {
                            gamePlayer.errors.allErrors.each {
                                println it
                            }
                        }			
		}
		
		if (params['latitude']) {
			gamePlayer.setLatitude(new Double(params['latitude']))
		}

		if (params['deviceId']) {
			gamePlayer.setDeviceId(new String(params['deviceId']))
		}
		
		if (!gamePlayer.hasErrors() && gamePlayer.save()) {
		println "saved player"
			redirect(action: "showAndroid",id:gamePlayer.id)
		}
	}

	def updateGamesPlayed = {
//		println(params)
		gamePlayerService.updateGamesPlayed(params['id'],params['score'])
		//gamePlayerService.updateGamesPlayed2(params['id','score']) // this doesn't work
		//gamePlayerService.updateGamesPlayed2(params['id'])
		redirect(action: "showAndroid",id:params['id'])
	}
	
	def createAndroid = {
		//println "hi from createAndroid"
		println " createAndroid params = " + params;
		
		def gamePlayer = new GamePlayer(
			userName: params['userName'],
			deviceId: params['deviceId'],
			longitude: params['longitude'],
			latitude: params['latitude']
			//onlineNow: true //params['onlineNow']
			//lastAccessDate: new Date(),
		)
		
		println " createAndroid error: " + gamePlayer.hasErrors()
		//gamePlayer.save()
		//println "gamePlayer saved "
		
		if (!gamePlayer.hasErrors() && gamePlayer.save()) {
			println "about to redirect to showAndroid"
			redirect(action: "showAndroid",id:gamePlayer.id)
		}
	}
		
	def showOrig = {
		def gamePlayerInstance = GamePlayer.get(params['id']) //params['userName'])
		if (gamePlayerInstance) {
			[gamePlayerInstance: gamePlayerInstance]
		}
	}
	
	def showAndroid  = { 
		println "showAndroid params = " + params;
		def gamePlayerInstance = GamePlayer.get(params['id']) //params['userName'])
		if (gamePlayerInstance) {
			[gamePlayerInstance: gamePlayerInstance]
			def jsonResult = gamePlayerInstance
			def result = [User: jsonResult]
			render result as JSON
//			String responseTo = "http://" + request.getRemoteHost() + ":" + request.getRemotePort()
//			log.info responseTo
//			redirect(responseTo)
		}
	}
	
	def highScores = {
		if (!params.max)
			params.max = 20
		[ gamePlayerInstanceList : GamePlayer.list(sort:"highScore", order:"desc"), gamePlayerInstanceTotal : GamePlayer.count() ]
	}
	
	def edit = {
		//println(">>>>>>>>>>>>>>>inside edit ")
		//println "params = " + params;
		//println "saveAccessAuthorized = " + saveAccessAuthorized 
		def accessAuthorized = params[authorizationKey]
		if (accessAuthorized != null && accessAuthorized.equals(authorizedPassword)) {
			//println "accessAuthorized is: " + accessAuthorized;
			def gamePlayerInstance = GamePlayer.get(params['id'])
			return [ gamePlayerInstance : gamePlayerInstance ]
		} else {
			//println "cannot find password"
			redirect(url:redirectUrl)
		}
	}
	
	def delete = {
		//println "really gonna delete it!"
		def accessAuthorized = params[authorizationKey]
		if (accessAuthorized != null && accessAuthorized.equals(authorizedPassword)) {
			def gamePlayerInstance = GamePlayer.get(params.id)
			if (gamePlayerInstance) {
				try {
					gamePlayerInstance.delete(flush: true)
					flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'gamePlayer.label', default: 'GamePlayer'), params.id])}"
//					redirect(action: "list")
					redirect(action: "listAfterDelete")
				}
				catch (org.springframework.dao.DataIntegrityViolationException e) {
					flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'gamePlayer.label', default: 'GamePlayer'), params.id])}"
					redirect(action: "show", id: params.id)
				}
			} else {
				flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'gamePlayer.label', default: 'GamePlayer'), params.id])}"
				redirect(action: "list")
			}
		}
	}

	def listAfterDelete = {
		//println "inside listAfterDelete"
		//redirect(action:list, params:[authorizationKey:authorizedPassword]) // we get: params in list = [authorizationKey:yesIcan, action:list, controller:gamePlayer]
		//redirect(action:list, params:["${authorizationKey}":"${authorizedPassword}"]) // password is omitted but accessAuthorized is there! Go figure....
		//redirect(action:list, params:["${authorizationKey}":authorizedPassword]) // we get: params in list = [accessAuthorized:, action:list, controller:gamePlayer]
		redirect(action: "list", params:[accessAuthorized:authorizedPassword]) // so we'll just use this workaround for now
	}
	
}
