package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.Company;
import models.IdentifiableObjectManager;
import models.IdentifiableObjectManagerInterface;
import models.JobPosting;
import models.Link;
import models.Person;
import models.Skill;
import models.SkillProficiency;
import models.recommender.JobSite;
import models.recommender.JobType;
import models.recommender.RecommendationStrategyKind;

class JobRecommenderTests
{
	Person alice;
	ArrayList<Link> aliceRecommendedJobs;
	Person bob;
	ArrayList<Link> bobRecommendedJobs;
	ArrayList<Person> people;

	Skill java;
	Skill python;

	Company apple;
	Company google;

	JobPosting googleEngi;
	Link googleEngiLink;

	JobPosting appleTech;
	Link appleTechLink;

	IdentifiableObjectManagerInterface testManager;

	@BeforeEach
	void setUp() throws Exception
	{
		testManager = new IdentifiableObjectManager();

		alice = new Person("Alice", "ateam@gmail.com", testManager);
		bob = new Person("Bob", "bobert33@yahoo.com", testManager);

		java = new Skill("Java", testManager);
		python = new Skill("Python", testManager);

		apple = new Company("Apple", "tim.cook@apple.com", testManager);
		google = new Company("Google", "sundar.pichai@google.com", testManager);

		googleEngi = new JobPosting("Senior Software Developer", google, testManager);
		appleTech = new JobPosting("Apple Genius Technician", apple, testManager);

		// check links properly
		assertEquals(
				Arrays.asList(
						new Link[] { new Link(google.fetchPage(), Link.RelationshipType.FROM_COMPANY, testManager) }),
				googleEngi.getLinks().get("company"));

		aliceRecommendedJobs = new ArrayList<Link>();
		bobRecommendedJobs = new ArrayList<Link>();

		googleEngiLink = new Link(googleEngi.fetchPage(), Link.RelationshipType.RECOMMENDED_JOB, testManager);
		appleTechLink = new Link(appleTech.fetchPage(), Link.RelationshipType.RECOMMENDED_JOB, testManager);

		// check that company has jobpostings
		ArrayList<Link> applePostings = new ArrayList<Link>();
		applePostings.add(new Link(appleTech.fetchPage(), Link.RelationshipType.HAS_OPENING, testManager));
		assertEquals(applePostings, apple.getLinks().get("jobPostings"));
		
		people = new ArrayList<Person>();
		people.add(alice);
		people.add(bob);
	}

