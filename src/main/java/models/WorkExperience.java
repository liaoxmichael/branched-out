//import java.time.LocalDate;
package models;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestUtilities;
import models.rest.RestReadyInterface;

public class WorkExperience implements Identifiable, RestReadyInterface
{

	int id;
	Map<String, List<Link>> links;

	String title;
	String description;

	@JsonIgnore
	protected IdentifiableObjectManagerInterface manager;

//	LocalDate startDate;
//	LocalDate endDate;

	public WorkExperience()
	{
	}

	public WorkExperience(String title, String description, Company company, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.title = title;
		this.description = description;
		links = new Hashtable<String, List<Link>>();
		links.put("company",
				Arrays.asList(new Link[] { new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager) }));

		manager.register(this); // registering with the manager
		this.manager = manager;
	}

	@Override
	public int getId()
	{
		return id;
	}

	public static record ResponseRecord(String request, boolean successful, String message, WorkExperience data) {
	}

	public static final String RESOURCE = "jobs";
	public static final String RESOURCE_DESC = "All the previously-held work experiences users have had in Branched Out";

	public static WorkExperience retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), WorkExperience.class);
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

	@Override
	public boolean store()
	{
		return RestUtilities.store(this, WorkExperience.class, RESOURCE, RESOURCE_DESC);
	}

	/**
	 * @return the links
	 */
	public Map<String, List<Link>> getLinks()
	{
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(Map<String, List<Link>> links)
	{
		this.links = links;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(description, links, title);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkExperience other = (WorkExperience) obj;
		return Objects.equals(description, other.description) && Objects.equals(links, other.links)
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString()
	{
		return "WorkExperience [id=" + id + ", links=" + links + ", title=" + title + ", description=" + description
				+ "]";
	}

}
