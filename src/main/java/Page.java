
import java.util.ArrayList;
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
	protected ArrayList<User> canEdit;
	protected ArrayList<Integer> canEditIds;
	@JsonIgnore
	protected ArrayList<User> cantView;
	protected ArrayList<Integer> cantViewIds;
	@JsonIgnore
	protected IdentifiableObjectManagerInterface manager;

	public Page()
	{
	}

	public Page(Entity entity, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.entity = entity;
		canEdit = new ArrayList<User>();
		canEditIds = new ArrayList<Integer>();
		cantView = new ArrayList<User>();
		cantViewIds = new ArrayList<Integer>();

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
		canEdit.add(user);
		canEditIds.add(user.getId());
	}

	public boolean removeEditor(User user)
	{

		return canEdit.remove(user) && canEditIds.remove(Integer.valueOf(user.getId()));
	}

	public void blockViewer(User user)
	{
		cantView.add(user);
		cantViewIds.add(user.getId());
	}

	public boolean unblockViewer(User user)
	{
		return cantView.remove(user) && cantViewIds.remove(Integer.valueOf(user.getId()));
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
	public ArrayList<Integer> getCanEditIds()
	{
		return canEditIds;
	}

	/**
	 * @param canEditIds the canEditIds to set
	 */
	public void setCanEditIds(ArrayList<Integer> canEditIds)
	{
		ArrayList<User> newList = new ArrayList<User>();
		for (Integer i : canEditIds)
		{
			newList.add((User) manager.getById(i));
		}
		this.canEdit = newList;

		this.canEditIds = canEditIds;
	}

	/**
	 * @return the cantViewIds
	 */
	public ArrayList<Integer> getCantViewIds()
	{
		return cantViewIds;
	}

	/**
	 * @param cantViewIds the cantViewIds to set
	 */
	public void setCantViewIds(ArrayList<Integer> cantViewIds)
	{
		ArrayList<User> newList = new ArrayList<User>();
		for (Integer i : canEditIds)
		{
			newList.add((User) manager.getById(i));
		}
		this.cantView = newList;

		this.cantViewIds = cantViewIds;
	}

	/**
	 * @return whether or not the specified user has edit permissions
	 */
	public boolean canEdit(User user)
	{
		return canEdit.contains(user);
	}

	/**
	 * @return whether or not the specified user is blocked from viewing
	 */
	public boolean cantView(User user)
	{
		return cantView.contains(user);
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

	/**
	 * @return the canEdit
	 */
	public ArrayList<User> getCanEdit()
	{
		return canEdit;
	}

	/**
	 * @param canEdit the canEdit to set
	 */
	public void setCanEdit(ArrayList<User> canEdit)
	{
		ArrayList<Integer> newIds = new ArrayList<Integer>();
		for (User u : canEdit)
		{
			newIds.add(u.getId());
		}
		this.canEditIds = newIds;

		this.canEdit = canEdit;

	}

	/**
	 * @return the cantView
	 */
	public ArrayList<User> getCantView()
	{
		return cantView;
	}

	/**
	 * @param cantView the cantView to set
	 */
	public void setCantView(ArrayList<User> cantView)
	{
		ArrayList<Integer> newIds = new ArrayList<Integer>();
		for (User u : canEdit)
		{
			newIds.add(u.getId());
		}
		this.cantViewIds = newIds;

		this.cantView = cantView;
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
		return "Page [id=" + id + ", entity=" + entity + ", canEdit=" + canEdit + ", cantView=" + cantView + "]";
	}

}
