package models.recommender;

import models.JobPosting;
import models.Person;

public interface RecommendationStrategy
{
	public boolean check(Person person, JobPosting job);
}
