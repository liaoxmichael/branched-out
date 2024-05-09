package views;

import static org.assertj.core.api.Assertions.*;
import static views.TestUtils.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
public class NavBarViewTest implements ViewTransitionHandlerInterface
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
		loader.setLocation(Main.class.getResource("../views/NavBarView.fxml"));

		Pane view;
		try
		{
			view = loader.load();
			initDatabase();

			NavBarController controller = loader.getController();
			// need to arbitrarily choose a user to load in with for controller, will matter
			// more in other views (using Alice)
			currentUser = Person.retrieve(5);
			controller.setModels(currentUser, this);

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
	void testProfileShowing(FxRobot robot)
	{
		assertThat(showProfileCalled).isEqualTo(1); // should display a profile (our profile) right away
		assertThat(lastShownUser).isEqualTo(currentUser); // make sure it's our own user!

		robot.clickOn("#homeButton");
		assertThat(showProfileCalled).isEqualTo(2);
		assertThat(lastShownUser).isEqualTo(currentUser);
	}

	@Test
	void testLogout(FxRobot robot)
	{
		robot.clickOn("#logoutButton");
		assertThat(showLoginCalled).isEqualTo(1);
	}

	private void searchForType(FxRobot robot, int index)
	{
		selectFromChoiceBox(robot, index, "#entityTypeSelector");
		robot.clickOn("#searchButton");
	}

	@Test
	void testSearch(FxRobot robot)
	{
		robot.clickOn("#searchButton"); // make sure that clicking this without a value in the search bar does not cause
										// any fatal errors/search for nothing
		assertThat(showSearchDisplayCalled).isEqualTo(0);
		assertThat(lastShownLabel).isNull();
		assertThat(lastShownEntities).isNull();

		// reminder that the list of items goes:
		// 0: Companies
		// 1: Job Postings
		// 2: People
		// 3: Skills

		searchForType(robot, 0);
		assertThat(showSearchDisplayCalled).isEqualTo(1);
		assertThat(lastShownLabel).isEqualTo("Companies");
		assertThat(lastShownEntities).isEqualTo(Company.retrieveAll());

		searchForType(robot, 1);
		assertThat(showSearchDisplayCalled).isEqualTo(2);
		assertThat(lastShownLabel).isEqualTo("Job Postings");
		assertThat(lastShownEntities).isEqualTo(JobPosting.retrieveAll());

		searchForType(robot, 2);
		assertThat(showSearchDisplayCalled).isEqualTo(3);
		assertThat(lastShownLabel).isEqualTo("People");
		assertThat(lastShownEntities).isEqualTo(Person.retrieveAll());

		searchForType(robot, 3);
		assertThat(showSearchDisplayCalled).isEqualTo(4);
		assertThat(lastShownLabel).isEqualTo("Skills");
		assertThat(lastShownEntities).isEqualTo(Skill.retrieveAll());
	}

}
