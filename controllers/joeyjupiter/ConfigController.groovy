package joeyjupiter
import grails.converters.JSON
import java.io.BufferedWriter

class ConfigController {
    
    def getConfigValues = {
    
        
        def criteria = Config.createCriteria()
        def result = criteria.list {}
        /*
        get {
            eq("key", "RabbitMQIpAddress")
            
            projections {
                property("value")
            }
	}
	*/
	
	//println "IP Address is: " + result.value
	
	//render result.value 
	//render (contentType: "text/plain") {result.value} 
	//render (result.value, contentType: "text/plain", encoding: "UTF-8") 
	//render(text: result.value, contentType: "text/html", encoding: "ASCII")
	//render (contentType: "text/plain") {result.value} 
	//render "Joe was here " + result.value
	//render result.value
	//render "Joe was here ${result.value}"
	//render contentType: "text/plain", text: result.value
	render result as JSON
    }
}
