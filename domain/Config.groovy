package joeyjupiter
import groovy.sql.Sql

class Config {
	
	String key
	String value
	
	// static constraints = { }
	
	static mapping = {
		table 'tictacshmo_config'
		columns {
			key: 'key'
			value: 'value'
		}
	}

}
	



