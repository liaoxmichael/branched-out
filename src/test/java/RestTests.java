import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	JobRecommenderInterface testRecommender;

	void create(Record r, String uri) throws JsonMappingException, JsonProcessingException
	{
		String response = client.post().uri(uri).body(r).retrieve().body(String.class);
		System.out.println(response);
		JsonNode finalResponse = mapper.readTree(response);
//		System.out.println(finalResponse.message);
		assertTrue(finalResponse.get("successful").asBoolean());
	}

	void checkData(ArrayList<ResponseData> dataList, String uri) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(uri).retrieve().body(String.class);
		System.out.println(r);
		Response response = mapper.readValue(r, Response.class);
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
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Company c = client.get().uri(response.data().get(i).location()).retrieve()
					.body(Company.CompanyResponse.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), c);
		}
	}

	void checkLinks(ArrayList<Link> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Link.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Link l = client.get().uri(response.data().get(i).location()).retrieve().body(Link.LinkResponse.class)
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
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Page p = client.get().uri(response.data().get(i).location()).retrieve().body(Page.PageResponse.class)
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
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Person p = client.get().uri(response.data().get(i).location()).retrieve().body(Person.PersonResponse.class)
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
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Project p = client.get().uri(response.data().get(i).location()).retrieve()
					.body(Project.ProjectResponse.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), p);
		}
	}

	void checkSkills(ArrayList<Skill> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, Skill.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			Skill s = client.get().uri(response.data().get(i).location()).retrieve().body(Skill.SkillResponse.class)
					.data();
//			System.out.println(p);
			assertEquals(list.get(i), s);
		}
	}

	void checkSkillProficiencies(ArrayList<SkillProficiency> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, SkillProficiency.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			SkillProficiency sp = client.get().uri(response.data().get(i).location()).retrieve()
					.body(SkillProficiency.SkillProficiencyResponse.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), sp);
		}
	}

	void checkWorkExperiences(ArrayList<WorkExperience> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, WorkExperience.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			WorkExperience we = client.get().uri(response.data().get(i).location()).retrieve()
					.body(WorkExperience.WorkExperienceResponse.class).data();
//			System.out.println(p);
			assertEquals(list.get(i), we);
		}
	}
	
	void checkJobPostings(ArrayList<JobPosting> list) throws JsonMappingException, JsonProcessingException
	{
		String r = client.get().uri(RestUtilities.join(RestUtilities.TEAM_URI, JobPosting.RESOURCE)).retrieve()
				.body(String.class);
		System.out.println(r);
		Response response = mapper.readValue(r, Response.class);
		for (int i = 0; i < response.data().size(); i++)
		{
			JobPosting jp = client.get().uri(response.data().get(i).location()).retrieve()
					.body(JobPosting.JobPostingResponse.class).data();
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
		String result = client.delete().uri(RestUtilities.TEAM_URI).retrieve().body(String.class);
		System.out.println(result);

		// recreate now!
		create(team, RestUtilities.TEAM_URI);

		// instantiating all the objects to test with
		testManager = new IdentifiableObjectManager();
		testRecommender = new JobRecommender();

		apple = new Company("Apple", "tim.cook@apple.com", testManager); // 0
		applePage = apple.getPage(); // 1

		google = new Company("Google", "sundar.pichai@google.com", testManager); // 2
		googlePage = google.getPage(); // 3

		alice = new Person("Alice", "ateam@gmail.com", testRecommender, testManager); // 4
		alicePage = alice.getPage(); // 5

		bob = new Person("Bob", "bobert33@yahoo.com", testRecommender, testManager); // 6
		bobPage = bob.getPage(); // 7

		java = new Skill("Java", testManager); // 8
		javaPage = java.getPage(); // 9

		python = new Skill("Python", testManager); // 10
		pythonPage = python.getPage(); // 11

		helloWorld = new Project("Hello World", testManager); // 12
		helloPage = helloWorld.getPage(); // 13

		myFirstProgram = new Project("My First Program", testManager); // 14
		firstPage = myFirstProgram.getPage(); // 15
	}

	@Test
	void testJobPostings() throws JsonMappingException, JsonProcessingException
	{
		googleEngi = new JobPosting("Senior Software Developer", google, testRecommender, testManager); // 16
		
		appleTech = new JobPosting("Apple Genius Technician", apple, testRecommender, testManager); // 19
		
		ArrayList<JobPosting> jobPostings = new ArrayList<JobPosting>();
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, JobPosting.RESOURCE)));

		assertTrue(googleEngi.store()); // should create the directory AND add it
		jobPostings.add(googleEngi);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, JobPosting.RESOURCE))); // check if directory
																									// exists
		checkJobPostings(jobPostings);

		assertFalse(googleEngi.store()); // shouldn't be able to store something already stored

		assertTrue(appleTech.store()); // now checking multiple things
		jobPostings.add(appleTech);
		checkJobPostings(jobPostings);

		// testing retrieval
		assertEquals(googleEngi, JobPosting.retrieve(16));
		assertEquals(appleTech, JobPosting.retrieve(19));

		assertNull(JobPosting.retrieve(10)); // something that hasn't been stored yet; should return null as failure

		// let's store something that isn't a company and try to retrieve it using
		// Page.retrieve
		assertTrue(applePage.store());
		assertNull(JobPosting.retrieve(1)); // company != page

		assertNull(JobPosting.retrieve(-1)); // should not exist
	}

	@Test
	void testCompany() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Company> companies = new ArrayList<Company>();

		// testing storage
		// currently no resource directory should exist; let's make sure
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Company.RESOURCE)));

		assertTrue(apple.store()); // should create the directory AND add it
		companies.add(apple);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Company.RESOURCE))); // check if directory
																									// exists
		checkCompanies(companies);

		assertFalse(apple.store()); // shouldn't be able to store something already stored

		assertTrue(google.store()); // now checking multiple things
		companies.add(google);
		checkCompanies(companies);

		// testing retrieval
		assertEquals(apple, Company.retrieve(0)); // recall - apple id = 0
		assertEquals(google, Company.retrieve(2));

		assertNull(Company.retrieve(10)); // something that hasn't been stored yet; should return null as failure

		// let's store something that isn't a company and try to retrieve it using
		// Page.retrieve
		assertTrue(applePage.store());
		assertNull(Company.retrieve(1)); // company != page

		assertNull(Company.retrieve(-1)); // should not exist
	}

	@Test
	void testLink() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Link> links = new ArrayList<Link>();

		// need to make some links to test!
		Link aliceMentor = new Link(alicePage, Link.RelationshipType.MENTOR_PERSON, testManager); // 16
		Link bobMentor = new Link(bobPage, Link.RelationshipType.MENTOR_PERSON, testManager); // 17

		// testing storage
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Link.RESOURCE)));

		assertTrue(aliceMentor.store());
		links.add(aliceMentor);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Link.RESOURCE)));
		checkLinks(links);

		assertFalse(aliceMentor.store());

		assertTrue(bobMentor.store());
		links.add(bobMentor);
		checkLinks(links);

		// testing retrieval
		assertEquals(aliceMentor, Link.retrieve(16));
		assertEquals(bobMentor, Link.retrieve(17));

		assertNull(Link.retrieve(10));

		assertTrue(applePage.store());
		assertNull(Link.retrieve(1)); // link != page

		assertNull(Link.retrieve(-1));
	}

	@Test
	void testPage() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Page> pages = new ArrayList<Page>();

		// testing storage
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Page.RESOURCE)));

		assertTrue(applePage.store());
		pages.add(applePage);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Page.RESOURCE))); // check if directory
																								// exists
		checkPages(pages);

		assertFalse(applePage.store());

		assertTrue(googlePage.store());
		pages.add(googlePage);
		checkPages(pages);

		// testing retrieval
		assertEquals(applePage, Page.retrieve(1)); // recall - applePage id = 1
		assertEquals(googlePage, Page.retrieve(3));

		assertNull(Page.retrieve(10));

		assertTrue(apple.store()); // hehe apple store
		assertNull(Page.retrieve(0)); // page != company

		assertNull(Page.retrieve(-1));
	}

	@Test
	void testPerson() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Person> people = new ArrayList<Person>();

		// testing storage
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Person.RESOURCE)));

		assertTrue(alice.store());
		people.add(alice);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Person.RESOURCE)));
		checkPeople(people);

		assertFalse(alice.store());

		assertTrue(bob.store());
		people.add(bob);
		checkPeople(people);

		// testing retrieval
		assertEquals(alice, Person.retrieve(4));
		assertEquals(bob, Person.retrieve(6));

		assertNull(Person.retrieve(10));

		assertTrue(applePage.store());
		assertNull(Person.retrieve(1)); // person != page

		assertNull(Person.retrieve(-1));
	}

	@Test
	void testProject() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Project> projects = new ArrayList<Project>();

		// testing storage
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Project.RESOURCE)));

		assertTrue(helloWorld.store());
		projects.add(helloWorld);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Project.RESOURCE)));
		checkProjects(projects);

		assertFalse(helloWorld.store());

		assertTrue(myFirstProgram.store());
		projects.add(myFirstProgram);
		checkProjects(projects);

		// testing retrieval
		assertEquals(helloWorld, Project.retrieve(12));
		assertEquals(myFirstProgram, Project.retrieve(14));

		assertNull(Project.retrieve(10));

		assertTrue(applePage.store());
		assertNull(Project.retrieve(1)); // project != page

		assertNull(Project.retrieve(-1));
	}

	@Test
	void testSkill() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<Skill> skills = new ArrayList<Skill>();

		// testing storage
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Skill.RESOURCE)));

		assertTrue(java.store());
		skills.add(java);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, Skill.RESOURCE)));
		checkSkills(skills);

		assertFalse(java.store());

		assertTrue(python.store());
		skills.add(python);
		checkSkills(skills);

		// testing retrieval
		assertEquals(java, Skill.retrieve(8));
		assertEquals(python, Skill.retrieve(10));

		assertNull(Skill.retrieve(9));

		assertTrue(applePage.store());
		assertNull(Skill.retrieve(1)); // skill != page

		assertNull(Skill.retrieve(-1));
	}

	@Test
	void testSkillProficiency() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<SkillProficiency> profs = new ArrayList<SkillProficiency>();

		// need to make some profs to test!
		SkillProficiency javaExperienced = new SkillProficiency(java, SkillProficiency.ProficiencyLevel.ADVANCED,
				testManager); // 16
		SkillProficiency pythonExperienced = new SkillProficiency(python, SkillProficiency.ProficiencyLevel.ADVANCED,
				testManager); // 17
		SkillProficiency pythonBeginner = new SkillProficiency(python, SkillProficiency.ProficiencyLevel.BEGINNER,
				testManager); // 18

		// testing storage
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, SkillProficiency.RESOURCE)));

		assertTrue(javaExperienced.store());
		profs.add(javaExperienced);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, SkillProficiency.RESOURCE)));
		checkSkillProficiencies(profs);

		assertFalse(javaExperienced.store());

		assertTrue(pythonExperienced.store());
		profs.add(pythonExperienced);
		checkSkillProficiencies(profs);

		// just want to test same-skill diff-level b/c SkillProficiencies have
		// interesting equality
		assertTrue(pythonBeginner.store());
		profs.add(pythonBeginner);
		checkSkillProficiencies(profs);

		// testing retrieval
		assertEquals(javaExperienced, SkillProficiency.retrieve(16));
		assertEquals(pythonExperienced, SkillProficiency.retrieve(17));
		assertEquals(pythonBeginner, SkillProficiency.retrieve(18));

		assertNull(SkillProficiency.retrieve(9));

		assertTrue(applePage.store());
		assertNull(SkillProficiency.retrieve(1)); // skill proficiency != page

		assertNull(SkillProficiency.retrieve(-1));
	}

	@Test
	void testWorkExperience() throws JsonMappingException, JsonProcessingException
	{
		ArrayList<WorkExperience> jobs = new ArrayList<WorkExperience>();

		// need to make some jobs to test!
		WorkExperience mobileUX = new WorkExperience("Mobile UX Design Lead", "Crafted new Apple layout for home page.",
				apple, testManager); // 16
		WorkExperience bardEngineer = new WorkExperience("Bard ML Engineer", "Launched the beta chatbot Bard.", google,
				testManager); // 18 because 17 taken by Link created

		// testing storage
		assertFalse(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, WorkExperience.RESOURCE)));

		assertTrue(mobileUX.store());
		jobs.add(mobileUX);

		assertTrue(existsOnServer(RestUtilities.join(RestUtilities.TEAM_URI, WorkExperience.RESOURCE)));
		checkWorkExperiences(jobs);

		assertFalse(mobileUX.store());

		assertTrue(bardEngineer.store());
		jobs.add(bardEngineer);
		checkWorkExperiences(jobs);

		// testing retrieval
		assertEquals(mobileUX, WorkExperience.retrieve(16));
		assertEquals(bardEngineer, WorkExperience.retrieve(18));

		assertNull(WorkExperience.retrieve(9));

		assertTrue(applePage.store());
		assertNull(WorkExperience.retrieve(1)); // work experience != page

		assertNull(WorkExperience.retrieve(-1));
	}
}
