package views;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static views.TestUtils.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VerticalDirection;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.Main;
import models.Company;
import models.JobPosting;
import models.Person;
import models.Skill;
import models.User;
import models.ViewTransitionHandlerInterface;
import models.adapters.Displayable;

@ExtendWith(ApplicationExtension.class)
class UserViewTest implements ViewTransitionHandlerInterface
{
	int showMainCalled;

	int showSearchDisplayCalled;
	ObservableList<Displayable> lastShownEntities;
	String lastShownLabel;

	int showProfileCalled;
	User lastShownUser;

	int showSkillCalled;
	Skill lastShownSkill;

	int showJobPostingCalled;
	JobPosting lastShownJobPosting;

	int showBlockErrorCalled;

	int showLoginCalled;

	User currentUser;
	UserController controller;

	@Start
	public void start(Stage stage)
	{
		showMainCalled = 0;
		showSearchDisplayCalled = 0;
		showProfileCalled = 0;
		showSkillCalled = 0;
		showJobPostingCalled = 0;
		showBlockErrorCalled = 0;
		showLoginCalled = 0;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../views/UserView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();
			initDatabase();

			controller = loader.getController();
			// we'll set models in tests using Platform.runLater to keep them in the FX
			// thread

			Scene s = new Scene(view);
			stage.setScene(s);
			stage.show();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void showMain(User user)
	{
		showMainCalled++;
		currentUser = user;
	}

	@Override
	public void showSearchDisplay(ObservableList<Displayable> entities, String newLabel)
	{
		showSearchDisplayCalled++;
		lastShownEntities = entities;
		lastShownLabel = newLabel;
	}

	@Override
	public void showProfile(User user)
	{
		showProfileCalled++;
		lastShownUser = user;
	}

	@Override
	public void showSkill(Skill skill)
	{
		showSkillCalled++;
		lastShownSkill = skill;
	}

	@Override
	public void showJobPosting(JobPosting job)
	{
		showJobPostingCalled++;
		lastShownJobPosting = job;
	}

	@Override
	public void showBlockError()
	{
		showBlockErrorCalled++;
	}

	@Override
	public void showLogin()
	{
		showLoginCalled++;
	}

	@Test
	void testBlocked(FxRobot robot)
	{
		// Bob is blocked by Google!
		Platform.runLater(() ->
		{
			controller.setModels(Company.retrieve(3), Person.retrieve(7), this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(showBlockErrorCalled).isEqualTo(1);
	}

	private void checkButtonDisabled(FxRobot robot, String target)
	{
		assertTrue(robot.lookup(target).queryAs(Button.class).isDisabled());
	}

	@Test
	void testCannotEdit(FxRobot robot)
	{
		// Bob can edit his own page, but not Alice's
		Platform.runLater(() ->
		{
			controller.setModels(Person.retrieve(5), Person.retrieve(7), this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		checkButtonDisabled(robot, "#editProfileButton");
		checkButtonDisabled(robot, "#addJobButton");
		checkButtonDisabled(robot, "#addSkillButton");
	}

	@Test
	void testDataLoaded(FxRobot robot)
	{
		// Alice can edit her own page
		Person alice = Person.retrieve(5);
		Platform.runLater(() ->
		{
			controller.setModels(alice, alice, this);
		});
		WaitForAsyncUtils.waitForFxEvents();

		// make sure add containers aren't visible until button clicked
		checkInvisible(robot, "#addJobContainer");
		checkInvisible(robot, "#addSkillContainer");

		// test that all the data is there
		checkLabel(robot, alice.getName(), "#nameLabel");
		checkLabel(robot, alice.getBio(), "#bioLabel");
		checkLabel(robot, alice.getPronouns(), "#pronounsLabel");

		// testing image is hard: let's just verify there is an image
		assertThat(robot.lookup("#profileImage").queryAs(ImageView.class).getImage()).isNotNull();
		assertThat(robot.lookup("#bannerImage").queryAs(ImageView.class).getImage()).isNotNull();

		checkListView(robot, FXCollections.observableArrayList(alice.getSkills()), "#skillsList");
		checkListView(robot, FXCollections.observableArrayList(alice.getJobs()), "#jobsList");

		checkLabel(robot, "1", "#numFollowersLabel");
		checkLabel(robot, "1", "#numFollowingLabel"); // we'll test on the company tests the case where they differ

		// test editing
		robot.clickOn("#editProfileButton");

		checkInvisible(robot, "#nameLabel");
		checkInvisible(robot, "#bioLabel");
		checkInvisible(robot, "#pronounsLabel");
		checkVisible(robot, "#nameTextField");
		checkVisible(robot, "#biographyTextArea");
		checkVisible(robot, "#pronounsTextField");

		enterText(robot, " Heart", "#nameTextField");
		enterText(robot, " From the river to the sea.", "#biographyTextArea");
		// let's make sure we can modify things too beyond just adding
		robot.clickOn("#pronounsTextField").push(KeyCode.RIGHT); // this is clicking in the center of the field; we need
																	// to move the caret
		robot.eraseText("her".length());
		robot.write("they");

		robot.clickOn("#editProfileButton"); // save changes

		// double check that local model is updated
		checkLabel(robot, alice.getName(), "#nameLabel");
		checkLabel(robot, alice.getBio(), "#bioLabel");
		checkLabel(robot, alice.getPronouns(), "#pronounsLabel");

		// rest out of viewport, so let's scroll down
		robot.scroll(20, VerticalDirection.UP);

		// now let's add work experiences/skills
		robot.clickOn("#addJobButton");
		checkVisible(robot, "#addJobContainer");

		robot.clickOn("#addSkillButton");
		checkVisible(robot, "#addSkillContainer");

		// check that cancel works
		robot.clickOn("#cancelJobButton");
		checkInvisible(robot, "#addJobContainer");

		robot.clickOn("#cancelSkillButton");
		checkInvisible(robot, "#addSkillContainer");

		robot.clickOn("#addJobButton");
		robot.clickOn("#addSkillButton");

		// check to make sure you can't submit them empty first (nonfatal)
		robot.clickOn("#submitJobButton");
		checkListView(robot, FXCollections.observableArrayList(alice.getJobs()), "#jobsList");

		robot.clickOn("#submitSkillButton");
		checkListView(robot, FXCollections.observableArrayList(alice.getSkills()), "#skillsList");

		// OK now finally test adding
		robot.clickOn("#addJobButton");
		enterText(robot, "Junior Software Developer", "#jobTitleTextField");
		enterText(robot, "Put out fires and learned fast.", "#jobDescriptionTextArea");
		selectFromChoiceBox(robot, 0, "#companySelector");
		robot.clickOn("#submitJobButton");
		checkListView(robot, FXCollections.observableArrayList(alice.getJobs()), "#jobsList");

		robot.clickOn("#addSkillButton"); // note that the way i've implemented addSkill means that the list stays the
											// same, but the skill level will change!
		selectFromChoiceBox(robot, 0, "#skillSelector");
		selectFromChoiceBox(robot, 2, "#proficiencyLevelSelector");
		robot.clickOn("#submitSkillButton");
		checkListView(robot, FXCollections.observableArrayList(alice.getSkills()), "#skillsList");

		// check the model is also updated on server
		Person aliceAfterUpdate = Person.retrieve(5);
		assertThat(aliceAfterUpdate.getName()).isEqualTo(alice.getName());
		assertThat(aliceAfterUpdate.getBio()).isEqualTo(alice.getBio());
		assertThat(aliceAfterUpdate.getPronouns()).isEqualTo(alice.getPronouns());

		assertThat(aliceAfterUpdate.getSkills()).isEqualTo(alice.getSkills());
		assertThat(aliceAfterUpdate.getJobs()).isEqualTo(alice.getJobs());
	}

	@Test
	void testCompanyDifferent(FxRobot robot)
	{
		// Alice is admin of Google so can edit it!
		Platform.runLater(() ->
		{
			controller.setModels(Company.retrieve(3), Person.retrieve(5), this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		// because a Company doesn't have extra fields (work experiences, skills),
		// we'll just make sure that those fields are hidden
		checkInvisible(robot, "#jobsContainer");
		checkInvisible(robot, "#skillsContainer");

		// test editing (mainly that you shouldn't be able to change a company's
		// pronouns)
		
	}

	@Test
	void testViewTransitions(FxRobot robot)
	{
		// we can transition to:
		// a Company from our work experiences
		// search display of all our Followers
		// a Skill from our skills
	}
}
