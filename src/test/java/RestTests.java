import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Company;
import models.IdentifiableObjectManager;
import models.IdentifiableObjectManagerInterface;
import models.JobPosting;
import models.Link;
import models.Page;
import models.Person;
import models.Project;
import models.Skill;
import models.SkillProficiency;
import models.WorkExperience;
import models.rest.ResponseListData;
import models.rest.ResponseData;
import models.rest.RestUtilities;

class RestTests
{

	RestClient client;
	ObjectMapper mapper;

	ResponseData team;

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

	JobPosting googleEngi;
	JobPosting appleTech;

	IdentifiableObjectManagerInterface testManager;

	void create(Record r, String uri) throws JsonMappingException, JsonProcessingException
	{
		String response = client.post().uri(uri).body(r).retrieve().body(String.class);
//		System.out.println(response);
		JsonNode finalResponse = mapper.readTree(response);
//		System.out.println(finalResponse.message);
		assertTrue(finalResponse.get("successful").asBoolean());
	}

	void checkData(ArrayList<ResponseData> dataList, String uri) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(uri).retrieve().body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			ResponseData d = response.data().get(i);
			assertEquals(dataList.get(i).name(), d.name());
			assertEquals(dataList.get(i).description(), d.description());
			assertEquals(dataList.get(i).location(), d.location());
		}
	}

	void checkCompanies(ArrayList<Company> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Company.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Company c = client.get().uri(response.data().get(i).location()).retrieve()
					.body(Company.ResponseRecord.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), c);
		}
	}

	void checkLinks(ArrayList<Link> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Link.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Link l = client.get().uri(response.data().get(i).location()).retrieve().body(Link.ResponseRecord.class)
					.data();
//			System.out.println(p);
			assertEquals(list.get(i), l);
		}
	}

	void checkPages(ArrayList<Page> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Page.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Page p = client.get().uri(response.data().get(i).location()).retrieve().body(Page.ResponseRecord.class)
					.data();
//			System.out.println(p);
			assertEquals(list.get(i), p);
		}
	}

	void checkPeople(ArrayList<Person> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Person.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Person p = client.get().uri(response.data().get(i).location()).retrieve().body(Person.ResponseRecord.class)
					.data();
//			System.out.println(p);
			assertEquals(list.get(i), p);
		}
	}

	void checkProjects(ArrayList<Project> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Project.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Project p = client.get().uri(response.data().get(i).location()).retrieve()
					.body(Project.ResponseRecord.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), p);
		}
	}

	void checkSkills(ArrayList<Skill> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Skill.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Skill s = client.get().uri(response.data().get(i).location()).retrieve().body(Skill.ResponseRecord.class)
					.data();
//			String p = client.get().uri(response.data().get(i).location()).retrieve().body(String.class);
//			System.out.println(p);
//			Skill.ResponseRecord rr = mapper.readValue(p, Skill.ResponseRecord.class);
//			Skill s = rr.data();
			assertEquals(list.get(i), s);
		}
	}

	void checkSkillProficiencies(ArrayList<SkillProficiency> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, SkillProficiency.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			SkillProficiency sp = client.get().uri(response.data().get(i).location()).retrieve()
					.body(SkillProficiency.ResponseRecord.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), sp);
		}
	}

	void checkWorkExperiences(ArrayList<WorkExperience> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, WorkExperience.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			WorkExperience we = client.get().uri(response.data().get(i).location()).retrieve()
					.body(WorkExperience.ResponseRecord.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), we);
		}
	}

	void checkJobPostings(ArrayList<JobPosting> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, JobPosting.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		ResponseListData response = mapper.readValue(r, ResponseListData.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			JobPosting jp = client.get().uri(response.data().get(i).location()).retrieve()
					.body(JobPosting.ResponseRecord.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), jp);
		}
	}

	boolean existsOnServer(String uri) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(uri).retrieve().body(String.class);
		System.out.println(r);
		JsonNode finalResponse = mapper.readTree(r);
