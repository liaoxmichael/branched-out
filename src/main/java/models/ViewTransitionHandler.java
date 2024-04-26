package models;

import java.io.IOException;

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
	Entity dataModel;

	public ViewTransitionHandler(BorderPane view, Entity model)
	{
		mainview = view;
		dataModel = model;
	}

	public void showMain() throws IOException
	{
		// set mainview to dashboard
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/MainView.fxml"));

		Pane view = loader.load();

		MainController controller = loader.getController();
		mainview.setTop(null);
		mainview.setCenter(view);
		mainview = (BorderPane) view; // reset mainview to take perspective of actual Main (w/ nav bar) instead of
										// LoginView
		controller.setModels(dataModel, this);
	}

	public void showSearchDisplay() throws IOException // possible enum?
	{
		// set center to display list of job postings
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/SearchDisplayView.fxml"));

		ScrollPane view = loader.load();

		SearchDisplayController controller = loader.getController();
		mainview.setCenter(view);
		controller.setModels(model, this);

	}

	public void showProfile(User user) throws IOException
	{
		// set center to user profile
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/UserView.fxml"));

		ScrollPane view = loader.load();

		UserController controller = loader.getController();
		mainview.setCenter(view);
		controller.setModels(model, this);
		controller.loadData();
	}

	public void showSkill(Skill skill) throws IOException
	{
		// show skill page from user profile
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/SkillView.fxml"));

		ScrollPane view = loader.load();

		SkillController controller = loader.getController();
		mainview.setCenter(view);
		controller.setModels(model, this);
		//controller.loadData(); // TODO
	}

	public void showJobPosting(JobPosting job) throws IOException
	{
		// TODO takes us from display job postings to job posting page
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewTransitionHandler.class.getResource("../views/JobPostingView.fxml"));

		ScrollPane view = loader.load();

		JobPostingController controller = loader.getController();
		mainview.setCenter(view);
		controller.setModels(job, this);

	}
}
