import java.util.Objects;

public class SkillProficiency implements Identifiable
{

	int id;
	protected IdentifiableObjectManager manager;

	enum ProficiencyLevel {
		BEGINNER, INTERMEDIATE, ADVANCED
	}

	ProficiencyLevel level;
	Skill skill;

	public SkillProficiency(Skill skill, ProficiencyLevel level, IdentifiableObjectManager manager)
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
