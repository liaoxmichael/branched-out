package models.rest;
import org.springframework.web.client.RestClient;

public final class RestUtilities
{
	public static final String IP_ADDRESS = "http://localhost:9000";
	public static final String TEAM_RESOURCE = "v1";
	public static final String BASE_URI = join(IP_ADDRESS, TEAM_RESOURCE);

	public static final String TEAM_NAME = "Liao";
	public static final String TEAM_DESC = "Michael Liao's personal team resources";
	public static final String TEAM_URI = join(BASE_URI, TEAM_NAME);

	private RestUtilities()
	{
		// do not instantiate
	}

	/*
	 * @param base base string to start with
	 * 
	 * @param resources any additional resource directories
	 */
	public static String join(String base, String... resources)
	{
		String result = base;
		for (String s : resources)
		{
			result += "/" + s;
		}
		return result;
	}

	public static boolean doesResourceExist(int id, String resource)
	{
		RestClient client = RestClient.create();
		ResponseObject responseObject = client.get()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, resource, String.valueOf(id))).retrieve()
				.body(ResponseObject.class);

		return responseObject.successful(); // if empty: false; if exists: true
	}

	// overloaded version with no id to check resource directories
	public static boolean doesResourceExist(String resource)
	{
		RestClient client = RestClient.create();
		ResponseObject responseObject = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, resource))
				.retrieve().body(ResponseObject.class);

		return responseObject.successful();
	}

	public static boolean createResource(String resource, String resource_desc)
	{
		RestClient client = RestClient.create();
		ResponseData r = new ResponseData(resource, resource_desc,
				RestUtilities.join(RestUtilities.TEAM_URI, resource));
		ResponseObject responseObject = client.post().uri(RestUtilities.join(RestUtilities.TEAM_URI, resource)).body(r)
				.retrieve().body(ResponseObject.class);

		return responseObject.successful();
	}

}