//		System.out.println(finalResponse.message);
		return finalResponse.get("successful").asBoolean();
	}

	@BeforeEach
	void setUp() throws Exception
	{
		// make sure to run your server beforehand!
		// worth running UtilsTest before as well
		client = RestClient.create();
		mapper = new ObjectMapper();

		team = new ResponseData(RestUtilities.TEAM_NAME, RestUtilities.TEAM_DESC, RestUtilities.TEAM_URI);

		// delete existing team for ease of testing
		client.delete().uri(RestUtilities.TEAM_URI).retrieve().body(String.class);
//		System.out.println(result);

		// recreate now!
		create(team, RestUtilities.TEAM_URI);

		// instantiating all the objects to test with
		testManager = new IdentifiableObjectManager(); // 0

		apple = new Company("Apple", "tim.cook@apple.com", testManager); // 1
		applePage = apple.getPage(); // 2

		google = new Company("Google", "sundar.pichai@google.com", testManager); // 3
		googlePage = google.getPage(); // 4

		alice = new Person("Alice", "ateam@gmail.com", testManager); // 5
		alicePage = alice.getPage(); // 6

		bob = new Person("Bob", "bobert33@yahoo.com", testManager); // 7
		bobPage = bob.getPage(); // 8

		java = new Skill("Java", testManager); // 9
		javaPage = java.getPage(); // 10

		python = new Skill("Python", testManager); // 11
		pythonPage = python.getPage(); // 12

		helloWorld = new Project("Hello World", testManager); // 13
		helloPage = helloWorld.getPage(); // 14

		myFirstProgram = new Project("My First Program", testManager); // 15
		firstPage = myFirstProgram.getPage(); // 16
	}

	@Test
	void testJobPostings() throws JsonMappingException, JsonProcessingException
	{
		googleEngi = new JobPosting("Senior Software Developer", google, testManager); // 17
		appleTech = new JobPosting("Apple Genius Technician", apple, testManager); // 21

		ArrayList<JobPosting> jobPostings = new ArrayList<>();
		jobPostings.add(googleEngi);
		jobPostings.add(appleTech);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, JobPosting.RESOURCE)));
		checkJobPostings(jobPostings);
		assertEquals(jobPostings, JobPosting.retrieveAll());

		assertEquals(googleEngi, JobPosting.retrieve(17));
		assertEquals(appleTech, JobPosting.retrieve(21));

		assertNull(JobPosting.retrieve(10));
		assertTrue(applePage.update());
		assertNull(JobPosting.retrieve(2));
		assertNull(JobPosting.retrieve(-1));
	}

	@Test
	void testCompany() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Company> companies = new ArrayList<>();
		companies.add(apple);
		companies.add(google);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Company.RESOURCE)));
		checkCompanies(companies);
		assertEquals(companies, Company.retrieveAll());

		assertEquals(google, Company.retrieve(3));
		assertEquals(apple, Company.retrieve(1));

		assertNull(Company.retrieve(10));
		assertTrue(apple.update());
		assertNull(Company.retrieve(2));
		assertNull(Company.retrieve(-1));
	}

	@Test
	void testLink() throws JsonMappingException, JsonProcessingException
	{
		Link aliceMentor = new Link(alicePage, Link.RelationshipType.MENTOR_PERSON, testManager); // 17
		Link bobMentor = new Link(bobPage, Link.RelationshipType.MENTOR_PERSON, testManager); // 18

		ArrayList<Link> links = new ArrayList<>();
		links.add(aliceMentor);
		links.add(bobMentor);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Link.RESOURCE)));
		checkLinks(links);
		assertEquals(links, Link.retrieveAll());

		assertEquals(aliceMentor, Link.retrieve(17));
		assertEquals(bobMentor, Link.retrieve(18));

		assertNull(Link.retrieve(10));
		assertTrue(applePage.update());
		assertNull(Link.retrieve(2));
		assertNull(Link.retrieve(-1));
	}

	@Test
	void testPage() throws JsonMappingException, JsonProcessingException
	{
		

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Page.RESOURCE)));
		assertEquals(8, Page.retrieveAll().size());

		assertEquals(applePage, Page.retrieve(2));
		assertEquals(googlePage, Page.retrieve(4));

		assertNull(Page.retrieve(200));
		assertTrue(apple.update());
		assertNull(Page.retrieve(1));
		assertNull(Page.retrieve(-1));
	}

	@Test
	void testPerson() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Person> people = new ArrayList<>();
		people.add(alice);
		people.add(bob);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Person.RESOURCE)));
		checkPeople(people);
		assertEquals(people, Person.retrieveAll());

		assertEquals(alice, Person.retrieve(5));
		assertEquals(bob, Person.retrieve(7));

		assertNull(Person.retrieve(10));
		assertTrue(applePage.update());
		assertNull(Person.retrieve(2));
		assertNull(Person.retrieve(-1));
	}

	@Test
	void testProject() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Project> projects = new ArrayList<>();
		projects.add(helloWorld);
		projects.add(myFirstProgram);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Project.RESOURCE)));
		checkProjects(projects);
		assertEquals(projects, Project.retrieveAll());

		assertEquals(helloWorld, Project.retrieve(13));
		assertEquals(myFirstProgram, Project.retrieve(15));

		assertNull(Project.retrieve(10));
		assertTrue(applePage.update());
		assertNull(Project.retrieve(2));
		assertNull(Project.retrieve(-1));
	}

	@Test
	void testSkill() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Skill> skills = new ArrayList<>();
		skills.add(python);
		skills.add(java);
		

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Skill.RESOURCE)));
		checkSkills(skills);
		assertEquals(skills, Skill.retrieveAll());

		// Testing retrieval
		assertEquals(java, Skill.retrieve(9));
		assertEquals(python, Skill.retrieve(11));

		assertNull(Skill.retrieve(10));
		assertTrue(applePage.update());
		assertNull(Skill.retrieve(2));
		assertNull(Skill.retrieve(-1));
	}

	@Test
	void testSkillProficiency() throws JsonMappingException, JsonProcessingException
	{
		SkillProficiency javaExperienced = new SkillProficiency(java, SkillProficiency.ProficiencyLevel.ADVANCED,
				testManager);
		SkillProficiency pythonExperienced = new SkillProficiency(python, SkillProficiency.ProficiencyLevel.ADVANCED,
				testManager);
		SkillProficiency pythonBeginner = new SkillProficiency(python, SkillProficiency.ProficiencyLevel.BEGINNER,
				testManager);

		ArrayList<SkillProficiency> profs = new ArrayList<>();
		profs.add(javaExperienced);
		profs.add(pythonExperienced);
		profs.add(pythonBeginner);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, SkillProficiency.RESOURCE)));
		checkSkillProficiencies(profs);
		assertEquals(profs, SkillProficiency.retrieveAll());

		assertEquals(javaExperienced, SkillProficiency.retrieve(17));
		assertEquals(pythonExperienced, SkillProficiency.retrieve(18));
		assertEquals(pythonBeginner, SkillProficiency.retrieve(19));

		assertNull(SkillProficiency.retrieve(9));
		assertTrue(applePage.update());
		assertNull(SkillProficiency.retrieve(2));
		assertNull(SkillProficiency.retrieve(-1));
	}

	@Test
	void testWorkExperience() throws JsonMappingException, JsonProcessingException
	{
		WorkExperience mobileUX = new WorkExperience("Mobile UX Design Lead", "Crafted new Apple layout for home page.",
				apple, testManager); // 17
		WorkExperience bardEngineer = new WorkExperience("Bard ML Engineer", "Launched the beta chatbot Bard.", google,
				testManager); // 19

		ArrayList<WorkExperience> jobs = new ArrayList<>();
		jobs.add(mobileUX);
		jobs.add(bardEngineer);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, WorkExperience.RESOURCE)));
		checkWorkExperiences(jobs);
		assertEquals(jobs, WorkExperience.retrieveAll());

		// Testing retrieval
		assertEquals(mobileUX, WorkExperience.retrieve(17));
		assertEquals(bardEngineer, WorkExperience.retrieve(19));

		assertNull(WorkExperience.retrieve(9));
		assertTrue(applePage.update());
		assertNull(WorkExperience.retrieve(2));
		assertNull(WorkExperience.retrieve(-1));
	}
}
