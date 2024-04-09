
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Entity implements Identifiable
{
	int id;
	@JsonIgnore
	Page page;
	int pageId;
	Hashtable<String, ArrayList<Link>> links; // maybe make map interface
	ArrayList<String> externalWebLinks;
	@JsonIgnore
	protected IdentifiableObjectManagerInterface manager;

	public Entity()
	{
	}
	
	public Entity(IdentifiableObjectManagerInterface manager) // WIP will this register subclass objects as Entities?
	{
		id = manager.getNextId();
		links = new Hashtable<String, ArrayList<Link>>();
		externalWebLinks = new ArrayList<String>();

		manager.register(this);
		this.manager = manager;
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
		pageId = page.id;
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
		return Objects.equals(externalWebLinks, other.externalWebLinks) && Objects.equals(links, other.links);
	}

	@Override
	public String toString()
	{
		return "Entity [id=" + id + ", links=" + links + ", externalWebLinks=" + externalWebLinks + "]";
	}

}
