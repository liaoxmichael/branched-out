
import java.util.ArrayList;

public class IdentifiableObjectManager implements IdentifiableObjectManagerInterface
{
	ArrayList<Identifiable> objects = new ArrayList<Identifiable>();
	int currentID = 0;
	
	@Override
	public int getNextId()
	{
		return currentID++;
	}

	public Identifiable getById(int index)
	{
		return objects.get(index);
	}
	
	@Override
	/**
	 * @return the objects
	 */
	public ArrayList<Identifiable> getObjects()
	{
		return objects;
	}

	@Override
	/**
	 * @return the currentID
	 */
	public int getCurrentId()
	{
		return currentID;
	}

	@Override
	public void register(Identifiable object)
	{
		objects.add(object);
	}

}
