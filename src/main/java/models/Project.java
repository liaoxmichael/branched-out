package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestUtilities;
import models.rest.RestReadyInterface;

public class Project extends Post implements RestReadyInterface
{
	public Project()
	{
		super();
	}

	public Project(String title, IdentifiableObjectManagerInterface manager)
	{
		super(title, manager);
		links.put("contributors", new ArrayList<Link>());
		links.put("coordinators", new ArrayList<Link>());
		links.put("companies", new ArrayList<Link>());
		
		store();
	}

	public void addContributor(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.CONTRIBUTOR_PERSON, manager);

		if (links.get("contributors").indexOf(newLink) != -1) // if already contributor, terminate early
		{
			return;
		} // else

		links.get("contributors") // guaranteed because of constructor; else risky and could return null
				.add(newLink);
		// it's reciprocal; need to add to person's projects list
		person.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
		update();
		person.update();
	}

	public void removeContributor(Person person)
	{
		Link target = new Link(person.getPage(), Link.RelationshipType.CONTRIBUTOR_PERSON, manager);
		links.get("contributors").remove(target); // this will return false if it's not there; no big deal.
		// it's reciprocal; need to remove from other user's list
		person.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
		person.update();
		update();
	}

	public void addCoordinator(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.COORDINATOR_PERSON, manager);

		if (links.get("coordinators").indexOf(newLink) != -1)
		{
			return;
		}

		links.get("coordinators").add(newLink);
		person.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
		person.store();
		update();
	}

	public void removeCoordinator(Person person)
	{
		Link target = new Link(person.getPage(), Link.RelationshipType.COORDINATOR_PERSON, manager);
		links.get("coordinators").remove(target);
		person.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
		person.store();
		update();
		
	}

	public void addCompany(Company company)
	{
		Link newLink = new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager);

		if (links.get("companies").indexOf(newLink) != -1)
		{
			return;
		}

		links.get("companies").add(newLink);
		company.addProject(this);
		update();
	}

	public void removeCompany(Company company)
	{
		Link target = new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager);
		links.get("companies").remove(target);
		company.removeProject(this);
		update();
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

	public static record ResponseRecord(String request, boolean successful, String message, Project data) {
	}

	public static final String RESOURCE = "projects";
	public static final String RESOURCE_DESC = "All the projects on Branched Out.";

	public static Project retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			// need to fill in page
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), Project.class);
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
	
	public static List<Project> retrieveAll()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Project> list = new ArrayList<Project>();
		List<JsonNode> nodes = RestUtilities.retrieveAll(RESOURCE);
		try
		{
			for (JsonNode n : nodes)
			{
				System.out.println(n);
				list.add(mapper.treeToValue(n, Project.class));
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
		return RestUtilities.store(this, Project.class, RESOURCE, RESOURCE_DESC);
	}
	
	@Override
	public boolean update()
	{
		return RestUtilities.update(this, Project.class, RESOURCE, RESOURCE_DESC);
	}

	@Override
	public String toString()
	{
		return "Project [title=" + title + ", description=" + description + ", id=" + id + ", links=" + links
				+ ", externalWebLinks=" + externalWebLinks + "]";
	}

}
