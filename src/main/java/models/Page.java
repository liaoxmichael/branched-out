package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestUtilities;
import models.rest.RestReadyInterface;

public class Page implements Identifiable, RestReadyInterface // should be abstract; made concrete for testing
{
	int id;
	@JsonIgnore
	protected Entity entity;
	int entityId;
	@JsonIgnore
	protected List<User> editors;
	protected List<Integer> editorIds;
	@JsonIgnore
	protected List<User> blockedViewers;
	protected List<Integer> blockedViewerIds;
	protected IdentifiableObjectManagerInterface manager;

	public Page()
	{
	}

	public Page(Entity entity, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		manager.register(this);

		this.entity = entity;
		editors = new ArrayList<User>();
		editorIds = new ArrayList<Integer>();
		blockedViewers = new ArrayList<User>();
		blockedViewerIds = new ArrayList<Integer>();

		this.manager = manager;
	}

	@Override
	public int getId()
	{
		return id;
	}

	public static record ResponseRecord(String request, boolean successful, String message, Page data) {
	}

	public static final String RESOURCE = "pages";
	public static final String RESOURCE_DESC = "All the pages in Branched Out.";

	public static Page retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			// need to fill in Entity, Editors, Viewers
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), Page.class);
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
	
	public static List<Page> retrieveAll()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Page> list = new ArrayList<Page>();
		List<JsonNode> nodes = RestUtilities.retrieveAll(RESOURCE);
		try
		{
			for (JsonNode n : nodes)
			{
				System.out.println(n);
				list.add(mapper.treeToValue(n, Page.class));
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
		return RestUtilities.store(this, Page.class, RESOURCE, RESOURCE_DESC);
	}

	public void addEditor(User user)
	{
		int index = editors.indexOf(user);

		if (index != -1) // if already an editor: early termination
		{
			return;
		} // else
		editors.add(user);
		editorIds.add(user.getId());
	}

	public boolean removeEditor(User user)
	{

		return editors.remove(user) && editorIds.remove(Integer.valueOf(user.getId()));
	}

	public void blockViewer(User user)
	{
		int index = blockedViewers.indexOf(user);

		if (index != -1) // if already blocked: early termination
		{
			return;
		} // else

		blockedViewers.add(user);
		blockedViewerIds.add(user.getId());
	}

	public boolean unblockViewer(User user)
	{
		return blockedViewers.remove(user) && blockedViewerIds.remove(Integer.valueOf(user.getId()));
	}

	/**
	 * @return whether or not the specified user has edit permissions
	 */
	public boolean canEdit(User user)
	{
		boolean found = false;
		for (User u : editors)
		{
			if (user.equals(u))
			{
				found = true;
				break;
			}
		}
		return found;
		// return canEdit.contains(user);
	}

	/**
	 * @return whether or not the specified user is blocked from viewing
	 */
	public boolean cantView(User user)
	{
		return blockedViewers.contains(user);
	}

	/**
	 * @return the entityId
	 */
	public int getEntityId()
	{
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(int entityId)
	{
		this.entityId = entityId;
	}

	/**
	 * @return the editorIds
	 */
	public List<Integer> getEditorIds()
	{
		return editorIds;
	}

	/**
	 * @param editorIds the editorIds to set
	 */
	public void setEditorIds(List<Integer> editorIds)
	{
		List<User> newSet = new ArrayList<User>();
		for (Integer i : editorIds)
		{
			if (Person.retrieve(i) != null)
			{
				newSet.add(Person.retrieve(i));
			} else if (Company.retrieve(i) != null)
			{
				newSet.add(Company.retrieve(i));
			}
		}
		this.editors = newSet;

		this.editorIds = editorIds;
	}

	/**
	 * @return the blockedViewerIds
	 */
	public List<Integer> getBlockedViewerIds()
	{
		return blockedViewerIds;
	}

	/**
	 * @param blockedViewerIds the blockedViewerIds to set
	 */
	public void setBlockedViewerIds(List<Integer> blockedViewerIds)
	{
		List<User> newSet = new ArrayList<User>();
		for (Integer i : blockedViewerIds)
		{
			if (Person.retrieve(i) != null)
			{
				newSet.add(Person.retrieve(i));
			} else if (Company.retrieve(i) != null)
			{
				newSet.add(Company.retrieve(i));
			}
		}
		this.blockedViewers = newSet;

		this.blockedViewerIds = blockedViewerIds;
	}

	/**
	 * @return the editors
	 */
	public List<User> getEditors()
	{
		return editors;
	}

	/**
	 * @param editors the editors to set
	 */
	public void setEditors(List<User> editors)
	{
		List<Integer> newIds = new ArrayList<Integer>();
		for (User u : editors)
		{
			newIds.add(u.getId());
		}
		this.editorIds = newIds;

		this.editors = editors;

	}

	/**
	 * @return the blockedViewers
	 */
	public List<User> getBlockedViewers()
	{
		return blockedViewers;
	}

	/**
	 * @param blockedViewers the blockedViewers to set
	 */
	public void setBlockedViewers(List<User> blockedViewers)
	{
		List<Integer> newIds = new ArrayList<Integer>();
		for (User u : blockedViewers)
		{
			newIds.add(u.getId());
		}
		this.blockedViewerIds = newIds;

		this.blockedViewers = blockedViewers;
	}

	/**
	 * @return the entity
	 */
	public Entity getEntity()
	{
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
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
		Page other = (Page) obj;
		return id == other.id;
	}

	@Override
	public String toString()
	{
		return "Page [id=" + id + ", entity=" + entity + ", entityId=" + entityId + ", editors=" + editors
				+ ", editorIds=" + editorIds + ", blockedViewers=" + blockedViewers + ", blockedViewerIds="
				+ blockedViewerIds + ", manager=" + manager + "]";
	}

}
