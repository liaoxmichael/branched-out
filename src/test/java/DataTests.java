import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import models.Company;
import models.Identifiable;
import models.IdentifiableObjectManager;
import models.IdentifiableObjectManagerInterface;
import models.Link;
import models.Page;
import models.Person;
import models.Project;
import models.Skill;
import models.SkillProficiency;
import models.User;
import models.WorkExperience;
import models.rest.ResponseData;
import models.rest.RestUtilities;

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

	IdentifiableObjectManagerInterface testManager;

	@BeforeEach
	void setUp() throws Exception
	{
		// clear the server just in case
		RestClient client = RestClient.create();

		ResponseData team = new ResponseData(RestUtilities.TEAM_NAME, RestUtilities.TEAM_DESC, RestUtilities.TEAM_URI);

		client.delete().uri(RestUtilities.TEAM_URI).retrieve();

		client.post().uri(RestUtilities.TEAM_URI).body(team).retrieve().body(String.class);
		
		testManager = new IdentifiableObjectManager(); // 0
		ArrayList<Identifiable> objects = new ArrayList<Identifiable>();
		objects.add((Identifiable) testManager);
		assertEquals(objects, testManager.getObjects());

		apple = new Company("Apple", "tim.cook@apple.com", testManager); // 1
		applePage = apple.fetchPage(); // 2
		objects.add(apple);
		objects.add(applePage);
		assertEquals(objects, testManager.getObjects());

		assertEquals(apple, testManager.getById(1));
		assertEquals(applePage, testManager.getById(2));

		google = new Company("Google", "sundar.pichai@google.com", testManager); // 3
		googlePage = google.fetchPage(); // 4
		objects.add(google);
		objects.add(googlePage);
		assertEquals(objects, testManager.getObjects());

		alice = new Person("Alice", "ateam@gmail.com", testManager); // 5
		alicePage = alice.fetchPage(); // 6

		bob = new Person("Bob", "bobert33@yahoo.com", testManager); // 7
		bobPage = bob.fetchPage(); // 8

		java = new Skill("Java", testManager); // 9
		javaPage = java.fetchPage(); // 10

		python = new Skill("Python", testManager); // 11
		pythonPage = python.fetchPage(); // 12

		helloWorld = new Project("Hello World", testManager); // 13
		helloPage = helloWorld.fetchPage(); // 14

		myFirstProgram = new Project("My First Program", testManager); // 15
		firstPage = myFirstProgram.fetchPage(); // 16

		// some spot checks on the array integrity
		assertEquals(alice, testManager.getById(5));
		assertEquals(alicePage, testManager.getById(6));
		assertEquals(java, testManager.getById(9));
		assertEquals(python, testManager.getById(11));
		assertEquals(firstPage, testManager.getById(16));
	}

	@Test
	void testPageEditPerms()
	{
		ArrayList<User> editors = new ArrayList<User>();
		// should start w/ alice as editor
		editors.add(alice);

		assertIterableEquals(editors, alicePage.fetchEditors());
		assertTrue(alicePage.canEdit(alice));
		assertFalse(alicePage.canEdit(bob));

		// make sure one editor OK -- can't double add same user
		alicePage.addEditor(alice);
		assertIterableEquals(editors, alicePage.fetchEditors());
		assertTrue(alicePage.canEdit(alice));
		assertFalse(alicePage.canEdit(bob));

		// check multiple editors
		alicePage.addEditor(bob);
		editors.add(bob);
		assertIterableEquals(editors, alicePage.fetchEditors());
		assertTrue(alicePage.canEdit(alice));
		assertTrue(alicePage.canEdit(bob));

		// check works if remove worked
		assertTrue(alicePage.removeEditor(bob));
		editors.remove(bob);
		assertIterableEquals(editors, alicePage.fetchEditors());
		assertTrue(alicePage.canEdit(alice));
		assertFalse(alicePage.canEdit(bob));

		// make sure second remove doesn't affect it
		assertFalse(alicePage.removeEditor(bob));
		assertIterableEquals(editors, alicePage.fetchEditors());
		assertTrue(alicePage.canEdit(alice));
		assertFalse(alicePage.canEdit(bob));

		// check if actually emptied
		assertTrue(alicePage.removeEditor(alice));
		editors.clear();
		assertIterableEquals(editors, alicePage.fetchEditors());
		assertFalse(alicePage.canEdit(alice));
		assertFalse(alicePage.canEdit(bob));
	}

	@Test
	void testPageViewPerms()
	{
		ArrayList<User> blocked = new ArrayList<User>();

		assertIterableEquals(blocked, alicePage.fetchBlockedViewers());
		assertFalse(alicePage.cantView(apple));
		assertFalse(alicePage.cantView(bob));

		alicePage.blockViewer(bob);
		blocked.add(bob);
		assertIterableEquals(blocked, alicePage.fetchBlockedViewers());
		assertFalse(alicePage.cantView(apple));
		assertTrue(alicePage.cantView(bob));

		alicePage.blockViewer(apple);
		blocked.add(apple);
		assertIterableEquals(blocked, alicePage.fetchBlockedViewers());
		assertTrue(alicePage.cantView(apple));
		assertTrue(alicePage.cantView(bob));

		assertTrue(alicePage.unblockViewer(apple));
		blocked.remove(apple);
		assertIterableEquals(blocked, alicePage.fetchBlockedViewers());
		assertFalse(alicePage.cantView(apple));
		assertTrue(alicePage.cantView(bob));

		assertFalse(alicePage.unblockViewer(apple));
		assertIterableEquals(blocked, alicePage.fetchBlockedViewers());
		assertFalse(alicePage.cantView(apple));
		assertTrue(alicePage.cantView(bob));

		assertTrue(alicePage.unblockViewer(bob));
		blocked.clear();
		assertIterableEquals(blocked, alicePage.fetchBlockedViewers());
		assertFalse(alicePage.cantView(apple));
		assertFalse(alicePage.cantView(bob));
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

		// making sure it terminates early properly
		alice.followUser(apple);
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
		SkillProficiency advancedJava = new SkillProficiency(java, SkillProficiency.ProficiencyLevel.ADVANCED,
				testManager);
		assertEquals(advancedJava, aliceJava);
		aliceSkills.add(advancedJava);
		assertEquals(aliceSkills, alice.getSkills());

		SkillProficiency alicePython = alice.addSkill(python, SkillProficiency.ProficiencyLevel.BEGINNER);
		SkillProficiency beginnerPython = new SkillProficiency(python, SkillProficiency.ProficiencyLevel.BEGINNER,
				testManager);
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

		alice.addJob("Mobile UX Design Lead", "Crafted new Apple layout for home page.", apple);
		assertEquals(aliceJobs, alice.getJobs());

		WorkExperience aliceBard = alice.addJob("Bard ML Engineer", "Launched the beta chatbot Bard.", google);
		WorkExperience bardEngineer = new WorkExperience("Bard ML Engineer", "Launched the beta chatbot Bard.", google,
				testManager);
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

		helloWorld.addContributor(alice);
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

		helloWorld.addCoordinator(alice);
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

		helloWorld.addCompany(apple);
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

	@Test
	void testSkillMentors()
	{
		ArrayList<Link> javaMentors = new ArrayList<Link>();
		assertEquals(javaMentors, java.getLinks().get("mentors"));

		java.addMentor(alice);
		Link aliceMentor = new Link(alicePage, Link.RelationshipType.MENTOR_PERSON, testManager);
		javaMentors.add(aliceMentor);
		assertEquals(javaMentors, java.getLinks().get("mentors"));

		java.addMentor(alice);
		assertEquals(javaMentors, java.getLinks().get("mentors"));

		java.addMentor(bob);
		Link bobMentor = new Link(bobPage, Link.RelationshipType.MENTOR_PERSON, testManager);
		javaMentors.add(bobMentor);
		assertEquals(javaMentors, java.getLinks().get("mentors"));

		java.removeMentor(bob);
		javaMentors.remove(bobMentor);
		assertEquals(javaMentors, java.getLinks().get("mentors"));

		java.removeMentor(bob);
		assertEquals(javaMentors, java.getLinks().get("mentors"));

		java.removeMentor(alice);
		javaMentors.clear();
		assertEquals(javaMentors, java.getLinks().get("mentors"));
	}

}
