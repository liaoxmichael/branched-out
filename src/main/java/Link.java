
import java.util.Objects;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Link implements Identifiable
{

	int id;
	@JsonIgnore
	Page page;
	int pageId;

	enum RelationshipType {
		HAS_SKILL, REQUIRES_SKILL,

		HAS_PROJECT,

		FOLLOWING_USER, FOLLOWER_USER, MENTOR_PERSON, CONTRIBUTOR_PERSON, COORDINATOR_PERSON, JOB_APPLICANT_PERSON,

		FROM_COMPANY,
	}

	RelationshipType relation;

	public Link()
	{
	}

	public Link(Page page, RelationshipType type, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.page = page;
		pageId = this.page.getId();
		relation = type;

		manager.register(this);
	}

	public Link(int pageId, RelationshipType type, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.pageId = pageId;
		relation = type;

		manager.register(this);
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * @return the pageId
	 */
	public int getPageId()
	{
		return pageId;
	}

	/**
	 * @param pageId the pageId to set
	 */
	public void setPageId(int pageId)
	{
		this.pageId = pageId;
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
		ResponseObject result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(ResponseObject.class);
		return result.successful();
	}

	/**
	 * @return the page
	 */
	public Page getPage()
	{
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Page page)
	{
		this.page = page;
	}

	/**
	 * @return the relation
	 */
	public RelationshipType getRelation()
	{
		return relation;
	}

	/**
	 * @param relation the relation to set
	 */
	public void setRelation(RelationshipType relation)
	{
		this.relation = relation;
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
		return pageId == other.pageId && relation == other.relation;
	}

	@Override
	public String toString()
	{
		return "Link [id=" + id + ", page=" + page + ", relation=" + relation + "]";
	}

}
