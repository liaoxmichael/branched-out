
import java.util.Objects;

import org.springframework.web.client.RestClient;

public class Link implements Identifiable
{

	int id;
	Page page;

	enum RelationshipType {
		HAS_SKILL, REQUIRES_SKILL,

		HAS_PROJECT,

		FOLLOWING_USER, FOLLOWER_USER, MENTOR_PERSON, CONTRIBUTOR_PERSON, COORDINATOR_PERSON, JOB_APPLICANT_PERSON,

		FROM_COMPANY,
	}

	RelationshipType relation;

	public Link(Page page, RelationshipType type, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.page = page;
		relation = type;

		manager.register(this);
	}

	@Override
	public int getId()
	{
		return id;
	}

	public record LinkResponse(String request, boolean successful, String message, Link data) {
	}

	public static final String RESOURCE = "links";
	public static final String RESOURCE_DESC = "All the links in Branched Out.";

	public static Link retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			LinkResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(LinkResponse.class);

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
		LinkResponse result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(LinkResponse.class);
		return result.successful;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(page, relation);
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
		Link other = (Link) obj;
		return Objects.equals(page, other.page) && relation == other.relation;
	}

	@Override
	public String toString()
	{
		return "Link [id=" + id + ", page=" + page + ", relation=" + relation + "]";
	}

}
