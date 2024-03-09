
public class SkillProficiency extends IdentifiableObject
{
	
	enum ProficiencyLevel {
		BEGINNER,
		INTERMEDIATE,
		ADVANCED
	}
	
	ProficiencyLevel level;
	Link skill;

	public SkillProficiency(int id)
	{
		super(id);
		// TODO Auto-generated constructor stub
	}

}
