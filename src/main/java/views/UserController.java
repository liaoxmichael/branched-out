package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.User;
import models.ViewTransitionHandler;

public class UserController
{
	ViewTransitionHandler viewModel;
	User dataModel;

	@FXML
	private Button addExperienceBtn;

	@FXML
	private Button addSkillBtn;

	@FXML
	private Label bio;

	@FXML
	private Button editExperienceBtn;

	@FXML
	private Button editProfileBtn;

	@FXML
	private Button editSkillBtn;

	@FXML
	private Label name;

	@FXML
	private Label numFollowers;

	@FXML
	private Label numFollowing;

	@FXML
	private Label pronouns;
	
	@FXML
	private ListView<String> skillsList;
	
	@FXML
	private ListView<String> jobsList;
	
	@FXML
	private ListView<String> communitiesList;

	public void setModels(User newModel, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;
	}
}
