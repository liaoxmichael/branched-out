package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import models.rest.RestReadyInterface;

public abstract class Entity implements Identifiable, RestReadyInterface
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
		id = manager.nextId();
		manager.register(this);
		links = new HashMap<String, List<Link>>();
		externalWebLinks = new ArrayList<String>();

		// automatically initialize a page
		page = new Page(this, manager);
		pageId = page.getId();

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
	public Page fetchPage()
	{
		return Page.retrieve(pageId);
	}

	/**
	 * @return the pageId
	 */
	public int getPageId()
	{
		return pageId;
	}

	/**
	 * @param pageId the pageId to set
	 */
	public void setPageId(int pageId)
	{
		this.pageId = pageId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
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

}
