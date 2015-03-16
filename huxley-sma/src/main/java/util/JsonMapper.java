package util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMapper {

	static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
	
	public static String writeValueAsString(Object o) {
		
		String jsonString= null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			jsonString = mapper.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return jsonString;
		
	}

}
