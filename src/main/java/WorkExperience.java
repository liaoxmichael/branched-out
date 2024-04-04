//import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

import org.springframework.web.client.RestClient;

public class WorkExperience implements Identifiable
{

	int id;
	Hashtable<String, ArrayList<Link>> links;

	String title;
	String description;

	protected IdentifiableObjectManagerInterface manager;

//	LocalDate startDate;
//	LocalDate endDate;

	public WorkExperience(String title, String description, Company company, IdentifiableObjectManagerInterface manager) // if
																															// gets
																															// more
																															// complex,
																															// consider
																															// using
	// factory pattern?
	{
		id = manager.getNextId();
		this.title = title;
		this.description = description;
		links = new Hashtable<String, ArrayList<Link>>();
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

	public boolean store()
	{
		RestClient client = RestClient.create();
		if (!RestUtilities.doesResourceExist(RESOURCE))
		{ // need to create the thing!
			RestUtilities.createResource(RESOURCE, RESOURCE_DESC);
		}
		WorkExperienceResponse result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(WorkExperienceResponse.class);
		return result.successful;
	}

	/**
	 * @return the links
	 */
	public Hashtable<String, ArrayList<Link>> getLinks()
	{
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(Hashtable<String, ArrayList<Link>> links)
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
