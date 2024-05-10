package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestReadyInterface;
import models.rest.RestUtilities;

public class Link implements Identifiable, RestReadyInterface
{

	int id;
//	@JsonIgnore
//	Page page;
	int pageId;

	public enum RelationshipType {
		HAS_SKILL, REQUIRES_SKILL,

		HAS_PROJECT,

		FOLLOWING_USER, FOLLOWER_USER, MENTOR_PERSON, CONTRIBUTOR_PERSON, COORDINATOR_PERSON, JOB_APPLICANT_PERSON,

		FROM_COMPANY,

		HAS_OPENING,

		RECOMMENDED_JOB,
	}

	RelationshipType relation;

	public Link()
	{
	}

	public Link(Page page, RelationshipType type, IdentifiableObjectManagerInterface manager)
	{
		if (manager == null) {
			manager = IdentifiableObjectManager.retrieve(0);
		}
		id = manager.nextId();
//		this.page = page;
		pageId = page.getId();
		relation = type;

		manager.register(this);
		
		store();
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

	public static record ResponseRecord(String request, boolean successful, String message, Link data) {
	}

	public static final String RESOURCE = "links";
	public static final String RESOURCE_DESC = "All the links in Branched Out.";

	public static Link retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			// need to fill back in Page
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), Link.class);
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

	public static List<Link> retrieveAll()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Link> list = new ArrayList<Link>();
		List<JsonNode> nodes = RestUtilities.retrieveAll(RESOURCE);
		try
		{
			for (JsonNode n : nodes)
			{
//				System.out.println(n);
				list.add(mapper.treeToValue(n, Link.class));
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
		return RestUtilities.store(this, Link.class, RESOURCE, RESOURCE_DESC);
	}
	
	@Override
	public boolean update()
	{
		return RestUtilities.update(this, Link.class, RESOURCE, RESOURCE_DESC);
	}

	/**
	 * @return the page
	 */
	public Page fetchPage()
	{
		return Page.retrieve(pageId);
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
		return Objects.hash(pageId, relation);
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
		return "Link [id=" + id + ", pageId=" + pageId + ", relation=" + relation + "]";
	}

}
