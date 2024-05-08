package models;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import models.adapters.Displayable;
import views.JobPostingController;
import views.MainController;
import views.SearchDisplayController;
import views.SkillController;
import views.UserController;

public class ViewTransitionHandler
{
	BorderPane mainview;
	User currentUser;

	public ViewTransitionHandler(BorderPane view)
	{
		mainview = view;
	}

	public void showMainFromLogin(User user)
	{
		// set mainview to dashboard
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/MainView.fxml"));

		Pane view;
		try
		{
			view = loader.load();

			MainController controller = loader.getController();
			mainview.setTop(null);
			mainview.setCenter(view);
			// reset mainview to take perspective of actual MainView instead of LoginView
			mainview = (BorderPane) view;
			currentUser = user;
			controller.setModels(user, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showMain(User user)
	{
		mainview.setCenter(null);
	}

	public void showSearchDisplay(ObservableList<Displayable> entities, String newLabel) // possible enum for classes?
	{
		// set center to display list of job postings
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/SearchDisplayView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();

			SearchDisplayController controller = loader.getController();
			mainview.setCenter(view);
			controller.setModels(entities, newLabel, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showProfile(User user)
	{
		// set center to user profile
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/UserView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();

			UserController controller = loader.getController();
			mainview.setCenter(view);
			controller.setModels(user, currentUser, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showSkill(Skill skill)
	{
		// show skill page from user profile
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/SkillView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();

			SkillController controller = loader.getController();
			mainview.setCenter(view);
			controller.setModels(skill, currentUser, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showJobPosting(JobPosting job)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/JobPostingView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();

			JobPostingController controller = loader.getController();
			mainview.setCenter(view);
			controller.setModels(job, currentUser, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showBlockError()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/BlockErrorView.fxml"));

		BorderPane view;
		try
		{
			view = loader.load();

			mainview.setCenter(view);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
