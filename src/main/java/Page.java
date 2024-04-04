
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.web.client.RestClient;

public class Page implements Identifiable // should be abstract; made concrete for testing
{

	int id;
	private Entity entity;
	private ArrayList<User> canEdit;
	private ArrayList<User> cantView;
	protected IdentifiableObjectManagerInterface manager;

	public Page(Entity entity, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.entity = entity;
		canEdit = new ArrayList<User>();
		cantView = new ArrayList<User>();

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

	public boolean store()
	{
		RestClient client = RestClient.create();
		if (!RestUtilities.doesResourceExist(RESOURCE))
		{ // need to create the thing!
			RestUtilities.createResource(RESOURCE, RESOURCE_DESC);
		}
		PageResponse result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(PageResponse.class);
		return result.successful;
	}

	public void addEditor(User user)
	{
		canEdit.add(user);
	}

	public boolean removeEditor(User user)
	{
		return canEdit.remove(user);
	}

	public void blockViewer(User user)
	{
		cantView.add(user);
	}

	public boolean unblockViewer(User user)
	{
		return cantView.remove(user);
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
