
public class RecommendBySitePreference implements RecommendationStrategy
{

	@Override
	public boolean check(Person person, JobPosting job)
	{
		return person.getJobSitePreferences().contains(job.getSite());
	}

}
