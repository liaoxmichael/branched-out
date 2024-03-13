import java.util.ArrayList;

public class Skill extends Post
{

	public Skill(String title, IdentifiableObjectManager manager)
	{
		super(title, manager);
		links.put("mentors", new ArrayList<Link>());
	}

	public void addMentor(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.MENTOR_PERSON, manager);
		if (links.get("mentors").indexOf(newLink) != -1) // if already mentor, terminate early
		{
			return;
		} // else

		links.get("mentors") // guaranteed because of constructor; else risky and could return null
				.add(newLink);
	}

	public boolean removeMentor(Person person)
	{
		return links.get("mentors").remove(new Link(person.getPage(), Link.RelationshipType.MENTOR_PERSON, manager));
	}

	@Override
	public String toString()
	{
		return "Skill [title=" + title + ", description=" + description + ", id=" + id + ", links=" + links
				+ ", externalWebLinks=" + externalWebLinks + "]";
	}

}
