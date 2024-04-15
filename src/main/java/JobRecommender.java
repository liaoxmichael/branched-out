import java.util.ArrayList;
import java.util.List;

public class JobRecommender implements JobRecommenderInterface
{

	List<Person> openToWork;

	public JobRecommender()
	{
		openToWork = new ArrayList<Person>();
	}

	@Override
	public void recommend(JobPosting job)
	{
		for (Person p : openToWork)
		{
			if (job.getStrategy().check(p, job))
			{
				p.addJobPosting(job);
			}
		}
	}

	@Override
	public void registerHireable(Person person)
	{
		if (!openToWork.contains(person))
		{
			openToWork.add(person);
		}
	}

	@Override
	public boolean unregisterHireable(Person person)
	{
		return openToWork.remove(person);
	}

}
