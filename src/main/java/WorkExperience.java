//import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WorkExperience implements Identifiable, Storable
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
		links.put("companies", new ArrayList<Link>());
		links.get("companies").add(new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager));

		manager.register(this); // registering with the manager
		this.manager = manager;
	}

	@Override
	public int getId()
	{
		return id;
	}

	public record WorkExperienceResponse(String request, boolean successful, String message, WorkExperience data) {
	}

	public static final String RESOURCE = "jobs";
	public static final String RESOURCE_DESC = "All the previously-held work experiences users have had in Branched Out";

	public static WorkExperience retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			WorkExperienceResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(WorkExperienceResponse.class);

			return response.data;
		}
		// else
		return null;
	}

	@Override
	public boolean store()
	{
		RestClient client = RestClient.create();
		if (!RestUtilities.doesResourceExist(RESOURCE))
		{ // need to create the thing!
			RestUtilities.createResource(RESOURCE, RESOURCE_DESC);
		}
		ResponseObject result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(ResponseObject.class);
		return result.successful();
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
