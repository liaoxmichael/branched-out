package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Person;
import models.Skill;
import models.User;
import models.ViewTransitionHandler;

public class SkillController
{
	ViewTransitionHandler viewModel;
	Skill dataModel;

	@FXML
	private Button addMentorButton;

	@FXML
	private Label descriptionLabel;

	@FXML
	private Button editButton;

	@FXML
	private ListView<Person> mentorList;

	@FXML
	private Label nameLabel;

	public void setModels(Skill newModel, User currentUser, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;

		if (dataModel.fetchPage().cantView(currentUser))
		{
			viewModel.showBlockError();
		} else
		{
			if (dataModel.fetchPage().canEdit(currentUser))
			{
				editButton.setDisable(false);
			} else
			{
				editButton.setDisable(true);
			}
		}
	}

	@FXML
	void onAddClick(ActionEvent event)
	{

	}

	@FXML
	void onEditClick(ActionEvent event)
	{

	}

}
