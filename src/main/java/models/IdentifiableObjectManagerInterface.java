package models;

import java.util.List;

public interface IdentifiableObjectManagerInterface
{
	public int nextId();

	public int getCurrentId();

	public Identifiable getById(int index);

	public void register(Identifiable object);

	public List<Identifiable> getObjects();
}
