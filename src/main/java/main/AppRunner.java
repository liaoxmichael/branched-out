package main;

import org.springframework.web.client.RestClient;

import models.Company;
import models.IdentifiableObjectManager;
import models.Person;
import models.Project;
import models.Skill;
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

		client.delete().uri(RestUtilities.TEAM_URI);

		System.out.println(client.post().uri(RestUtilities.TEAM_URI).body(team).retrieve().body(String.class));
		IdentifiableObjectManager testManager = new IdentifiableObjectManager(); // 0

		new Company("Apple", "tim.cook@apple.com", testManager); // 1
		new Company("Google", "sundar.pichai@google.com", testManager); // 3
		new Person("Alice", "ateam@gmail.com", testManager); // 5
		new Person("Bob", "bobert33@yahoo.com", testManager); // 7
		new Skill("Java", testManager); // 9
		new Skill("Python", testManager); // 11
		new Project("Hello World", testManager); // 13
		new Project("My First Program", testManager); // 15
	}

}
