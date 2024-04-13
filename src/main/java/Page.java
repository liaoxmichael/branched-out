
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Page implements Identifiable, Storable // should be abstract; made concrete for testing
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
	@JsonIgnore
	protected IdentifiableObjectManagerInterface manager;

	public Page()
	{
	}

	public Page(Entity entity, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.entity = entity;
		editors = new ArrayList<User>();
		editorIds = new ArrayList<Integer>();
		blockedViewers = new ArrayList<User>();
		blockedViewerIds = new ArrayList<Integer>();

		manager.register(this);
		this.manager = manager;
	}

	@Override
	public int getId()
	{
		return id;
	}

	public record PageResponse(String request, boolean successful, String message, Page data) {
	}

	public static final String RESOURCE = "pages";
	public static final String RESOURCE_DESC = "All the pages in Branched Out.";

	public static Page retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			PageResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(PageResponse.class);

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
	 * @return the canEditIds
	 */
	public List<Integer> getCanEditIds()
	{
		return editorIds;
	}

	/**
	 * @param canEditIds the canEditIds to set
	 */
	public void setCanEditIds(List<Integer> canEditIds)
	{
		List<User> newSet = new ArrayList<User>();
		for (Integer i : canEditIds)
		{
			newSet.add((User) manager.getById(i));
		}
		this.editors = newSet;

		this.editorIds = canEditIds;
	}

	/**
	 * @return the cantViewIds
	 */
	public List<Integer> getCantViewIds()
	{
		return blockedViewerIds;
	}

	/**
	 * @param cantViewIds the cantViewIds to set
	 */
	public void setCantViewIds(List<Integer> cantViewIds)
	{
		List<User> newSet = new ArrayList<User>();
		for (Integer i : editorIds)
		{
			newSet.add((User) manager.getById(i));
		}
		this.blockedViewers = newSet;

		this.blockedViewerIds = cantViewIds;
	}

	/**
	 * @return the canEdit
	 */
	public List<User> getCanEdit()
	{
		return editors;
	}

	/**
	 * @param canEdit the canEdit to set
	 */
	public void setCanEdit(List<User> canEdit)
	{
		List<Integer> newIds = new ArrayList<Integer>();
		for (User u : canEdit)
		{
			newIds.add(u.getId());
		}
		this.editorIds = newIds;

		this.editors = canEdit;

	}

	/**
	 * @return the cantView
	 */
	public List<User> getCantView()
	{
		return blockedViewers;
	}

	/**
	 * @param cantView the cantView to set
	 */
	public void setCantView(List<User> cantView)
	{
		List<Integer> newIds = new ArrayList<Integer>();
		for (User u : editors)
		{
			newIds.add(u.getId());
		}
		this.blockedViewerIds = newIds;

		this.blockedViewers = cantView;
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
		return "Page [id=" + id + ", entity=" + entity + ", canEdit=" + editors + ", cantView=" + blockedViewers + "]";
	}

}
