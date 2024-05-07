package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import models.SkillProficiency;
import models.User;
import models.ViewTransitionHandler;
import models.WorkExperience;

public class UserController
{
	ViewTransitionHandler viewModel;
	User dataModel;

	@FXML
    private Button addJobButton;

    @FXML
    private Button addSkillButton;

    @FXML
    private ImageView bannerImage;

    @FXML
    private Label bioLabel;

    @FXML
    private Button editProfileButton;

    @FXML
    private ListView<WorkExperience> jobsList;

    @FXML
    private Label nameLabel;

    @FXML
    private Label numFollowersLabel;

    @FXML
    private Label numFollowingLabel;

    @FXML
    private ImageView profileImage;

    @FXML
    private Label pronounsLabel;

    @FXML
    private ListView<SkillProficiency> skillsList;

	public void setModels(User newModel, User currentUser, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;
	}
}
