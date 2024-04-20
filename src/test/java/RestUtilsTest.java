import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.ResponseData;
import models.rest.RestUtilities;

class RestUtilsTest
{
	
	RestClient client;
	ObjectMapper mapper;
	ResponseData team;
	
	void create(Record r, String uri) throws JsonMappingException, JsonProcessingException
	{
		String response = client.post().uri(uri).body(r).retrieve().body(String.class);
		System.out.println(response);
		JsonNode finalResponse = mapper.readTree(response);
//		System.out.println(finalResponse.message);
		assertTrue(finalResponse.get("successful").asBoolean());
	}
	
	@BeforeEach
	void setUp() throws Exception
	{
		// remember to run server before
		client = RestClient.create();
		mapper = new ObjectMapper();

		team = new ResponseData(RestUtilities.TEAM_NAME, RestUtilities.TEAM_DESC, RestUtilities.TEAM_URI);

		// delete existing team for ease of testing
		String result = client.delete().uri(RestUtilities.TEAM_URI).retrieve().body(String.class);
		System.out.println(result);

		// recreate now!
		create(team, RestUtilities.TEAM_URI);
	}

	@Test
	void test() throws JsonMappingException, JsonProcessingException
	{
		// join function
		assertEquals("a", RestUtilities.join("a"));
		assertEquals("a/b", RestUtilities.join("a", "b"));
		assertEquals("a/b/c", RestUtilities.join("a", "b", "c"));
		
		// test doesResourceExist & createResource
		assertFalse(RestUtilities.doesResourceExist("test123"));
		assertTrue(RestUtilities.createResource("test123", "testing"));
		assertTrue(RestUtilities.doesResourceExist("test123"));
		
	}

}
