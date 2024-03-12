import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public abstract class Entity implements Identifiable
{
	int id;
	Page page;
	Hashtable<String, ArrayList<Link>> links;
	ArrayList<String> externalWebLinks;

	public Entity() // WIP will this register subclass objects as Entities?
	{
		id = IdentifiableObjectManager.INSTANCE.getNextID();
		links = new Hashtable<String, ArrayList<Link>>();
		externalWebLinks = new ArrayList<String>();

		IdentifiableObjectManager.INSTANCE.objects.add(this); // registering with the manager
	}

	public void addExternalWebLink(String link)
	{
		externalWebLinks.add(link);
	}

	public boolean removeExternalWebLink(String link)
	{
		return externalWebLinks.remove(link);
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * @return the page
	 */
	public Page getPage()
	{
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Page page)
	{
		this.page = page;
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
	 * @return the externalWebLinks
	 */
	public ArrayList<String> getExternalWebLinks()
	{
		return externalWebLinks;
	}

	/**
	 * @param externalWebLinks the externalWebLinks to set
	 */
	public void setExternalWebLinks(ArrayList<String> externalWebLinks)
	{
		this.externalWebLinks = externalWebLinks;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(externalWebLinks, links, page);
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
		Entity other = (Entity) obj;
		return Objects.equals(externalWebLinks, other.externalWebLinks) && Objects.equals(links, other.links)
				&& Objects.equals(page, other.page);
	}

}
