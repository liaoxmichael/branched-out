import java.util.ArrayList;
import java.util.Objects;

public abstract class Page implements Identifiable
{

	int id;
	Entity entity;
	ArrayList<User> canEdit;
	ArrayList<User> cantView;

	public Page(Entity entity)
	{
		id = IdentifiableObjectManager.INSTANCE.getNextID();
		this.entity = entity;
		canEdit = new ArrayList<User>();
		cantView = new ArrayList<User>();

		IdentifiableObjectManager.INSTANCE.objects.add(this); // registering with the manager
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
		return Objects.hash(canEdit, cantView, entity);
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
		return Objects.equals(canEdit, other.canEdit) && Objects.equals(cantView, other.cantView)
				&& Objects.equals(entity, other.entity);
	}

}
