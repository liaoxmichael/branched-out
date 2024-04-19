import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobRecommenderTests
{
	Person alice;
	ArrayList<Link> aliceRecommendedJobs;
	Person bob;
	ArrayList<Link> bobRecommendedJobs;

	Skill java;
	Skill python;

	Company apple;
	Company google;

	JobPosting googleEngi;
	Link googleEngiLink;
	
	JobPosting appleTech;
	Link appleTechLink;

	IdentifiableObjectManagerInterface testManager;
	JobRecommenderInterface testRecommender;

	@BeforeEach
	void setUp() throws Exception
	{
		testManager = new IdentifiableObjectManager();
		testRecommender = new JobRecommender();

		alice = new Person("Alice", "ateam@gmail.com", testRecommender, testManager);
		bob = new Person("Bob", "bobert33@yahoo.com", testRecommender, testManager);

		java = new Skill("Java", testManager);
		python = new Skill("Python", testManager);

		apple = new Company("Apple", "tim.cook@apple.com", testManager);
		google = new Company("Google", "sundar.pichai@google.com", testManager);

		googleEngi = new JobPosting("Senior Software Developer", google, testRecommender, testManager);
		appleTech = new JobPosting("Apple Genius Technician", apple, testRecommender, testManager);

		// check links properly
		assertEquals(
				Arrays.asList(
						new Link[] { new Link(google.getPage(), Link.RelationshipType.FROM_COMPANY, testManager) }),
				googleEngi.getLinks().get("company"));

		aliceRecommendedJobs = new ArrayList<Link>();
		bobRecommendedJobs = new ArrayList<Link>();

		googleEngiLink = new Link(googleEngi.getPage(), Link.RelationshipType.RECOMMENDED_JOB, testManager);
		appleTechLink = new Link(appleTech.getPage(), Link.RelationshipType.RECOMMENDED_JOB, testManager);
	}

	@Test
	void testRecommendAll()
	{
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// test that deregistering works
		bob.unmarkHireable();

		googleEngi.recommendJob();
		aliceRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		bob.markHireable();

		googleEngi.recommendJob(); // check that re-recommendation does not double up on Alice's jobs & appears in
									// Bob's
		bobRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		appleTech.recommendJob();
		aliceRecommendedJobs.add(appleTechLink);
		bobRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

	@Test
	void testRecomendBySitePreference()
	{
		googleEngi.setStrategy(new RecommendBySitePreference());
		appleTech.setStrategy(new RecommendBySitePreference());

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
		googleEngi.recommendJob();

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		googleEngi.setSite(JobSite.ON_SITE);
		appleTech.setSite(JobSite.HYBRID);

		googleEngi.recommendJob();
		aliceRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.addJobSitePreference(JobSite.HYBRID); // see if overlap works

		appleTech.recommendJob();
		aliceRecommendedJobs.add(appleTechLink);
		bobRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// test removal of jobs & re-recommendation
		appleTech.recommendJob();
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(appleTech);
		aliceRecommendedJobs.remove(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(googleEngi);
		aliceRecommendedJobs.clear();

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		appleTech.recommendJob();
		aliceRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

	@Test
	void testRecommendByTypePreference()
	{
		googleEngi.setStrategy(new RecommendByTypePreference());
		appleTech.setStrategy(new RecommendByTypePreference());

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
		googleEngi.recommendJob();

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		googleEngi.setType(JobType.FULL_TIME);
		appleTech.setType(JobType.TEMPORARY);

		googleEngi.recommendJob();
		aliceRecommendedJobs.add(googleEngiLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.addJobTypePreference(JobType.TEMPORARY); // see if overlap works

		appleTech.recommendJob();
		aliceRecommendedJobs.add(appleTechLink);
		bobRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		// test removal of jobs & re-recommendation
		appleTech.recommendJob();
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(appleTech);
		aliceRecommendedJobs.remove(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		alice.removeJobPosting(googleEngi);
		aliceRecommendedJobs.clear();

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));

		appleTech.recommendJob();
		aliceRecommendedJobs.add(appleTechLink);

		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

	@Test
	void testRecommendBySkill()
	{
		googleEngi.setStrategy(new RecommendBySkill());
		appleTech.setStrategy(new RecommendBySkill());
		
		ArrayList<SkillProficiency> googleEngiSkills = new ArrayList<SkillProficiency>();
		ArrayList<SkillProficiency> appleTechSkills = new ArrayList<SkillProficiency>();
		
		SkillProficiency javaIntermediate = googleEngi.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.INTERMEDIATE);
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
		SkillProficiency pythonBeginner = appleTech.addRequiredSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);
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
		
		appleTech.recommendJob();
		aliceRecommendedJobs.add(appleTechLink); // only alice matches
		
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
		
		googleEngi.addRequiredSkill(python, SkillProficiency.ProficiencyLevel.INTERMEDIATE);
		googleEngi.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.ADVANCED);
		
		googleEngi.recommendJob();
		// neither should qualify
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
		
		bob.addSkill(python, SkillProficiency.ProficiencyLevel.INTERMEDIATE);
		googleEngi.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.BEGINNER);
		googleEngi.addRequiredSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);
		
		googleEngi.recommendJob();
		aliceRecommendedJobs.add(googleEngiLink);
		bobRecommendedJobs.add(googleEngiLink);
		// both qualify
		assertEquals(aliceRecommendedJobs, alice.getLinks().get("recommendedJobs"));
		assertEquals(bobRecommendedJobs, bob.getLinks().get("recommendedJobs"));
	}

}
