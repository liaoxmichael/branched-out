package models.rest;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

import models.Identifiable;

//import java.util.Map;
//import static java.util.Map.entry;
//import models.Company;
//import models.IdentifiableObjectManager;
//import models.JobPosting;
//import models.Link;
//import models.Page;
//import models.Person;
//import models.Project;
//import models.Skill;
//import models.SkillProficiency;
//import models.WorkExperience;

public final class RestUtilities
{
	public static final String IP_ADDRESS = "http://localhost:9000";
	public static final String TEAM_RESOURCE = "v1";
	public static final String BASE_URI = join(IP_ADDRESS, TEAM_RESOURCE);

	public static final String TEAM_NAME = "Liao";
	public static final String TEAM_DESC = "Michael Liao's personal team resources";
	public static final String TEAM_URI = join(BASE_URI, TEAM_NAME);

	// this is worthless
//	public static Map<Class<?>, Class<?>> classToRecordMap = Map.ofEntries(
//			entry(Company.class, Company.ResponseRecord.class),
//			entry(JobPosting.class, JobPosting.ResponseRecord.class),
//			entry(IdentifiableObjectManager.class, IdentifiableObjectManager.ResponseRecord.class),
//			entry(Link.class, Link.ResponseRecord.class), entry(Page.class, Page.ResponseRecord.class),
//			entry(Person.class, Person.ResponseRecord.class), entry(Project.class, Project.ResponseRecord.class),
//			entry(Skill.class, Skill.ResponseRecord.class),
//			entry(SkillProficiency.class, SkillProficiency.ResponseRecord.class),
//			entry(WorkExperience.class, WorkExperience.ResponseRecord.class));

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
	
//	public static Class<?> classToRecord(Class<?> target)
//	{
//		return classToRecordMap.get(target);
//	}

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

	public static JsonNode retrieve(int id, String resourceName)
	{
		RestClient client = RestClient.create();

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
	
	public static boolean store(Identifiable obj, Class<?> targetClass, String resourceName, String resourceDesc)
	{
		RestClient client = RestClient.create();
		if (!RestUtilities.doesResourceExist(resourceName))
		{ // need to create the thing!
			RestUtilities.createResource(resourceName, resourceDesc);
		}
		ResponseObject result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, resourceName, String.valueOf(obj.getId()))).body(targetClass.cast(obj))
				.retrieve().body(ResponseObject.class);
		return result.successful();
	}
}