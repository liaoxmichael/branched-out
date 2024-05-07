package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.rest.RestUtilities;
import models.adapters.Displayable;
import models.rest.RestReadyInterface;

public class SkillProficiency implements Identifiable, RestReadyInterface, Displayable
{

	int id;
	protected IdentifiableObjectManagerInterface manager;

	public enum ProficiencyLevel {
		BEGINNER("Beginner"), INTERMEDIATE("Intermediate"), ADVANCED("Advanced");

		public final String label;

		ProficiencyLevel(String label)
		{
			this.label = label;
		}
	}

	ProficiencyLevel level;
	Skill skill;

	public SkillProficiency()
	{
	}

	public SkillProficiency(Skill skill, ProficiencyLevel level, IdentifiableObjectManagerInterface manager)
	{
		id = manager.nextId();
		this.skill = skill;
		this.level = level;
		manager.register(this); // registering with the manager
		this.manager = manager;
		
		store();
	}

	@Override
	public int getId()
	{
		return id;
	}

	public static record ResponseRecord(String request, boolean successful, String message, SkillProficiency data) {
	}

	public static final String RESOURCE = "proficiencies";
	public static final String RESOURCE_DESC = "All the skill proficiencies users have on Branched Out";

	public static SkillProficiency retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), SkillProficiency.class);
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

	public static List<SkillProficiency> retrieveAll()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<SkillProficiency> list = new ArrayList<SkillProficiency>();
		List<JsonNode> nodes = RestUtilities.retrieveAll(RESOURCE);
		try
		{
			for (JsonNode n : nodes)
			{
				System.out.println(n);
				list.add(mapper.treeToValue(n, SkillProficiency.class));
			}
		} catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean store()
	{
		return RestUtilities.store(this, SkillProficiency.class, RESOURCE, RESOURCE_DESC);
	}
	
	@Override
	public boolean update()
	{
		return RestUtilities.update(this, SkillProficiency.class, RESOURCE, RESOURCE_DESC);
	}

	/**
	 * @return the level
	 */
	public ProficiencyLevel getLevel()
	{
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(ProficiencyLevel level)
	{
		this.level = level;
		update();
	}

	/**
	 * @return the skill
	 */
	public Skill getSkill()
	{
		return skill;
	}

	/**
	 * @param skill the skill to set
	 */
	public void setSkill(Skill skill)
	{
		this.skill = skill;
		update();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(level, skill);
	}

	@Override
	public boolean equals(Object obj) // if same skill, proficiency level does not matter
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillProficiency other = (SkillProficiency) obj;
		return Objects.equals(skill, other.skill);
	}

	@Override
	public String toString()
	{
		return level.label + " " + skill;
	}

}
