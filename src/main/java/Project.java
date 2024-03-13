import java.util.ArrayList;

public class Project extends Post
{

	public Project(String title, IdentifiableObjectManager manager)
	{
		super(title, manager);
		links.put("contributors", new ArrayList<Link>());
		links.put("coordinators", new ArrayList<Link>());
		links.put("companies", new ArrayList<Link>());
	}

	public void addContributor(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.CONTRIBUTOR_PERSON, manager);

		if (links.get("contributors").indexOf(newLink) != -1) // if already contributor, terminate early
		{
			return;
		} // else

		links.get("contributors") // guaranteed because of constructor; else risky and could return null
				.add(newLink);
		// it's reciprocal; need to add to person's projects list
		person.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
	}

	public void removeContributor(Person person)
	{
		Link target = new Link(person.getPage(), Link.RelationshipType.CONTRIBUTOR_PERSON, manager);
		links.get("contributors").remove(target); // this will return false if it's not there; no big deal.
		// it's reciprocal; need to remove from other user's list
		person.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
	}

	public void addCoordinator(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.COORDINATOR_PERSON, manager);

		if (links.get("coordinators").indexOf(newLink) != -1)
		{
			return;
		}

		links.get("coordinators").add(newLink);
		person.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
	}

	public void removeCoordinator(Person person)
	{
		Link target = new Link(person.getPage(), Link.RelationshipType.COORDINATOR_PERSON, manager);
		links.get("coordinators").remove(target);
		person.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
	}

	public void addCompany(Company company)
	{
		Link newLink = new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager);

		if (links.get("companies").indexOf(newLink) != -1)
		{
			return;
		}

		links.get("companies").add(newLink);
		company.getLinks().get("projects").add(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
	}

	public void removeCompany(Company company)
	{
		Link target = new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager);
		links.get("companies").remove(target);
		company.getLinks().get("projects").remove(new Link(page, Link.RelationshipType.HAS_PROJECT, manager));
	}

	@Override
	public String toString()
	{
		return "Project [title=" + title + ", description=" + description + ", id=" + id + ", links=" + links
				+ ", externalWebLinks=" + externalWebLinks + "]";
	}

}
