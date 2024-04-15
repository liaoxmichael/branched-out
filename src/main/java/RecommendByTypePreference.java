
public class RecommendByTypePreference implements RecommendationStrategy
{

	@Override
	public boolean check(Person person, JobPosting job)
	{
		return person.getJobTypePreferences().contains(job.getType());
	}

}
