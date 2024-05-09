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

		BorderPane view;
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

	}

	@Test
	void testLogout(FxRobot robot)
	{

	}

	@Test
	void testSearch()
	{

	}

}
