
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.web.client.RestClient;

public class Person extends User implements Storable
{
	public Person()
	{
	}

	String pronouns;
	ArrayList<SkillProficiency> skills;
	ArrayList<WorkExperience> jobs;

	public Person(String name, String email, IdentifiableObjectManagerInterface manager)
	{
		super(name, email, manager);
		skills = new ArrayList<SkillProficiency>();
		jobs = new ArrayList<WorkExperience>();
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
		return skills.remove(new SkillProficiency(skill, SkillProficiency.ProficiencyLevel.BEGINNER, manager)); // level
																												// doesn't
		// matter
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

	public record PersonResponse(String request, boolean successful, String message, Person data) {
	}

	public static final String RESOURCE = "people";
	public static final String RESOURCE_DESC = "All the people on Branched Out.";

	public static Person retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			PersonResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(PersonResponse.class);

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
	public ArrayList<SkillProficiency> getSkills()
	{
		return skills;
	}

	/*
	 * @param skills the skills to set
	 */
	public void setSkills(ArrayList<SkillProficiency> skills)
	{
		this.skills = skills;
	}

	/**
	 * @return the jobs
	 */
	public ArrayList<WorkExperience> getJobs()
	{
		return jobs;
	}

	/*
	 * @param jobs the jobs to set
	 */
	public void setJobs(ArrayList<WorkExperience> jobs)
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
