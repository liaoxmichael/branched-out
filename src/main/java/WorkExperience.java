
//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class WorkExperience implements Identifiable
{

	int id;
	Hashtable<String, ArrayList<Link>> links;

	String title;
	String description;
	
	protected IdentifiableObjectManager manager;

//	LocalDate startDate;
//	LocalDate endDate;

	public WorkExperience(String title, String description, Company company, IdentifiableObjectManager manager) // if gets more complex, consider using
																				// factory pattern?
	{
		id = manager.getNextId();
		this.title = title;
		this.description = description;
		links = new Hashtable<String, ArrayList<Link>>();
		links.put("companies", new ArrayList<Link>());
		links.get("companies").add(new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager));

		manager.register(this); // registering with the manager
		this.manager = manager;
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * @return the links
	 */
	public Hashtable<String, ArrayList<Link>> getLinks()
	{
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(Hashtable<String, ArrayList<Link>> links)
	{
		this.links = links;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(description, links, title);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkExperience other = (WorkExperience) obj;
		return Objects.equals(description, other.description) && Objects.equals(links, other.links)
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString()
	{
		return "WorkExperience [id=" + id + ", links=" + links + ", title=" + title + ", description=" + description
				+ "]";
	}

}
