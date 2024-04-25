package models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestUtilities;
import models.rest.RestReadyInterface;

public class Skill extends Post implements RestReadyInterface
{

	public Skill()
	{
		super();
	}

	public Skill(String title, IdentifiableObjectManagerInterface manager)
	{
		super(title, manager);
		links.put("mentors", new ArrayList<Link>());
	}

	public void addMentor(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.MENTOR_PERSON, manager);
		if (links.get("mentors").indexOf(newLink) != -1) // if already mentor, terminate early
		{
			return;
		} // else

		links.get("mentors") // guaranteed because of constructor; else risky and could return null
				.add(newLink);
	}

	public boolean removeMentor(Person person)
	{
		return links.get("mentors").remove(new Link(person.getPage(), Link.RelationshipType.MENTOR_PERSON, manager));
	}

	public static record ResponseRecord(String request, boolean successful, String message, Skill data) {
	}

	public static final String RESOURCE = "skills";
	public static final String RESOURCE_DESC = "All the skills on Branched Out";

	public static Skill retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), Skill.class);
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
	
	public static List<Skill> retrieveAll()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Skill> list = new ArrayList<Skill>();
		List<JsonNode> nodes = RestUtilities.retrieveAll(RESOURCE);
		try
		{
			for (JsonNode n : nodes)
			{
				System.out.println(n);
				list.add(mapper.treeToValue(n, Skill.class));
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
		return RestUtilities.store(this, Skill.class, RESOURCE, RESOURCE_DESC);
	}

	@Override
	public String toString()
	{
		return "Skill [title=" + title + ", description=" + description + ", id=" + id + ", links=" + links
				+ ", externalWebLinks=" + externalWebLinks + "]";
	}

}
