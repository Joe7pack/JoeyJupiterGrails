package joeyjupiter
import groovy.sql.Sql

class GamePlayer {
	
	String userName
	boolean onlineNow
	boolean playingNow
	int highScore
	int opponentId
	int highScoreOpponentId
	int gamesPlayed
	//int portNumber
	//Date lastAccessDate
	Date lastUpdated
	Date dateCreated
	
	Double longitude
	Double latitude
	String deviceId
	
	String getOpponentName() {
		def gp = GamePlayer.findById(getId())
		def searchValue = Long.valueOf(Integer.toString(gp.opponentId))
		if (searchValue == 0)
			return ""
		String[] queryResult = executeQuery("select userName from GamePlayer where id = ?", searchValue)
		return queryResult[0]
	}

	String getHighScoreOpponentName() {
		def gp = GamePlayer.findById(getId())
		def searchValue = Long.valueOf(Integer.toString(gp.highScoreOpponentId))
		if (searchValue == 0)
			return ""
		String[] queryResult = executeQuery("select userName from GamePlayer where id = ?", searchValue)
		return queryResult[0]
	}
	
    // static constraints = { }
	
	static transients = ['opponentName', 'highScoreOpponentName']
	
	static mapping = {
		table 'tictacshmo_players'
		columns {
			id column:'player_id'
                        userName column: 'user_name'
                        onlineNow column: 'online_now'
                        playingNow column: 'playing_now'
			gamesPlayed column: 'games_played'
			highScore column: 'high_score'
			opponentId column: 'opponent_id'
                        highScoreOpponentId column: 'high_score_opponent_id'
			deviceId column: 'device_id'
			totalPrizesWon column: 'total_prizes_won'
			prizesWonToday column: 'prizes_won_today'
                        lastUpdated column: 'last_updated'
                        dateCreated column: 'date_created'
		}
	}

}
	


