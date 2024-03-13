import java.util.ArrayList;

public interface IdentifiableObjectManager
{
	public int getNextId();
	
	public int getCurrentId();

	public Identifiable getById(int index);
	
	public void register(Identifiable object);
	
	public ArrayList<Identifiable> getObjects();
}
