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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Main;
import models.JobPosting;
import models.Person;
import models.Skill;
import models.User;
import models.ViewTransitionHandlerInterface;
import models.adapters.Displayable;

@ExtendWith(ApplicationExtension.class)
class LoginViewTest implements ViewTransitionHandlerInterface
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
		loader.setLocation(Main.class.getResource("../views/LoginView.fxml"));

		BorderPane view;
		try
		{
			view = loader.load();
			initDatabase();

			LoginController controller = loader.getController();
			controller.setModels(this);

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
	
	public void attemptLogin(FxRobot robot, String username, String password)
	{
		enterText(robot, username, "#userIdField");
		enterText(robot, password, "#passwordField");
		robot.clickOn("#loginButton");
	}

	@Test
	void testLogin(FxRobot robot)
	{
		// attempt a faulty login
		attemptLogin(robot, "fakeuser", "");
		checkVisible(robot, "#errorMessageLabel");
		checkLabel(robot, "Wrong username or password. Try again.", "#errorMessageLabel");
		assertThat(showLoginCalled).isEqualTo(0);

		// attempt a real login
		robot.clickOn("#userIdField");
		robot.eraseText("fakeuser".length()); // want to leave username behind!
		attemptLogin(robot, "ateam@gmail.com", "1234");

		// check that we actually did log in
		assertThat(showMainCalled).isEqualTo(1);
		assertThat(currentUser).isEqualTo(Person.retrieve(5));

		// login without password
		attemptLogin(robot, "bobert33@yahoo.com", "");
		assertThat(showMainCalled).isEqualTo(2);
	}

}
