package models.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

import models.Identifiable;

public final class RestUtilities
{
	public static final String IP_ADDRESS = "http://localhost:9000";
	public static final String TEAM_RESOURCE = "v1";
	public static final String BASE_URI = join(IP_ADDRESS, TEAM_RESOURCE);

	public static final String TEAM_NAME = "Liao";
	public static final String TEAM_DESC = "Michael Liao's personal team resources";
	public static final String TEAM_URI = join(BASE_URI, TEAM_NAME);

	private static RestClient client = RestClient.create();

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
//		RestClient client = RestClient.create();
		ResponseObject responseObject = client.get()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, resource, String.valueOf(id))).retrieve()
				.body(ResponseObject.class);

		return responseObject.successful(); // if empty: false; if exists: true
	}

	// overloaded version with no id to check resource directories
	public static boolean doesResourceExist(String resource)
	{
//		RestClient client = RestClient.create();
		ResponseObject responseObject = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, resource))
				.retrieve().body(ResponseObject.class);

		return responseObject.successful();
	}

	public static boolean createResource(String resource, String resource_desc)
	{
//		RestClient client = RestClient.create();
		ResponseData r = new ResponseData(resource, resource_desc,
				RestUtilities.join(RestUtilities.TEAM_URI, resource));
		ResponseObject responseObject = client.post().uri(RestUtilities.join(RestUtilities.TEAM_URI, resource)).body(r)
				.retrieve().body(ResponseObject.class);

		return responseObject.successful();
	}

	public static JsonNode retrieve(int id, String resourceName)
	{
//		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, resourceName))
		{
			ResponseNode response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, resourceName, String.valueOf(id))).retrieve()
					.body(ResponseNode.class);

			return response.data();
		}
		// else
		return null;
	}

	public static List<JsonNode> retrieveAll(String resourceName)
	{
//		RestClient client = RestClient.create();

		List<JsonNode> list = new ArrayList<JsonNode>();

		if (RestUtilities.doesResourceExist(resourceName))
		{
			ResponseListData response = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, resourceName))
					.retrieve().body(ResponseListData.class);
			for (int i = 0; i < response.data().size(); i++)
			{
				ResponseData d = response.data().get(i);
				ResponseNode n = client.get().uri(d.location()).retrieve().body(ResponseNode.class);
				list.add(n.data());
			}
		}
		return list;
	}

	public static boolean store(Identifiable obj, Class<?> targetClass, String resourceName, String resourceDesc)
	{
		ResponseObject result;
//		RestClient client = RestClient.create();
		if (!RestUtilities.doesResourceExist(resourceName))
		{ // need to create the thing!
			RestUtilities.createResource(resourceName, resourceDesc);
		}
		result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, resourceName, String.valueOf(obj.getId())))
				.body(targetClass.cast(obj)).retrieve().body(ResponseObject.class);
//		System.out.println(result.message());
		return result.successful();
	}

	public static boolean update(Identifiable obj, Class<?> targetClass, String resourceName, String resourceDesc)
	{
		ResponseObject result;
		result = client.put().uri(RestUtilities.join(RestUtilities.TEAM_URI, resourceName, String.valueOf(obj.getId())))
				.body(targetClass.cast(obj)).retrieve().body(ResponseObject.class);

//		System.out.println(result.message());
		return result.successful();
	}
}