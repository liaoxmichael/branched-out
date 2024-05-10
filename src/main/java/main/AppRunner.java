package main;

import org.springframework.web.client.RestClient;

import models.Company;
import models.IdentifiableObjectManager;
import models.JobPosting;
import models.Person;
import models.Skill;
import models.SkillProficiency.ProficiencyLevel;
import models.recommender.JobSite;
import models.recommender.JobType;
import models.rest.ResponseData;
import models.rest.RestUtilities;

public class AppRunner
{
	public static void main(String[] args)
	{
		populateData();
		Main.main(args);

	}

	public static void populateData()
	{
		RestClient client = RestClient.create();

		ResponseData team = new ResponseData(RestUtilities.TEAM_NAME, RestUtilities.TEAM_DESC, RestUtilities.TEAM_URI);

		client.delete().uri(RestUtilities.TEAM_URI).retrieve();

		client.post().uri(RestUtilities.TEAM_URI).body(team).retrieve().body(String.class);
		IdentifiableObjectManager testManager = new IdentifiableObjectManager(); // 0

		Company apple = new Company("Apple", "tim.cook@apple.com", testManager); // 1
		Company google = new Company("Google", "sundar.pichai@google.com", testManager); // 3
		Person alice = new Person("Alice", "ateam@gmail.com", testManager); // 5
		Person bob = new Person("Bob", "bobert33@yahoo.com", testManager); // 7
		Skill java = new Skill("Java", testManager); // 9
		Skill python = new Skill("Python", testManager); // 11
		JobPosting googleDev = new JobPosting("Senior Software Developer", google, testManager); // 13
		JobPosting appleTech = new JobPosting("Apple Genius Technician", apple, testManager); // 17
		
		python.setDescription("Sorry about our flexible typing!");
		python.update();

		java.setDescription("Like the coffee.");
		java.addMentor(alice);
		java.fetchPage().addEditor(alice);
		java.update();

		google.setAvatarURL("/googleg.png");
		google.setBio("You've heard of us.");
		google.followUser(alice);
		google.update();

		apple.setBio("Making designer tech.");
		apple.setAvatarURL("/apple.png");
		apple.update();

		googleDev.setDescription(
				"We're looking for a highly-motivated designer and developer who can maintain our existing codebase while helping to onboard junior developers as we expand.");
		googleDev.setLocation("San Francisco, CA");
		googleDev.setSite(JobSite.ON_SITE);
		googleDev.setType(JobType.FULL_TIME);
		googleDev.addRequiredSkill(java, ProficiencyLevel.ADVANCED);
		googleDev.update();

		appleTech.setDescription("We need a team player who's ready to help set up a new store in a college town.");
		appleTech.setLocation("Danville, KY");
		appleTech.setSite(JobSite.HYBRID);
		appleTech.setType(JobType.SEASONAL);
		appleTech.update();

		bob.addSkill(python, ProficiencyLevel.BEGINNER);
		bob.setPronouns("he/they");
		bob.setBio("I don't know what I'm doing.");
		bob.update();

		alice.setPassword("1234");
		alice.addSkill(python, ProficiencyLevel.INTERMEDIATE);
		alice.addSkill(java, ProficiencyLevel.ADVANCED);
		alice.setPronouns("she/her");
		alice.setBio("Admin at Google. Just trying to innovate.");
		alice.followUser(google);
		alice.addJob("Server Manager", "Oversaw day-to-day operations of data racks.", google);
		alice.update();

		google.fetchPage().blockViewer(bob);

		apple.fetchPage().addEditor(alice);

		googleDev.addApplicant(bob);
		googleDev.fetchPage().addEditor(alice);
		googleDev.update();
	}

}
