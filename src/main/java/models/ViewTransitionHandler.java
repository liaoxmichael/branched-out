package models;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import views.JobPostingController;
import views.MainController;
import views.SearchDisplayController;
import views.SkillController;
import views.UserController;

public class ViewTransitionHandler
{
	BorderPane mainview;

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
			mainview = (BorderPane) view; // reset mainview to take perspective of actual Main (w/ nav bar) instead of
											// LoginView
			controller.setModels(user, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showMain(User user)
	{
		// set mainview to dashboard
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/MainView.fxml"));

		Pane view;
		try
		{
			view = loader.load();

			MainController controller = loader.getController();
			mainview.setCenter(view);

			controller.setModels(user, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public <T extends Entity> void showSearchDisplay(ObservableList<T> entities) // possible enum for classes?
	{
		// set center to display list of job postings
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/SearchDisplayView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();
			
			SearchDisplayController<T> controller = loader.getController();
			mainview.setCenter(view);
			controller.setModels(entities, this);
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
//			controller.setModels(model, this);
//			controller.loadData();
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
//			controller.setModels(model, this);
			// controller.loadData(); // TODO
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showJobPosting(JobPosting job)
	{
		// TODO takes us from display job postings to job posting page
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/JobPostingView.fxml"));

		ScrollPane view;
		try
		{
			view = loader.load();
			
			JobPostingController controller = loader.getController();
			mainview.setCenter(view);
//			controller.setModels(job, this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