	@Test
	void testRecommendAll()
	{
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// test that deregistering works
		bob.unmarkHireable();

		googleEngi.recommendJob(people);
		aliceRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		bob.markHireable();

		googleEngi.recommendJob(people); // check that re-recommendation does not double up on Alice's jobs & appears in
									// Bob's
		bobRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		appleTech.recommendJob(people);
		aliceRecommendedJobs.add(appleTechLink);
		bobRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

	@Test
	void testRecomendBySitePreference()
	{
		googleEngi.setStrategyKind(RecommendationStrategyKind.BY_SITE);
		appleTech.setStrategyKind(RecommendationStrategyKind.BY_SITE);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// time to give our people some preferences
		ArrayList<JobSite> alicePrefs = new ArrayList<JobSite>();
		ArrayList<JobSite> bobPrefs = new ArrayList<JobSite>();

		assertEquals(alicePrefs, alice.getJobSitePreferences());
		assertEquals(bobPrefs, bob.getJobSitePreferences());

		alice.addJobSitePreference(JobSite.ON_SITE);
		alicePrefs.add(JobSite.ON_SITE);

		bob.addJobSitePreference(JobSite.HYBRID);
		bobPrefs.add(JobSite.HYBRID);

		bob.addJobSitePreference(JobSite.REMOTE);
		bobPrefs.add(JobSite.REMOTE);

		assertEquals(alicePrefs, alice.getJobSitePreferences());
		assertEquals(bobPrefs, bob.getJobSitePreferences()); // tests multiple items added

		// test removal
		bob.removeJobSitePreference(JobSite.HYBRID);
		bobPrefs.remove(JobSite.HYBRID);
		assertEquals(bobPrefs, bob.getJobSitePreferences());

		bob.removeJobSitePreference(JobSite.REMOTE);
		bobPrefs.clear();
		assertEquals(bobPrefs, bob.getJobSitePreferences());

		// add back
		bob.addJobSitePreference(JobSite.HYBRID);
		bob.addJobSitePreference(JobSite.REMOTE);

		// test failover
		bob.addJobSitePreference(JobSite.HYBRID); // should early return

		// test null job posting site
		googleEngi.recommendJob(people);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		googleEngi.setSite(JobSite.ON_SITE);
		appleTech.setSite(JobSite.HYBRID);

		googleEngi.recommendJob(people);
		aliceRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.addJobSitePreference(JobSite.HYBRID); // see if overlap works

		appleTech.recommendJob(people);
		aliceRecommendedJobs.add(appleTechLink);
		bobRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// test removal of jobs & re-recommendation
		appleTech.recommendJob(people);
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(appleTech);
		aliceRecommendedJobs.remove(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(googleEngi);
		aliceRecommendedJobs.clear();

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		appleTech.recommendJob(people);
		aliceRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

	@Test
	void testRecommendByTypePreference()
	{
		googleEngi.setStrategyKind(RecommendationStrategyKind.BY_TYPE);
		appleTech.setStrategyKind(RecommendationStrategyKind.BY_TYPE);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// time to give our people some preferences
		ArrayList<JobType> alicePrefs = new ArrayList<JobType>();
		ArrayList<JobType> bobPrefs = new ArrayList<JobType>();

		assertEquals(alicePrefs, alice.getJobTypePreferences());
		assertEquals(bobPrefs, bob.getJobTypePreferences());

		alice.addJobTypePreference(JobType.FULL_TIME);
		alicePrefs.add(JobType.FULL_TIME);

		bob.addJobTypePreference(JobType.INTERNSHIP);
		bobPrefs.add(JobType.INTERNSHIP);

		bob.addJobTypePreference(JobType.TEMPORARY);
		bobPrefs.add(JobType.TEMPORARY);

		assertEquals(alicePrefs, alice.getJobTypePreferences());
		assertEquals(bobPrefs, bob.getJobTypePreferences()); // tests multiple items added

		// test removal
		bob.removeJobTypePreference(JobType.INTERNSHIP);
		bobPrefs.remove(JobType.INTERNSHIP);
		assertEquals(bobPrefs, bob.getJobTypePreferences());

		bob.removeJobTypePreference(JobType.TEMPORARY);
		bobPrefs.clear();
		assertEquals(bobPrefs, bob.getJobTypePreferences());

		// add back
		bob.addJobTypePreference(JobType.INTERNSHIP);
		bob.addJobTypePreference(JobType.TEMPORARY);

		// test fail-over
		bob.addJobTypePreference(JobType.INTERNSHIP); // should early return

		// test null job posting site
		googleEngi.recommendJob(people);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		googleEngi.setType(JobType.FULL_TIME);
		appleTech.setType(JobType.TEMPORARY);

		googleEngi.recommendJob(people);
		aliceRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.addJobTypePreference(JobType.TEMPORARY); // see if overlap works

		appleTech.recommendJob(people);
		aliceRecommendedJobs.add(appleTechLink);
		bobRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// test removal of jobs & re-recommendation
		appleTech.recommendJob(people);
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(appleTech);
		aliceRecommendedJobs.remove(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(googleEngi);
		aliceRecommendedJobs.clear();

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		appleTech.recommendJob(people);
		aliceRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

	@Test
	void testRecommendBySkill()
	{
		googleEngi.setStrategyKind(RecommendationStrategyKind.BY_SKILL);
		appleTech.setStrategyKind(RecommendationStrategyKind.BY_SKILL);

		ArrayList<SkillProficiency> googleEngiSkills = new ArrayList<SkillProficiency>();
		ArrayList<SkillProficiency> appleTechSkills = new ArrayList<SkillProficiency>();

		SkillProficiency javaIntermediate = googleEngi.addRequiredSkill(java,
				SkillProficiency.ProficiencyLevel.INTERMEDIATE);
		// check that skill creation works
		assertEquals(javaIntermediate,
				new SkillProficiency(java, SkillProficiency.ProficiencyLevel.INTERMEDIATE, testManager));
		googleEngiSkills.add(new SkillProficiency(java, SkillProficiency.ProficiencyLevel.INTERMEDIATE, testManager));

		assertEquals(appleTechSkills, appleTech.getRequiredSkills()); // check that empty at first
		appleTech.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.BEGINNER);
		appleTechSkills.add(new SkillProficiency(java, SkillProficiency.ProficiencyLevel.BEGINNER, testManager));
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());
		// let's override:
		appleTech.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.ADVANCED);
		appleTechSkills.clear();
		appleTechSkills.add(new SkillProficiency(java, SkillProficiency.ProficiencyLevel.ADVANCED, testManager));
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());
		SkillProficiency pythonBeginner = appleTech.addRequiredSkill(python,
				SkillProficiency.ProficiencyLevel.BEGINNER);
		appleTechSkills.add(new SkillProficiency(python, SkillProficiency.ProficiencyLevel.BEGINNER, testManager));
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());

		// test removal
		appleTech.removeRequiredSkill(python);
		appleTechSkills.remove(pythonBeginner);
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());

		appleTech.removeRequiredSkill(java);
		appleTechSkills.clear();
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());

		// restore it
		appleTech.addRequiredSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);
		appleTech.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.BEGINNER);

		alice.addSkill(java, SkillProficiency.ProficiencyLevel.ADVANCED);
		alice.addSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);

		bob.addSkill(java, SkillProficiency.ProficiencyLevel.BEGINNER);

		appleTech.recommendJob(people);
		aliceRecommendedJobs.add(appleTechLink); // only alice matches

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		googleEngi.addRequiredSkill(python, SkillProficiency.ProficiencyLevel.INTERMEDIATE);
		googleEngi.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.ADVANCED);

		googleEngi.recommendJob(people);
		// neither should qualify
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		bob.addSkill(python, SkillProficiency.ProficiencyLevel.INTERMEDIATE);
		googleEngi.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.BEGINNER);
		googleEngi.addRequiredSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);

		googleEngi.recommendJob(people);
		aliceRecommendedJobs.add(googleEngiLink);
		bobRecommendedJobs.add(googleEngiLink);
		// both qualify
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

}
