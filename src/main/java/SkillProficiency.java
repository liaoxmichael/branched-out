
import java.util.Objects;

import org.springframework.web.client.RestClient;

public class SkillProficiency implements Identifiable
{

	int id;
	protected IdentifiableObjectManagerInterface manager;

	enum ProficiencyLevel {
		BEGINNER, INTERMEDIATE, ADVANCED
	}

	ProficiencyLevel level;
	Skill skill;

	public SkillProficiency(Skill skill, ProficiencyLevel level, IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		this.skill = skill;
		this.level = level;
		manager.register(this); // registering with the manager
		this.manager = manager;
	}

	@Override
	public int getId()
	{
		return id;
	}

	public record SkillProficiencyResponse(String request, boolean successful, String message, SkillProficiency data) {
	}

	public static final String RESOURCE = "proficiencies";
	public static final String RESOURCE_DESC = "All the skill proficiencies users have on Branched Out";

	public static SkillProficiency retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			SkillProficiencyResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(SkillProficiencyResponse.class);

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
		SkillProficiencyResponse result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(SkillProficiencyResponse.class);
		return result.successful;
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
		return "SkillProficiency [id=" + id + ", level=" + level + ", skill=" + skill + "]";
	}

}
