package views;

import static org.assertj.core.api.Assertions.*;
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
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
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
public class JobPostingViewTest implements ViewTransitionHandlerInterface
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
	JobPostingController controller;

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
		loader.setLocation(Main.class.getResource("../views/JobPostingView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();
			initDatabase();

			controller = loader.getController();

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
	void testDataExistsAndEditing(FxRobot robot)
	{
		// Alice can edit googleDev
		JobPosting googleDev = JobPosting.retrieve(13);
		Platform.runLater(() ->
		{
			controller.setModels(googleDev, Person.retrieve(5), this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		checkInvisible(robot, "#addSkillContainer");

		// check data is there
		checkLabel(robot, googleDev.getTitle(), "#titleLabel");
		checkLabel(robot, googleDev.getType().label, "#typeLabel");
		checkLabel(robot, googleDev.getSite().label, "#siteLabel");
		checkLabel(robot, googleDev.fetchCompany().getName(), "#companyNameLabel");
		checkLabel(robot, googleDev.getLocation(), "#locationLabel");
		checkLabel(robot, "1", "#numApplicantsLabel");
		checkLabel(robot, googleDev.getDescription(), "#descriptionLabel");

		assertThat(robot.lookup("#logoImage").queryAs(ImageView.class).getImage()).isNotNull();

		checkListView(robot, FXCollections.observableArrayList(googleDev.getRequiredSkills()), "#skillsList");

		// test editing
		robot.clickOn("#editJobPostingButton");
		checkInvisible(robot, "#titleLabel");
		checkInvisible(robot, "#typeLabel");
		checkInvisible(robot, "#siteLabel");
		checkInvisible(robot, "#locationLabel");
		checkInvisible(robot, "#descriptionLabel");

		checkVisible(robot, "#titleTextField");
		checkVisible(robot, "#typeSelector");
		checkVisible(robot, "#siteSelector");
		checkVisible(robot, "#locationTextField");
		checkVisible(robot, "#descriptionTextArea");

		enterText(robot, " and Janitor", "#titleTextField");
		selectFromChoiceBox(robot, 0, "#typeSelector"); // internship
		selectFromChoiceBox(robot, 1, "#siteSelector"); // hybrid
		clearTextField(robot, "#locationTextField");
		enterText(robot, "Seattle, WA", "#locationTextField");
		enterText(robot, " Also, maybe clean some of our new toilets.", "#descriptionTextArea");

		robot.clickOn("#editJobPostingButton");

		checkLabel(robot, googleDev.getTitle(), "#titleLabel");
		checkLabel(robot, googleDev.getType().label, "#typeLabel");
		checkLabel(robot, googleDev.getSite().label, "#siteLabel");
		checkLabel(robot, googleDev.getLocation(), "#locationLabel");
		checkLabel(robot, googleDev.getDescription(), "#descriptionLabel");

		// add skills
		robot.clickOn("#addSkillButton");
		checkVisible(robot, "#addSkillContainer");

		robot.clickOn("#cancelSkillButton");
		checkInvisible(robot, "#addSkillContainer");

		robot.clickOn("#addSkillButton");
		robot.clickOn("#submitSkillButton");
		checkListView(robot, FXCollections.observableArrayList(googleDev.getRequiredSkills()), "#skillsList");

		robot.clickOn("#addSkillButton"); // note that the way i've implemented addSkill means that the list stays the
		// same, but the skill level will change!
		selectFromChoiceBox(robot, 0, "#skillSelector"); // python
		selectFromChoiceBox(robot, 2, "#proficiencyLevelSelector"); // advanced
		robot.clickOn("#submitSkillButton");
		checkListView(robot, FXCollections.observableArrayList(googleDev.getRequiredSkills()), "#skillsList");
		
		// check server updated
		JobPosting googleDevAfterUpdate = JobPosting.retrieve(13);
		assertThat(googleDevAfterUpdate.getTitle()).isEqualTo(googleDev.getTitle());
		assertThat(googleDevAfterUpdate.getType()).isEqualTo(googleDev.getType());
		assertThat(googleDevAfterUpdate.getSite()).isEqualTo(googleDev.getSite());
		assertThat(googleDevAfterUpdate.getLocation()).isEqualTo(googleDev.getLocation());
		assertThat(googleDevAfterUpdate.getDescription()).isEqualTo(googleDev.getDescription());
		assertThat(googleDevAfterUpdate.getRequiredSkills()).isEqualTo(googleDev.getRequiredSkills());
	}

	@Test
	void testCannotEdit(FxRobot robot)
	{
		// Bob cannot edit googleDev
		Platform.runLater(() ->
		{
			controller.setModels(JobPosting.retrieve(13), Person.retrieve(7), this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		checkButtonDisabled(robot, "#addSkillButton");
		checkButtonDisabled(robot, "#editJobPostingButton");
	}

	@Test
	void testViewTransitions(FxRobot robot)
	{
		// can go to company from logo or linked name
		// and can go to skill from required skills
		Platform.runLater(() ->
		{
			controller.setModels(JobPosting.retrieve(13), Person.retrieve(7), this);
		});

		robot.clickOn("#logoButton");
		assertThat(showProfileCalled).isEqualTo(1);
		assertThat(lastShownUser).isEqualTo(Company.retrieve(3)); // google

		robot.clickOn("#companyNameLabel");
		assertThat(showProfileCalled).isEqualTo(2);
		assertThat(lastShownUser).isEqualTo(Company.retrieve(3)); // google

		selectFromListView(robot, 0, "#skillsList");
		assertThat(showSkillCalled).isEqualTo(1);
		assertThat(lastShownSkill).isEqualTo(Skill.retrieve(9)); // java
	}

}
