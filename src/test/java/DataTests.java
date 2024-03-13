import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataTests
{
	// can skip testing getters/setters/object equality (most) because
	// auto-generated!
	Company apple;
	Page applePage;

	Company google;
	Page googlePage;

	Person alice;
	Page alicePage;

	Person bob;
	Page bobPage;

	Skill java;
	Page javaPage;

	Skill python;
	Page pythonPage;

	Project helloWorld;
	Page helloPage;

	Project myFirstProgram;
	Page firstPage;
	
	IdentifiableObjectManager testManager;

	@BeforeEach
	void setUp() throws Exception
	{
		testManager = new ConcreteIdentifiableObjectManager();
		ArrayList<Identifiable> objects = new ArrayList<Identifiable>();
		assertEquals(objects, testManager.getObjects());

		apple = new Company("Apple", "tim.cook@apple.com", testManager); // 0
		objects.add(apple);
		assertEquals(objects, testManager.getObjects());

		applePage = new Page(apple, testManager); // 1
		objects.add(applePage);
		assertEquals(objects, testManager.getObjects()); // atp have checked multiple inputs!

		apple.setPage(applePage);

		assertEquals(apple, testManager.getById(0));
		assertEquals(applePage, testManager.getById(1));

		google = new Company("Google", "sundar.pichai@google.com", testManager); // 2
		googlePage = new Page(google, testManager); // 3
		google.setPage(googlePage);

		alice = new Person("Alice", "ateam@gmail.com", testManager); // 4
		alicePage = new Page(alice, testManager); // 5
		alice.setPage(alicePage);

		bob = new Person("Bob", "bobert33@yahoo.com", testManager); // 6
		bobPage = new Page(bob, testManager); // 7
		bob.setPage(bobPage);

		java = new Skill("Java", testManager); // 8
		javaPage = new Page(java, testManager); // 9
		java.setPage(javaPage);

		python = new Skill("Python", testManager); // 10
		pythonPage = new Page(python, testManager); // 11
		python.setPage(pythonPage);

		helloWorld = new Project("Hello World", testManager); // 12
		helloPage = new Page(helloWorld, testManager); // 13
		helloWorld.setPage(helloPage);

		myFirstProgram = new Project("My First Program", testManager); // 14
		firstPage = new Page(myFirstProgram, testManager); // 15
		myFirstProgram.setPage(firstPage);

		// some spot checks on the array integrity
		assertEquals(alicePage, testManager.getById(5));
		assertEquals(java, testManager.getById(8));
		assertEquals(python, testManager.getById(10));
		assertEquals(firstPage, testManager.getById(15));
	}

	@Test
	void testPageEditPerms()
	{
		ArrayList<User> editors = new ArrayList<User>();

		// should start empty
		assertEquals(editors, alicePage.getCanEdit());

		// make sure one editor OK
		alicePage.addEditor(alice);
		editors.add(alice);
		assertEquals(editors, alicePage.getCanEdit());

		// check multiple editors
		alicePage.addEditor(bob);
		editors.add(bob);
		assertEquals(editors, alicePage.getCanEdit());

		// check works if remove worked
		assertTrue(alicePage.removeEditor(bob));
		editors.remove(bob);
		assertEquals(editors, alicePage.getCanEdit());

		// make sure second remove doesn't affect it
		assertFalse(alicePage.removeEditor(bob));
		assertEquals(editors, alicePage.getCanEdit());

		// check if actually emptied
		assertTrue(alicePage.removeEditor(alice));
		editors.clear();
		assertEquals(editors, alicePage.getCanEdit());
	}

	@Test
	void testPageViewPerms()
	{
		ArrayList<User> blocked = new ArrayList<User>();

		assertEquals(blocked, alicePage.getCantView());

		alicePage.blockViewer(bob);
		blocked.add(bob);
		assertEquals(blocked, alicePage.getCantView());

		alicePage.blockViewer(apple);
		blocked.add(apple);
		assertEquals(blocked, alicePage.getCantView());

		assertTrue(alicePage.unblockViewer(apple));
		blocked.remove(apple);
		assertEquals(blocked, alicePage.getCantView());

		assertFalse(alicePage.unblockViewer(apple));
		assertEquals(blocked, alicePage.getCantView());

		assertTrue(alicePage.unblockViewer(bob));
		blocked.clear();
		assertEquals(blocked, alicePage.getCantView());
	}

	@Test
	void testUserFollowing()
	{
		ArrayList<Link> aliceFollowing = new ArrayList<Link>();
		ArrayList<Link> appleFollowers = new ArrayList<Link>();

		assertEquals(aliceFollowing, alice.getLinks().get("following"));
		assertEquals(appleFollowers, apple.getLinks().get("followers"));

		alice.followUser(apple);
		Link followingApple = new Link(applePage, Link.RelationshipType.FOLLOWING_USER, testManager);
		Link followerAlice = new Link(alicePage, Link.RelationshipType.FOLLOWER_USER, testManager);
		aliceFollowing.add(followingApple);
		appleFollowers.add(followerAlice);
		assertEquals(aliceFollowing, alice.getLinks().get("following"));
		assertEquals(appleFollowers, apple.getLinks().get("followers"));

		// check multiple followers
		bob.followUser(apple);
		Link followerBob = new Link(bobPage, Link.RelationshipType.FOLLOWER_USER, testManager);
		appleFollowers.add(followerBob);
		assertEquals(appleFollowers, apple.getLinks().get("followers"));

		// check multiple following
		alice.followUser(google);
		Link followingGoogle = new Link(googlePage, Link.RelationshipType.FOLLOWING_USER, testManager);
		aliceFollowing.add(followingGoogle);
		assertEquals(aliceFollowing, alice.getLinks().get("following"));

		// make sure can unfollow
		alice.unfollowUser(apple);
		aliceFollowing.remove(followingApple);
		appleFollowers.remove(followerAlice);
		assertEquals(aliceFollowing, alice.getLinks().get("following"));
		assertEquals(appleFollowers, apple.getLinks().get("followers"));

		// second unfollow should not cause errors
		alice.unfollowUser(apple);
		assertEquals(aliceFollowing, alice.getLinks().get("following"));
		assertEquals(appleFollowers, apple.getLinks().get("followers"));

		// check empty following list
		alice.unfollowUser(google);
		aliceFollowing.clear();
		assertEquals(aliceFollowing, alice.getLinks().get("following"));

		// check if emptied followers
		bob.unfollowUser(apple);
		appleFollowers.clear();
		assertEquals(appleFollowers, apple.getLinks().get("followers"));
	}

	@Test
	void testEntityExtWebLinks()
	{
		ArrayList<String> extLinks = new ArrayList<String>();
		assertEquals(extLinks, google.getExternalWebLinks());

		google.addExternalWebLink("google.com");
		extLinks.add("google.com");
		assertEquals(extLinks, google.getExternalWebLinks());

		google.addExternalWebLink("youtube.com");
		extLinks.add("youtube.com");
		assertEquals(extLinks, google.getExternalWebLinks());

		assertTrue(google.removeExternalWebLink("youtube.com"));
		extLinks.remove("youtube.com");
		assertEquals(extLinks, google.getExternalWebLinks());

		assertFalse(google.removeExternalWebLink("youtube.com"));
		assertEquals(extLinks, google.getExternalWebLinks());
	}

	@Test
	void testPersonSkillProfs()
	{
		ArrayList<SkillProficiency> aliceSkills = new ArrayList<SkillProficiency>();
		assertEquals(aliceSkills, alice.getSkills());

		SkillProficiency aliceJava = alice.addSkill(java, SkillProficiency.ProficiencyLevel.ADVANCED);
		SkillProficiency advancedJava = new SkillProficiency(java, SkillProficiency.ProficiencyLevel.ADVANCED, testManager);
		assertEquals(advancedJava, aliceJava);
		aliceSkills.add(advancedJava);
		assertEquals(aliceSkills, alice.getSkills());

		SkillProficiency alicePython = alice.addSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);
		SkillProficiency beginnerPython = new SkillProficiency(python, SkillProficiency.ProficiencyLevel.BEGINNER, testManager);
		assertEquals(beginnerPython, alicePython);
		aliceSkills.add(beginnerPython);
		assertEquals(aliceSkills, alice.getSkills());

		// testing auto-update feature
		alicePython = alice.addSkill(python, SkillProficiency.ProficiencyLevel.INTERMEDIATE);
		aliceSkills.remove(beginnerPython);
		SkillProficiency intermediatePython = new SkillProficiency(python,
				SkillProficiency.ProficiencyLevel.INTERMEDIATE, testManager);
		assertEquals(intermediatePython, alicePython);
		aliceSkills.add(intermediatePython);
		assertEquals(aliceSkills, alice.getSkills());

		assertTrue(alice.removeSkill(python));
		aliceSkills.remove(intermediatePython);
		assertEquals(aliceSkills, alice.getSkills());

		assertFalse(alice.removeSkill(python));
		assertEquals(aliceSkills, alice.getSkills());

		assertTrue(alice.removeSkill(java));
		aliceSkills.clear();
		assertEquals(aliceSkills, alice.getSkills());
	}

	@Test
	void testPersonWorkExps()
	{
		ArrayList<WorkExperience> aliceJobs = new ArrayList<WorkExperience>();
		assertEquals(aliceJobs, alice.getJobs());

		WorkExperience aliceMobile = alice.addJob("Mobile UX Design Lead", "Crafted new Apple layout for home page.",
				apple);
		WorkExperience mobileUX = new WorkExperience("Mobile UX Design Lead", "Crafted new Apple layout for home page.",
				apple, testManager);
		assertEquals(mobileUX, aliceMobile);
		aliceJobs.add(mobileUX);
		assertEquals(aliceJobs, alice.getJobs());

		WorkExperience aliceBard = alice.addJob("Bard ML Engineer", "Launched the beta chatbot Bard.", google);
		WorkExperience bardEngineer = new WorkExperience("Bard ML Engineer", "Launched the beta chatbot Bard.", google, testManager);
		assertEquals(bardEngineer, aliceBard);
		aliceJobs.add(bardEngineer);
		assertEquals(aliceJobs, alice.getJobs());

		assertTrue(alice.removeJob("Mobile UX Design Lead", "Crafted new Apple layout for home page.", apple));
		aliceJobs.remove(mobileUX);
		assertEquals(aliceJobs, alice.getJobs());

		assertFalse(alice.removeJob("Mobile UX Design Lead", "Crafted new Apple layout for home page.", apple));
		assertEquals(aliceJobs, alice.getJobs());

		assertTrue(alice.removeJob("Bard ML Engineer", "Launched the beta chatbot Bard.", google));
		aliceJobs.clear();
		assertEquals(aliceJobs, alice.getJobs());
	}

	@Test
	void testProjectContributors()
	{
		ArrayList<Link> contributors = new ArrayList<Link>();
		ArrayList<Link> aliceProjects = new ArrayList<Link>();
		assertEquals(contributors, helloWorld.getLinks().get("contributors"));
		assertEquals(aliceProjects, alice.getLinks().get("projects"));

		helloWorld.addContributor(alice);
		Link contributorAlice = new Link(alicePage, Link.RelationshipType.CONTRIBUTOR_PERSON, testManager);
		Link helloProject = new Link(helloPage, Link.RelationshipType.HAS_PROJECT, testManager);
		contributors.add(contributorAlice);
		aliceProjects.add(helloProject);
		assertEquals(contributors, helloWorld.getLinks().get("contributors"));
		assertEquals(aliceProjects, alice.getLinks().get("projects"));

		// multiple contributors
		helloWorld.addContributor(bob);
		Link contributorBob = new Link(bobPage, Link.RelationshipType.CONTRIBUTOR_PERSON, testManager);
		contributors.add(contributorBob);
		assertEquals(contributors, helloWorld.getLinks().get("contributors"));

		// multiple projects
		myFirstProgram.addContributor(alice);
		Link firstProject = new Link(firstPage, Link.RelationshipType.HAS_PROJECT, testManager);
		aliceProjects.add(firstProject);
		assertEquals(aliceProjects, alice.getLinks().get("projects"));

		// remove contributor
		helloWorld.removeContributor(bob);
		contributors.remove(contributorBob);
		assertEquals(contributors, helloWorld.getLinks().get("contributors"));

		// second remove should not cause issues
		helloWorld.removeContributor(bob);
		assertEquals(contributors, helloWorld.getLinks().get("contributors"));

		// check empty contributors
		helloWorld.removeContributor(alice);
		contributors.clear();
		assertEquals(contributors, helloWorld.getLinks().get("contributors"));

		// check empty projects
		myFirstProgram.removeContributor(alice);
		aliceProjects.clear();
		assertEquals(aliceProjects, alice.getLinks().get("projects"));
	}

	@Test
	void testProjectCoordinators()
	{
		// testing coordinators
		ArrayList<Link> coordinators = new ArrayList<Link>();
		ArrayList<Link> aliceProjects = new ArrayList<Link>();
		assertEquals(coordinators, helloWorld.getLinks().get("coordinators"));
		assertEquals(aliceProjects, alice.getLinks().get("projects"));

		helloWorld.addCoordinator(alice);
		Link coordinatorAlice = new Link(alicePage, Link.RelationshipType.COORDINATOR_PERSON, testManager);
		Link helloProject = new Link(helloPage, Link.RelationshipType.HAS_PROJECT, testManager);
		coordinators.add(coordinatorAlice);
		aliceProjects.add(helloProject);
		assertEquals(coordinators, helloWorld.getLinks().get("coordinators"));
		assertEquals(aliceProjects, alice.getLinks().get("projects"));

		helloWorld.addCoordinator(bob);
		Link coordinatorBob = new Link(bobPage, Link.RelationshipType.COORDINATOR_PERSON, testManager);
		coordinators.add(coordinatorBob);
		assertEquals(coordinators, helloWorld.getLinks().get("coordinators"));

		myFirstProgram.addCoordinator(alice);
		Link firstProject = new Link(firstPage, Link.RelationshipType.HAS_PROJECT, testManager);
		aliceProjects.add(firstProject);
		assertEquals(aliceProjects, alice.getLinks().get("projects"));

		helloWorld.removeCoordinator(bob);
		coordinators.remove(coordinatorBob);
		assertEquals(coordinators, helloWorld.getLinks().get("coordinators"));

		helloWorld.removeCoordinator(bob);
		assertEquals(coordinators, helloWorld.getLinks().get("coordinators"));

		helloWorld.removeCoordinator(alice);
		coordinators.clear();
		assertEquals(coordinators, helloWorld.getLinks().get("coordinators"));

		myFirstProgram.removeCoordinator(alice);
		aliceProjects.clear();
		assertEquals(aliceProjects, alice.getLinks().get("projects"));
	}

	@Test
	void testProjectCompanies()
	{
		ArrayList<Link> companies = new ArrayList<Link>();
		ArrayList<Link> appleProjects = new ArrayList<Link>();
		assertEquals(companies, helloWorld.getLinks().get("companies"));
		assertEquals(appleProjects, apple.getLinks().get("projects"));

		helloWorld.addCompany(apple);
		Link companyApple = new Link(applePage, Link.RelationshipType.FROM_COMPANY, testManager);
		Link helloProject = new Link(helloPage, Link.RelationshipType.HAS_PROJECT, testManager);
		companies.add(companyApple);
		appleProjects.add(helloProject);
		assertEquals(companies, helloWorld.getLinks().get("companies"));
		assertEquals(appleProjects, apple.getLinks().get("projects"));

		helloWorld.addCompany(google);
		Link companyGoogle = new Link(googlePage, Link.RelationshipType.FROM_COMPANY, testManager);
		companies.add(companyGoogle);
		assertEquals(companies, helloWorld.getLinks().get("companies"));

		myFirstProgram.addCompany(apple);
		Link firstProject = new Link(firstPage, Link.RelationshipType.HAS_PROJECT, testManager);
		appleProjects.add(firstProject);
		assertEquals(appleProjects, apple.getLinks().get("projects"));

		helloWorld.removeCompany(google);
		companies.remove(companyGoogle);
		assertEquals(companies, helloWorld.getLinks().get("companies"));

		helloWorld.removeCompany(google);
		assertEquals(companies, helloWorld.getLinks().get("companies"));

		helloWorld.removeCompany(apple);
		companies.clear();
		assertEquals(companies, helloWorld.getLinks().get("companies"));

		myFirstProgram.removeCompany(apple);
		appleProjects.clear();
		assertEquals(appleProjects, apple.getLinks().get("projects"));
	}

}
