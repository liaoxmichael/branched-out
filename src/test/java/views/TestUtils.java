package views;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.client.RestClient;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class TestUtils
{

	private TestUtils() // do not instantiate
	{
	}

	public static void initDatabase()
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

		googleDev.fetchPage().addEditor(alice);
	}

	public static void enterText(FxRobot robot, String input, String target)
	{
		robot.clickOn(target);
		robot.write(input);
	}

	public static void clearTextField(FxRobot robot, String target)
	{
		robot.lookup(target).queryAs(TextField.class).clear();
	}
	
	public static void clearTextArea(FxRobot robot, String target)
	{
		robot.lookup(target).queryAs(TextArea.class).clear();
	}

	public static void checkLabel(FxRobot robot, String expected, String target)
	{
		Assertions.assertThat(robot.lookup(target).queryAs(Label.class)).hasText(expected);
	}

	private static void checkVisibility(FxRobot robot, boolean checkIsVisible, String target)
	{
		Node n = robot.lookup(target).queryAs(Node.class);
		if (checkIsVisible)
		{
			Assertions.assertThat(n).isVisible();
		} else
		{
			Assertions.assertThat(n).isInvisible();
		}
	}

	public static void checkVisible(FxRobot robot, String target)
	{
		checkVisibility(robot, true, target);
	}

	public static void checkInvisible(FxRobot robot, String target)
	{
		checkVisibility(robot, false, target);
	}

	// how do you select something from a choicebox?
	// source: https://github.com/TestFX/TestFX/issues/216#issuecomment-785450678
	public static void selectFromChoiceBox(FxRobot robot, int index, String target)
	{
		robot.interact(() ->
		{
			robot.lookup(target).queryAs(ChoiceBox.class).getSelectionModel().select(index);
		});
	}

	public static void selectFromListView(FxRobot robot, int index, String target)
	{
		robot.interact(() ->
		{
			robot.lookup(target).queryAs(ListView.class).getSelectionModel().select(index);
		});
	}

	public static void checkListView(FxRobot robot, ObservableList<?> expected, String target)
	{
		assertIterableEquals(expected, robot.lookup(target).queryAs(ListView.class).getItems());
	}

}
