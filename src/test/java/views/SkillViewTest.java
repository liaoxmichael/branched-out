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
import javafx.stage.Stage;
import main.Main;
import models.JobPosting;
import models.Link;
import models.Person;
import models.Skill;
import models.User;
import models.ViewTransitionHandlerInterface;
import models.adapters.Displayable;

@ExtendWith(ApplicationExtension.class)
class SkillViewTest implements ViewTransitionHandlerInterface
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
	SkillController controller;

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
		loader.setLocation(Main.class.getResource("../views/SkillView.fxml"));

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
	void testEditing(FxRobot robot)
	{
		// Alice can edit Java
		Skill java = Skill.retrieve(9);
		Platform.runLater(() ->
		{
			controller.setModels(java, Person.retrieve(5), this);
		});
		WaitForAsyncUtils.waitForFxEvents();

		// make sure add containers aren't visible until button clicked
		checkInvisible(robot, "#addMentorContainer");

		// test that all the data is there
		checkLabel(robot, java.getTitle(), "#nameLabel");
		checkLabel(robot, java.getDescription(), "#descriptionLabel");
		
		ObservableList<Person> mentors = FXCollections.observableArrayList();
		for (Link l : java.getLinks().get("mentors"))
		{
			int i = l.fetchPage().getEntityId();
			if (Person.retrieve(i) != null)
			{
				mentors.add(Person.retrieve(i));
			}
		}
		
		checkListView(robot, mentors, "#mentorList");
		
		// testing editing
		robot.clickOn("#editButton");
		
		checkInvisible(robot, "#nameLabel");
		checkInvisible(robot, "#descriptionLabel");
		checkVisible(robot, "#nameTextField");
		checkVisible(robot, "#descriptionTextArea");
		
		enterText(robot, " 21", "#nameTextField");
		clearTextArea(robot, "#descriptionTextArea");
		enterText(robot, "Now with support for JavaScript syntax!", "#descriptionTextArea");
		
		robot.clickOn("#editButton"); // save changes
		checkLabel(robot, java.getTitle(), "#nameLabel");
		checkLabel(robot, java.getDescription(), "#descriptionLabel");
		
		robot.clickOn("#addMentorButton");
		checkVisible(robot, "#addMentorContainer");
		
		robot.clickOn("#cancelMentorButton");
		checkInvisible(robot, "#addMentorContainer");
		
		robot.clickOn("#addMentorButton");
		robot.clickOn("#submitMentorButton"); // submitting empty case
		checkListView(robot, mentors, "#mentorList");
		
		robot.clickOn("#addMentorButton"); // now let's test adding stuff
		selectFromChoiceBox(robot, 1, "#peopleSelector"); // bob
		robot.clickOn("#submitMentorButton");
		
		mentors.clear();
		for (Link l : java.getLinks().get("mentors"))
		{
			int i = l.fetchPage().getEntityId();
			if (Person.retrieve(i) != null)
			{
				mentors.add(Person.retrieve(i));
			}
		}
		
		checkListView(robot, mentors, "#mentorList");
		
		// check server update
		Skill javaAfterUpdate = Skill.retrieve(9);
		assertThat(javaAfterUpdate.getTitle()).isEqualTo(java.getTitle());
		assertThat(javaAfterUpdate.getDescription()).isEqualTo(java.getDescription());
		assertThat(javaAfterUpdate.getLinks().get("mentors")).isEqualTo(java.getLinks().get("mentors"));
	}

	@Test
	void testCannotEdit(FxRobot robot)
	{
		// Bob cannot edit Java
		Platform.runLater(() ->
		{
			controller.setModels(Skill.retrieve(9), Person.retrieve(7), this);
		});
		WaitForAsyncUtils.waitForFxEvents();

		checkButtonDisabled(robot, "#editButton");
		checkButtonDisabled(robot, "#addMentorButton");
	}

	@Test
	void testViewTransitions(FxRobot robot)
	{
		Platform.runLater(() ->
		{
			controller.setModels(Skill.retrieve(9), Person.retrieve(7), this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		// click on mentor to go to their page
		selectFromListView(robot, 0, "#mentorList");
		assertThat(showProfileCalled).isEqualTo(1);
		assertThat(lastShownUser).isEqualTo(Person.retrieve(5)); // should be alice
	}

}
