package models.recommender;

import models.JobPosting;
import models.Person;
import models.SkillProficiency;

public class RecommendBySkill implements RecommendationStrategy
{

	@Override
	public boolean check(Person person, JobPosting job)
	{
		// trivial to add threshold if wanted later
		for (SkillProficiency s : job.getRequiredSkills())
		{
			int skillIndex = person.getSkills().indexOf(s);
			if ((skillIndex == -1) || (person.getSkills().get(skillIndex).getLevel().compareTo(s.getLevel()) < 0)) {
				// either person does not have skill OR too low in the skill
				return false;
			}
			
//			if ((skillIndex != -1) && (person.getSkills().get(skillIndex).getLevel().compareTo(s.getLevel()) >= 0))
//			{
//				continue;
//			}
		}

		return true;
	}

}
