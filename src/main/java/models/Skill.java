package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestUtilities;
import models.adapters.Displayable;
import models.rest.RestReadyInterface;

public class Skill extends Post implements RestReadyInterface, Displayable
{

	public Skill()
	{
		super();
	}

	public Skill(String title, IdentifiableObjectManagerInterface manager)
	{
		super(title, manager);
		links.put("mentors", new ArrayList<Link>());

		store();
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

		update();
	}

	public boolean removeMentor(Person person)
	{
		boolean result = links.get("mentors")
				.remove(new Link(person.getPage(), Link.RelationshipType.MENTOR_PERSON, manager));
		update();
		return result;
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
//				System.out.println(n);
				// need to fill in page
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
	public void setTitle(String title)
	{
		super.setTitle(title);
		update();
	}
	
	@Override
	public void setDescription(String description)
	{
		super.setDescription(description);
		update();
	}
	
	@Override
	public void addExternalWebLink(String link)
	{
		super.addExternalWebLink(link);
		update();
	}

	@Override
	public boolean removeExternalWebLink(String link)
	{
		boolean result = super.removeExternalWebLink(link);
		update();
		return result;
	}

	@Override
	public void setPage(Page page)
	{
		super.setPage(page);
		update();
	}

	@Override
	public void setPageId(int pageId)
	{
		super.setPageId(pageId);
		update();
	}

	@Override
	public void setId(int id)
	{
		super.setId(id);
		update();
	}

	@Override
	public void setLinks(Map<String, List<Link>> links)
	{
		super.setLinks(links);
		update();
	}

	@Override
	public void setExternalWebLinks(List<String> externalWebLinks)
	{
		super.setExternalWebLinks(externalWebLinks);
		update();
	}

	@Override
	public boolean store()
	{
		return RestUtilities.store(this, Skill.class, RESOURCE, RESOURCE_DESC);
	}
	
	@Override
	public boolean update()
	{
		return RestUtilities.update(this, Skill.class, RESOURCE, RESOURCE_DESC);
	}

	@Override
	public String toString()
	{
		return title;
	}

}
