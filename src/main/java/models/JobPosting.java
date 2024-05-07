package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.adapters.Displayable;
import models.recommender.JobSite;
import models.recommender.JobType;
import models.recommender.RecommendationStrategy;
import models.recommender.RecommendationStrategyKind;
import models.recommender.StrategyFactory;
import models.rest.RestUtilities;
import models.rest.RestReadyInterface;

public class JobPosting extends Post implements RestReadyInterface, Displayable
{
	LocalDateTime expiration;
	LocalDate startDate;
	LocalDate datePosted;

	JobType type;
	JobSite site;
	String location;

	List<SkillProficiency> requiredSkills;

	RecommendationStrategyKind strategyKind;

	public JobPosting()
	{
	}

	/**
	 * @param title
	 * @param company
	 * @param manager
	 */
	public JobPosting(String title, Company company, IdentifiableObjectManagerInterface manager)
	{
		super(title, manager);
		// initializing with a single-item array to match type but fix size
		links.put("company",
				Arrays.asList(new Link[] { new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager) }));
		company.addJobPosting(this);

		page.addEditor(company); // automatically add company as an editor

		links.put("applicants", new ArrayList<Link>());

		requiredSkills = new ArrayList<SkillProficiency>();
		strategyKind = RecommendationStrategyKind.ALL; // by default -- can be changed later
		
		store();
	}

	public Company whichCompany()
	{
		return Company.retrieve(getLinks().get("company").get(0).getPage().getEntityId());
	}

	public void recommendJob(List<Person> people)
	{
		RecommendationStrategy strategy = StrategyFactory.constructStrategy(strategyKind);
		for (Person p : people)
		{
			if (p.isOpenToWork() && strategy.check(p, this))
			{
				p.addJobPosting(this);
				p.update();
			}
		}
	}

	public void recommendJob() // version that interacts with server
	{
		List<Person> people = Person.retrieveAll();
		recommendJob(people);
	}

	public SkillProficiency addRequiredSkill(Skill skill, SkillProficiency.ProficiencyLevel level)
	{
		SkillProficiency newProf = new SkillProficiency(skill, level, manager);
		int skillIndex = requiredSkills.indexOf(newProf);
		if (skillIndex != -1)
		{ // if skill exists, override
			requiredSkills.get(skillIndex).setLevel(level);
			return requiredSkills.get(skillIndex);
		}
		requiredSkills.add(newProf);
		update();
		return newProf;
	}

	public boolean removeRequiredSkill(Skill skill)
	{
		// level doesn't matter
		boolean result = requiredSkills.remove(new SkillProficiency(skill, SkillProficiency.ProficiencyLevel.BEGINNER, manager));
		update();
		return result;
	}

	public static record ResponseRecord(String request, boolean successful, String message, JobPosting data) {
	}

	public static final String RESOURCE = "jobPostings";
	public static final String RESOURCE_DESC = "All the job postings on Branched Out.";

	public static JobPosting retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), JobPosting.class);
		} catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<JobPosting> retrieveAll()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<JobPosting> list = new ArrayList<JobPosting>();
		List<JsonNode> nodes = RestUtilities.retrieveAll(RESOURCE);
		try
		{
			for (JsonNode n : nodes)
			{
				System.out.println(n);
				list.add(mapper.treeToValue(n, JobPosting.class));
			}
		} catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean store()
	{
		return RestUtilities.store(this, JobPosting.class, RESOURCE, RESOURCE_DESC);
	}
	
	@Override
	public boolean update()
	{
		return RestUtilities.update(this, JobPosting.class, RESOURCE, RESOURCE_DESC);
	}

	@Override
	public void setTitle(String title)
	{
		super.setTitle(title);
		update();
	}

	@Override
	public void setDescription(String description)
	{
		super.setDescription(description);
		update();
	}

	@Override
	public void addExternalWebLink(String link)
	{
		super.addExternalWebLink(link);
		update();
	}

	@Override
	public boolean removeExternalWebLink(String link)
	{
		boolean result = super.removeExternalWebLink(link);
		update();
		return result;
	}

	@Override
	public void setPage(Page page)
	{
		super.setPage(page);
		update();
	}

	@Override
	public void setPageId(int pageId)
	{
		super.setPageId(pageId);
		update();
	}

	@Override
	public void setId(int id)
	{
		super.setId(id);
		update();
	}

	@Override
	public void setLinks(Map<String, List<Link>> links)
	{
		super.setLinks(links);
		update();
	}

	@Override
	public void setExternalWebLinks(List<String> externalWebLinks)
	{
		super.setExternalWebLinks(externalWebLinks);
		update();
	}

	/**
	 * @return the datePosted
	 */
	public LocalDate getDatePosted()
	{
		return datePosted;
	}

	/**
	 * @param datePosted the datePosted to set
	 */
	public void setDatePosted(LocalDate datePosted)
	{
		this.datePosted = datePosted;
		update();
	}

	/**
	 * @return the expiration
	 */
	public LocalDateTime getExpiration()
	{
		return expiration;
	}

	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(LocalDateTime expiration)
	{
		this.expiration = expiration;
		update();
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate)
	{
		this.startDate = startDate;
		update();
	}

	/**
	 * @return the type
	 */
	public JobType getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(JobType type)
	{
		this.type = type;
		update();
	}

	/**
	 * @return the site
	 */
	public JobSite getSite()
	{
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(JobSite site)
	{
		this.site = site;
		update();
	}

	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
		update();
	}

	/**
	 * @return the requiredSkills
	 */
	public List<SkillProficiency> getRequiredSkills()
	{
		return requiredSkills;
	}

	/**
	 * @param requiredSkills the requiredSkills to set
	 */
	public void setRequiredSkills(List<SkillProficiency> requiredSkills)
	{
		this.requiredSkills = requiredSkills;
		update();
	}

	/**
	 * @return the strategyKind
	 */
	public RecommendationStrategyKind getStrategyKind()
	{
		return strategyKind;
	}

	/**
	 * @param strategyKind the strategyKind to set
	 */
	public void setStrategyKind(RecommendationStrategyKind strategyKind)
	{
		this.strategyKind = strategyKind;
		update();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(datePosted, expiration, location, requiredSkills, site, startDate, strategyKind, type);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobPosting other = (JobPosting) obj;
		return Objects.equals(datePosted, other.datePosted) && Objects.equals(expiration, other.expiration)
				&& Objects.equals(location, other.location) && Objects.equals(requiredSkills, other.requiredSkills)
				&& site == other.site && Objects.equals(startDate, other.startDate)
				&& strategyKind == other.strategyKind && type == other.type;
	}

	@Override
	public String toString()
	{
		return title + " at " + whichCompany();
	}

}
