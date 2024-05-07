package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.recommender.JobSite;
import models.recommender.JobType;
import models.recommender.RecommendationStrategy;
import models.recommender.RecommendationStrategyKind;
import models.recommender.StrategyFactory;
import models.rest.RestUtilities;
import models.rest.RestReadyInterface;

public class JobPosting extends Post implements RestReadyInterface
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
	}

	public Company getCompany()
	{
		return (Company) getLinks().get("company").get(0).getPage().getEntity();
	}

	public void recommendJob(List<Person> people)
	{
		RecommendationStrategy strategy = StrategyFactory.constructStrategy(strategyKind);
		for (Person p : people)
		{
			if (p.isOpenToWork() && strategy.check(p, this))
			{
				p.addJobPosting(this);
			}
		}
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
		return newProf;
	}

	public boolean removeRequiredSkill(Skill skill)
	{
		// level doesn't matter
		return requiredSkills.remove(new SkillProficiency(skill, SkillProficiency.ProficiencyLevel.BEGINNER, manager));
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
		return title + " at " + getCompany();
	}

}
