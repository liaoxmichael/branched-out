package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.recommender.JobRecommenderInterface;
import models.recommender.JobSite;
import models.recommender.JobType;
import models.rest.RestUtilities;
import models.rest.RestReadyInterface;

public class Person extends User implements RestReadyInterface
{
	String pronouns;
	List<SkillProficiency> skills;
	List<WorkExperience> jobs;

	List<JobType> jobTypePreferences;
	List<JobSite> jobSitePreferences;

	@JsonIgnore
	JobRecommenderInterface recommender;

	public Person()
	{
	}

	public Person(String name, String email, JobRecommenderInterface recommender,
			IdentifiableObjectManagerInterface manager)
	{
		super(name, email, manager);
		skills = new ArrayList<SkillProficiency>();
		jobs = new ArrayList<WorkExperience>();

		jobTypePreferences = new ArrayList<JobType>(); // if time: make priority queues, for now boolean
		jobSitePreferences = new ArrayList<JobSite>();

		links.put("recommendedJobs", new ArrayList<Link>());

		this.recommender = recommender;
		// automatically subscribe -- can use functions later on to unsubscribe
		markHireable();
	}

	public void markHireable()
	{
		recommender.registerHireable(this);
	}

	public void unmarkHireable()
	{
		recommender.unregisterHireable(this);
	}

	public void addJobPosting(JobPosting jobPosting)
	{
		Link newLink = new Link(jobPosting.getPage(), Link.RelationshipType.RECOMMENDED_JOB, manager);
		int linkIndex = links.get("recommendedJobs").indexOf(newLink);

		if (linkIndex != -1)
		{
			return;
		} // else

		links.get("recommendedJobs").add(newLink);
	}

	public boolean removeJobPosting(JobPosting jobPosting)
	{
		Link target = new Link(jobPosting.getPage(), Link.RelationshipType.RECOMMENDED_JOB, manager);
		return links.get("recommendedJobs").remove(target);
	}

	public void addJobTypePreference(JobType jobType)
	{
		int index = jobTypePreferences.indexOf(jobType);

		if (index != -1) // if already following: early termination
		{
			return;
		} // else
		jobTypePreferences.add(jobType);
	}

	public boolean removeJobTypePreference(JobType jobType)
	{
		return jobTypePreferences.remove(jobType);
	}

	public void addJobSitePreference(JobSite jobSite)
	{
		int index = jobSitePreferences.indexOf(jobSite);

		if (index != -1) // if already following: early termination
		{
			return;
		} // else
		jobSitePreferences.add(jobSite);
	}

	public boolean removeJobSitePreference(JobSite jobSite)
	{
		return jobSitePreferences.remove(jobSite);
	}

	public SkillProficiency addSkill(Skill skill, SkillProficiency.ProficiencyLevel level)
	{
		SkillProficiency newProf = new SkillProficiency(skill, level, manager);
		int skillIndex = skills.indexOf(newProf);
		if (skillIndex != -1)
		{ // if skill exists, override
			skills.get(skillIndex).setLevel(level);
			return skills.get(skillIndex);
		}
		skills.add(newProf);
		return newProf;
	}

	public boolean removeSkill(Skill skill)
	{
		// level doesn't matter
		return skills.remove(new SkillProficiency(skill, SkillProficiency.ProficiencyLevel.BEGINNER, manager));
	}

	public WorkExperience addJob(String jobTitle, String jobDesc, Company company)
	{
		WorkExperience newJob = new WorkExperience(jobTitle, jobDesc, company, manager);
		int jobIndex = jobs.indexOf(newJob);
		if (jobs.indexOf(newJob) != -1)
		{
			return jobs.get(jobIndex);
		} // in future, if date implemented, can throw overlapping exception
		jobs.add(newJob);
		return newJob;
	}

	public boolean removeJob(String jobTitle, String jobDesc, Company company)
	{
		return jobs.remove(new WorkExperience(jobTitle, jobDesc, company, manager));
	}

	@Override
	public int getId()
	{
		return id;
	}

	public static record ResponseRecord(String request, boolean successful, String message, Person data) {
	}

	public static final String RESOURCE = "people";
	public static final String RESOURCE_DESC = "All the people on Branched Out.";

	public static Person retrieve(int id)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.treeToValue(RestUtilities.retrieve(id, RESOURCE), Person.class);
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

	@Override
	public boolean store()
	{
		return RestUtilities.store(this, Person.class, RESOURCE, RESOURCE_DESC);
	}

	/**
	 * @return the jobTypePreferences
	 */
	public List<JobType> getJobTypePreferences()
	{
		return jobTypePreferences;
	}

	/**
	 * @param jobTypePreferences the jobTypePreferences to set
	 */
	public void setJobTypePreferences(List<JobType> jobTypePreferences)
	{
		this.jobTypePreferences = jobTypePreferences;
	}

	/**
	 * @return the jobSitePreferences
	 */
	public List<JobSite> getJobSitePreferences()
	{
		return jobSitePreferences;
	}

	/**
	 * @param jobSitePreferences the jobSitePreferences to set
	 */
	public void setJobSitePreferences(List<JobSite> jobSitePreferences)
	{
		this.jobSitePreferences = jobSitePreferences;
	}

	/**
	 * @return the pronouns
	 */
	public String getPronouns()
	{
		return pronouns;
	}

	/**
	 * @param pronouns the pronouns to set
	 */
	public void setPronouns(String pronouns)
	{
		this.pronouns = pronouns;
	}

	/**
	 * @return the skills
	 */
	public List<SkillProficiency> getSkills()
	{
		return skills;
	}

	/*
	 * @param skills the skills to set
	 */
	public void setSkills(List<SkillProficiency> skills)
	{
		this.skills = skills;
	}

	/**
	 * @return the jobs
	 */
	public List<WorkExperience> getJobs()
	{
		return jobs;
	}

	/*
	 * @param jobs the jobs to set
	 */
	public void setJobs(List<WorkExperience> jobs)
	{
		this.jobs = jobs;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(jobs, pronouns, skills);
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
		Person other = (Person) obj;
		return Objects.equals(jobs, other.jobs) && Objects.equals(pronouns, other.pronouns)
				&& Objects.equals(skills, other.skills);
	}

	@Override
	public String toString()
	{
		return "Person [pronouns=" + pronouns + ", skills=" + skills + ", jobs=" + jobs + ", name=" + name + ", bio="
				+ bio + ", email=" + email + ", phone=" + phone + ", avatarURL=" + avatarURL + ", bannerURL="
				+ bannerURL + ", id=" + id + ", links=" + links + ", externalWebLinks=" + externalWebLinks + "]";
	}

}
