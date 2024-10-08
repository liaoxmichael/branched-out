import java.util.ArrayList;
import java.util.Objects;

public class Page implements Identifiable // should be abstract; made concrete for testing
{

	int id;
	private Entity entity;
	private ArrayList<User> canEdit;
	private ArrayList<User> cantView;
	protected IdentifiableObjectManager manager;

	public Page(Entity entity, IdentifiableObjectManager manager)
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
	public boolean canEdit(User user) {
		return canEdit.contains(user);
	}
	
	/**
	 * @return whether or not the specified user is blocked from viewing
	 */
	public boolean cantView(User user) {
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
	 * @return the cantView
	 */
	public ArrayList<User> getCantView()
	{
		return cantView;
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
