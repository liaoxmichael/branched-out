package models.recommender;

import models.JobPosting;
import models.Person;

public interface JobRecommenderInterface
{
	public void recommend(JobPosting job);
	
	public void registerHireable(Person person);
	
	public boolean unregisterHireable(Person person);
}
