package models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestReadyInterface;
import models.rest.RestUtilities;

public class IdentifiableObjectManager implements Identifiable, IdentifiableObjectManagerInterface, RestReadyInterface
{
	@JsonIgnore
	List<Identifiable> objects;
	int currentId = 1;
	int id = 0; // this is the object manager's id, FIXED
	
	public IdentifiableObjectManager()
	{
		objects = new ArrayList<Identifiable>(); // may be able to nix this entirely with involvement of server
		register(this);
		store();
	}

	@Override
	public int nextId()
	{
		int assignedId = currentId; 
		currentId++;
		update();
		return assignedId;
	}

	public Identifiable getById(int index)
	{
		return objects.get(index);
	}
	
	@Override
	/**
	 * @return the objects
	 */
	public List<Identifiable> getObjects()
	{
		return objects;
	}

	@Override
	/**
	 * @return the currentID
	 */
	public int getCurrentId()
	{
		return currentId;
	}
	
	@Override
	public void register(Identifiable object)
	{
		objects.add(object);
	}
	
	public static record ResponseRecord(String request, boolean successful, String message, IdentifiableObjectManager data) {
	}

	public static final String RESOURCE = "identifiableObjectManager";
	public static final String RESOURCE_DESC = "The ID manager for Branched Out";

	public static IdentifiableObjectManager retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			// need to fill objects back in
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), IdentifiableObjectManager.class);
		} catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean store()
	{
		return RestUtilities.store(this, IdentifiableObjectManager.class, RESOURCE, RESOURCE_DESC);
	}
	
	@Override
	public boolean update()
	{
		return RestUtilities.update(this, IdentifiableObjectManager.class, RESOURCE, RESOURCE_DESC);
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * @param objects the objects to set
	 */
	public void setObjects(List<Identifiable> objects)
	{
		this.objects = objects;
	}

	/**
	 * @param currentId the currentId to set
	 */
	public void setCurrentId(int currentId)
	{
		this.currentId = currentId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return "IdentifiableObjectManager [currentId=" + currentId + ", id=" + id + "]";
	}
	
	

}
