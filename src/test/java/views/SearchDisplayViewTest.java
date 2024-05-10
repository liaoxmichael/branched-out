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
import models.Company;
import models.JobPosting;
import models.Person;
import models.Skill;
import models.User;
import models.ViewTransitionHandlerInterface;
import models.adapters.Displayable;

@ExtendWith(ApplicationExtension.class)
public class SearchDisplayViewTest implements ViewTransitionHandlerInterface
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
	SearchDisplayController controller;

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
		loader.setLocation(Main.class.getResource("../views/SearchDisplayView.fxml"));

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
	void testSkills(FxRobot robot)
	{
		Platform.runLater(() ->
		{
			controller.setModels(FXCollections.observableArrayList(Skill.retrieveAll()), "Skills", this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		
		checkLabel(robot, "Skills", "#infoLabel");
		
		selectFromListView(robot, 0, "#listView");
		assertThat(showSkillCalled).isEqualTo(1);
		assertThat(lastShownSkill).isEqualTo(Skill.retrieve(11)); // python
		
		selectFromListView(robot, 1, "#listView");
		assertThat(showSkillCalled).isEqualTo(2);
		assertThat(lastShownSkill).isEqualTo(Skill.retrieve(9)); // java
	}
	
	@Test
	void testJobPostings(FxRobot robot)
	{
		Platform.runLater(() ->
		{
			controller.setModels(FXCollections.observableArrayList(JobPosting.retrieveAll()), "Job Postings", this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		
		checkLabel(robot, "Job Postings", "#infoLabel");
		
		selectFromListView(robot, 0, "#listView");
		assertThat(showJobPostingCalled).isEqualTo(1);
		assertThat(lastShownJobPosting).isEqualTo(JobPosting.retrieve(13)); // google dev
		
		selectFromListView(robot, 1, "#listView");
		assertThat(showJobPostingCalled).isEqualTo(2);
		assertThat(lastShownJobPosting).isEqualTo(JobPosting.retrieve(17)); // apple tech
	}
	
	@Test
	void testCompanies(FxRobot robot)
	{
		Platform.runLater(() ->
		{
			controller.setModels(FXCollections.observableArrayList(Company.retrieveAll()), "Companies", this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		
		checkLabel(robot, "Companies", "#infoLabel");
		
		selectFromListView(robot, 0, "#listView");
		assertThat(showProfileCalled).isEqualTo(1);
		assertThat(lastShownUser).isEqualTo(Company.retrieve(1)); // apple
		
		selectFromListView(robot, 1, "#listView");
		assertThat(showProfileCalled).isEqualTo(2);
		assertThat(lastShownUser).isEqualTo(Company.retrieve(3)); // google
	}
	
	@Test
	void testPeople(FxRobot robot)
	{
		Platform.runLater(() ->
		{
			controller.setModels(FXCollections.observableArrayList(Person.retrieveAll()), "People", this);
		});
		WaitForAsyncUtils.waitForFxEvents();
		
		checkLabel(robot, "People", "#infoLabel");
		
		selectFromListView(robot, 0, "#listView");
		assertThat(showProfileCalled).isEqualTo(1);
		assertThat(lastShownUser).isEqualTo(Person.retrieve(5)); // alice
		
		selectFromListView(robot, 1, "#listView");
		assertThat(showProfileCalled).isEqualTo(2);
		assertThat(lastShownUser).isEqualTo(Person.retrieve(7)); // bob
	}

}
