package models.recommender;

import models.JobPosting;
import models.Person;

public class RecommendAll implements RecommendationStrategy
{

	@Override
	public boolean check(Person person, JobPosting job)
	{
		return true;
	}

}
