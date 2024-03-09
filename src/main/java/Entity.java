import java.util.ArrayList;
import java.util.Hashtable;

public abstract class Entity extends IdentifiableObject
{
	Page page;
	Hashtable<String, ArrayList<Link>> links;
	ArrayList<String> externalWebLinks;
	
	public Entity(int id, Page page)
	{
		super(id);
		this.page = page;
		links = new Hashtable<String, ArrayList<Link>>();
		externalWebLinks = new ArrayList<String>();
	}
	
	public void addExternalWebLink(String link) {
		externalWebLinks.add(link);
	}
	
	public boolean delExternalWebLink(String link) {
		return externalWebLinks.remove(link);
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

	
	
}
