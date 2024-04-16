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
	ArrayList<Link> bobRecommendedJobs = new ArrayList<Link>();

	Skill java;
	Skill python;

	Company apple;
	Company google;

	JobPosting googleEngi;
	ArrayList<SkillProficiency> googleEngiSkills;
	Link googleEngiLink;
	JobPosting appleTech;
	ArrayList<SkillProficiency> appleTechSkills;
	Link appleTechLink;

	SkillProficiency javaBeginner;
	SkillProficiency javaIntermediate;
	SkillProficiency javaAdvanced;

	SkillProficiency pythonBeginner;
	SkillProficiency pythonIntermediate;
	SkillProficiency pythonAdvanced;

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

		googleEngiSkills = new ArrayList<SkillProficiency>();
		googleEngi = new JobPosting("Senior Software Developer", google, testRecommender, testManager);

		// check links properly
		assertEquals(
				Arrays.asList(
						new Link[] { new Link(google.getPage(), Link.RelationshipType.FROM_COMPANY, testManager) }),
				googleEngi.getLinks().get("company"));

		javaIntermediate = googleEngi.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.INTERMEDIATE);
		// check that skill creation works
		assertEquals(javaIntermediate,
				new SkillProficiency(java, SkillProficiency.ProficiencyLevel.INTERMEDIATE, testManager));
		googleEngiSkills.add(new SkillProficiency(java, SkillProficiency.ProficiencyLevel.INTERMEDIATE, testManager));

		appleTechSkills = new ArrayList<SkillProficiency>();
		appleTech = new JobPosting("Apple Genius Technician", apple, testRecommender, testManager);
		assertEquals(appleTechSkills, appleTech.getRequiredSkills()); // check that empty at first
		javaBeginner = appleTech.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.BEGINNER);
		appleTechSkills.add(new SkillProficiency(java, SkillProficiency.ProficiencyLevel.BEGINNER, testManager));
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());
		// let's override:
		javaAdvanced = appleTech.addRequiredSkill(java, SkillProficiency.ProficiencyLevel.ADVANCED);
		appleTechSkills.clear();
		appleTechSkills.add(new SkillProficiency(java, SkillProficiency.ProficiencyLevel.ADVANCED, testManager));
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());
		pythonBeginner = appleTech.addRequiredSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);
		appleTechSkills.add(new SkillProficiency(python, SkillProficiency.ProficiencyLevel.BEGINNER, testManager));
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());

		aliceRecommendedJobs = new ArrayList<Link>();
		bobRecommendedJobs = new ArrayList<Link>();

		googleEngiLink = new Link(googleEngi.getPage(), Link.RelationshipType.RECOMMENDED_JOB, testManager);
		appleTechLink = new Link(appleTech.getPage(), Link.RelationshipType.RECOMMENDED_JOB, testManager);
	}

	@Test
	void testRemoveSkills()
	{
		appleTech.removeRequiredSkill(python);
		appleTechSkills.remove(pythonBeginner);
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());

		appleTech.removeRequiredSkill(java);
		appleTechSkills.clear();
		assertEquals(appleTechSkills, appleTech.getRequiredSkills());
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
	}

	@Test
	void testRecommendByTypePreference()
	{

	}

	@Test
	void testRecommendBySkill()
	{

	}

}
