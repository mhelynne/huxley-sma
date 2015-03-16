package util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonReader {

	static Logger logger = LoggerFactory.getLogger(JsonReader.class);

	public static Object readValueAsObject(String jsonString, Class<?> type) {
		
		Object object = null;
		ObjectReader reader = new ObjectMapper().reader(type);
		
		try {
			object = reader.readValue(jsonString);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return object;
		
		
	}

	
}
