package models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestUtilities;

public class Company extends User
{
	public Company()
	{
		super();
	}

	public Company(String name, String email, IdentifiableObjectManagerInterface manager)
	{
		super(name, email, manager);
		links.put("projects", new ArrayList<Link>());
		links.put("jobPostings", new ArrayList<Link>());
	}

	public void addProject(Project project)
	{
		Link newLink = new Link(project.getPage(), Link.RelationshipType.HAS_PROJECT, manager);
		int linkIndex = links.get("projects").indexOf(newLink);

		if (linkIndex != -1)
		{
			return;
		} // else

		links.get("projects").add(newLink);
	}

	public boolean removeProject(Project project)
	{
		Link target = new Link(project.getPage(), Link.RelationshipType.HAS_PROJECT, manager);
		return links.get("projects").remove(target);
	}

	public void addJobPosting(JobPosting post)
	{
		Link newLink = new Link(post.getPage(), Link.RelationshipType.HAS_OPENING, manager);
		int linkIndex = links.get("jobPostings").indexOf(newLink);

		if (linkIndex != -1)
		{
			return;
		} // else

		links.get("jobPostings").add(newLink);
	}

	public boolean removeJobPosting(JobPosting post)
	{
		Link target = new Link(post.getPage(), Link.RelationshipType.HAS_OPENING, manager);
		return links.get("recommendedJobs").remove(target);
	}

	public static record ResponseRecord(String request, boolean successful, String message, Company data) {
	}

	public static final String RESOURCE = "companies";
	public static final String RESOURCE_DESC = "All the companies on Branched Out.";

	public static Company retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), Company.class);
		} catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<Company> retrieveAll()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Company> list = new ArrayList<Company>();
		List<JsonNode> nodes = RestUtilities.retrieveAll(RESOURCE);
		try
		{
			for (JsonNode n : nodes)
			{
				System.out.println(n);
				list.add(mapper.treeToValue(n, Company.class));
			}
		} catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean store()
	{
		return RestUtilities.store(this, Company.class, RESOURCE, RESOURCE_DESC);
	}

	@Override
	public String toString()
	{
		return "Company [name=" + name + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", avatarURL="
				+ avatarURL + ", bannerURL=" + bannerURL + ", id=" + id + ", links=" + links + ", externalWebLinks="
				+ externalWebLinks + "]";
	}

}
