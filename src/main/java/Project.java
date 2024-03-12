import java.util.ArrayList;

public class Project extends Post
{

	public Project(String title)
	{
		super(title);
		links.put("contributors", new ArrayList<Link>());
		links.put("coordinators", new ArrayList<Link>());
		links.put("comapnies", new ArrayList<Link>());
	}

	public void addContributor(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.CONTRIBUTOR_PERSON);

		if (links.get("contributors").indexOf(newLink) != -1) // if already contributor, terminate early
		{
			return;
		} // else
		
		links.get("contributors") // guaranteed because of constructor; else risky and could return null
				.add(newLink);
		// it's reciprocal; need to add to person's projects list
		person.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT));
	}

	public void removeContributor(Person person)
	{
		Link target = new Link(person.getPage(), Link.RelationshipType.CONTRIBUTOR_PERSON);
		links.get("contributors").remove(target); // this will return false if it's not there; no big deal.
		// it's reciprocal; need to remove from other user's list
		person.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT));
	}

	public void addCoordinator(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.COORDINATOR_PERSON);

		if (links.get("coordinators").indexOf(newLink) != -1)
		{
			return;
		}
		
		links.get("coordinators").add(newLink);
		person.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT));
	}

	public void removeCoordinator(Person person)
	{
		Link target = new Link(person.getPage(), Link.RelationshipType.COORDINATOR_PERSON);
		links.get("coordinators").remove(target);
		person.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT));
	}

	public void addCompany(Company company)
	{
		Link newLink = new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY);

		if (links.get("companies").indexOf(newLink) != -1)
		{
			return;
		}
		
		links.get("companies").add(newLink);
		company.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT));
	}

	public void removeCompany(Company company)
	{
		Link target = new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY);
		links.get("companies").remove(target);
		company.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT));
	}

}
