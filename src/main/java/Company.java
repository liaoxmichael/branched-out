
import java.util.ArrayList;

import org.springframework.web.client.RestClient;

public class Company extends User implements Storable
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

		links.get("jobPostings").add(newLink);
	}

	public boolean removeProject(Project project)
	{
		Link target = new Link(project.getPage(), Link.RelationshipType.HAS_PROJECT, manager);
		return links.get("recommendedJobs").remove(target);
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

	public record CompanyResponse(String request, boolean successful, String message, Company data) {
	}

	public static final String RESOURCE = "companies";
	public static final String RESOURCE_DESC = "All the companies on Branched Out.";

	public static Company retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			CompanyResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(CompanyResponse.class);

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

	@Override
	public String toString()
	{
		return "Company [name=" + name + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", avatarURL="
				+ avatarURL + ", bannerURL=" + bannerURL + ", id=" + id + ", links=" + links + ", externalWebLinks="
				+ externalWebLinks + "]";
	}

}
