package joeyjupiter

import groovy.sql.Sql

class Prize {
	
	String name
	String url
	String location
	byte[] image
	double latitude
	double longitude
	int image_width
	int image_height
	int available
	
	// static constraints = { }
	
	static mapping = {
		table 'tictacshmo_prizes'
		columns {
			id column:'prize_id'
		}
	}

}
	



