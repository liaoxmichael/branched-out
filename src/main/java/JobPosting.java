import java.util.ArrayList;
import java.util.Arrays;

public class JobPosting extends Post
{
	// need to track date posted somehow?
	String location;

	public JobPosting()
	{
	}

	/**
	 * @param title
	 * @param manager
	 */
	public JobPosting(String title, Company company, IdentifiableObjectManagerInterface manager)
	{
		super(title, manager);
		// initializing with a single-item array to match type but fix size
		links.put("company",
				Arrays.asList(new Link[] { new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager) }));
		links.put("applicants", new ArrayList<Link>());
	}

}
