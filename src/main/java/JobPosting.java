import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JobPosting extends Post implements Storable
{
	LocalDateTime expiration;
	LocalDate startDate;

	int weeklyHours;
	String payRange;
	JobType type;
	JobSite site;
	String location;

	List<SkillProficiency> requiredSkills;

	@JsonIgnore // this is a bad workaround but it is 3 am and i don't know how to fix
	RecommendationStrategy strategy;

	@JsonIgnore
	JobRecommenderInterface recommender;

	public JobPosting()
	{
	}

	/**
	 * @param title
	 * @param company
	 * @param manager
	 */
	public JobPosting(String title, Company company, JobRecommenderInterface recommender,
			IdentifiableObjectManagerInterface manager)
	{
		super(title, manager);
		// initializing with a single-item array to match type but fix size
		links.put("company",
				Arrays.asList(new Link[] { new Link(company.getPage(), Link.RelationshipType.FROM_COMPANY, manager) }));
		company.addJobPosting(this);
		
		page.addEditor(company); // automatically add company as an editor

		links.put("applicants", new ArrayList<Link>());

		requiredSkills = new ArrayList<SkillProficiency>();
		strategy = new RecommendAll(); // by default -- can be changed later
		this.recommender = recommender;
	}
	
	public void recommendJob() {
		recommender.recommend(this);
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

	public record JobPostingResponse(String request, boolean successful, String message, JobPosting data) {
	}

	public static final String RESOURCE = "jobPostings";
	public static final String RESOURCE_DESC = "All the job postings on Branched Out.";

	public static JobPosting retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			JobPostingResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(JobPostingResponse.class);

			return response.data;
		}
		// else
		return null;
	}

	@Override
	public boolean store()
	{
		RestClient client = RestClient.create();
		if (!RestUtilities.doesResourceExist(RESOURCE))
		{ // need to create the thing!
			RestUtilities.createResource(RESOURCE, RESOURCE_DESC);
		}
		ResponseObject result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(ResponseObject.class);
		return result.successful();
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
	 * @return the weeklyHours
	 */
	public int getWeeklyHours()
	{
		return weeklyHours;
	}

	/**
	 * @param weeklyHours the weeklyHours to set
	 */
	public void setWeeklyHours(int weeklyHours)
	{
		this.weeklyHours = weeklyHours;
	}

	/**
	 * @return the payRange
	 */
	public String getPayRange()
	{
		return payRange;
	}

	/**
	 * @param payRange the payRange to set
	 */
	public void setPayRange(String payRange)
	{
		this.payRange = payRange;
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
	 * @return the strategy
	 */
	public RecommendationStrategy getStrategy()
	{
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(RecommendationStrategy strategy)
	{
		this.strategy = strategy;
	}

	/**
	 * @return the recommender
	 */
	public JobRecommenderInterface getRecommender()
	{
		return recommender;
	}

	/**
	 * @param recommender the recommender to set
	 */
	public void setRecommender(JobRecommenderInterface recommender)
	{
		this.recommender = recommender;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(expiration, location, payRange, requiredSkills, site, startDate, type, weeklyHours);
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
		return Objects.equals(expiration, other.expiration) && Objects.equals(location, other.location)
				&& Objects.equals(payRange, other.payRange) && Objects.equals(requiredSkills, other.requiredSkills)
				&& site == other.site && Objects.equals(startDate, other.startDate) && type == other.type
				&& weeklyHours == other.weeklyHours;
	}

	@Override
	public String toString()
	{
		return "JobPosting [expiration=" + expiration + ", startDate=" + startDate + ", weeklyHours=" + weeklyHours
				+ ", payRange=" + payRange + ", type=" + type + ", site=" + site + ", location=" + location
				+ ", requiredSkills=" + requiredSkills + "]";
	}

}
