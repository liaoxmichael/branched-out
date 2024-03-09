import java.util.ArrayList;

public abstract class Page extends IdentifiableObject
{
	
	Entity entity;
	ArrayList<User> canEdit;
	ArrayList<User> cantView;
	
	public Page(int id, Entity entity)
	{
		super(id);
		this.entity = entity;
		canEdit = new ArrayList<User>();
		cantView = new ArrayList<User>();
	}
	
	public void addEditor(User user) {
		canEdit.add(user);
	}
	
	public boolean delEditor(User user) {
		return canEdit.remove(user);
	}
	
	public void blockViewer(User user) {
		cantView.add(user);
	}
	
	public boolean unblockViewer(User user) {
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
	
	

}
