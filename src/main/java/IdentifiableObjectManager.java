import java.util.ArrayList;

public enum IdentifiableObjectManager {
	INSTANCE;

	ArrayList<Identifiable> objects = new ArrayList<Identifiable>();
	int currentID = 0;

	public int getNextID()
	{
		return currentID++;
	}

	public Identifiable getByID(int index)
	{
		return objects.get(index);
	}
}
