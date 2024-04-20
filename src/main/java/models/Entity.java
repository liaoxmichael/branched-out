package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Entity implements Identifiable
{
	int id;
	@JsonIgnore
	Page page;
	int pageId;
	Map<String, List<Link>> links;
	List<String> externalWebLinks;
	protected IdentifiableObjectManagerInterface manager;

	public Entity()
	{
	}

	public Entity(IdentifiableObjectManagerInterface manager)
	{
		id = manager.getNextId();
		manager.register(this);
		links = new HashMap<String, List<Link>>();
		externalWebLinks = new ArrayList<String>();
		
		// automatically initialize a page
		page = new Page(this, manager);

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
	public Map<String, List<Link>> getLinks()
	{
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(Map<String, List<Link>> links)
	{
		this.links = links;
	}

	/**
	 * @return the externalWebLinks
	 */
	public List<String> getExternalWebLinks()
	{
		return externalWebLinks;
	}

	/**
	 * @param externalWebLinks the externalWebLinks to set
	 */
	public void setExternalWebLinks(List<String> externalWebLinks)
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
