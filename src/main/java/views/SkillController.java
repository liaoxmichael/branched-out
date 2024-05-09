package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Link;
import models.Person;
import models.Skill;
import models.User;
import models.ViewTransitionHandler;
import models.adapters.FXUtils;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;

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

	@FXML
	private Button submitMentorButton;

	@FXML
	private Button cancelMentorButton;

	@FXML
	private HBox addMentorContainer;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextArea descriptionTextArea;

	@FXML
	private ChoiceBox<Person> peopleSelector;

	boolean currentlyEditing;

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
				addMentorButton.setDisable(false);
				peopleSelector.setItems(FXCollections.observableArrayList(Person.retrieveAll()));
			} else
			{
				editButton.setDisable(true);
				addMentorButton.setDisable(true);
			}
		}

		currentlyEditing = false;
		FXUtils.hideElement(nameTextField);
		FXUtils.hideElement(descriptionTextArea);

		FXUtils.hideElement(addMentorContainer);
		loadData();
	}

	@FXML
	void onAddClick(ActionEvent event)
	{
		FXUtils.showElement(addMentorContainer);
	}

	private void exitEditingMode()
	{
		FXUtils.hideElement(nameTextField);
		FXUtils.hideElement(descriptionTextArea);

		dataModel.setTitle(nameTextField.getText());
		dataModel.setDescription(descriptionTextArea.getText());
		dataModel.update();
		loadData();

		FXUtils.showElement(nameLabel);
		FXUtils.showElement(descriptionLabel);
	}

	private void enterEditingMode()
	{
		FXUtils.hideElement(nameLabel);
		FXUtils.hideElement(descriptionLabel);

		nameTextField.setText(dataModel.getTitle());
		FXUtils.showElement(nameTextField);

		descriptionTextArea.setText(dataModel.getDescription());
		FXUtils.showElement(descriptionTextArea);
	}

	@FXML
	void onEditClick(ActionEvent event)
	{
		currentlyEditing = !currentlyEditing;
		if (currentlyEditing)
		{
			editButton.setText("Save");
			enterEditingMode();
		} else
		{
			editButton.setText("Edit");
			exitEditingMode();
		}
	}

	private void loadData()
	{
		nameLabel.setText(dataModel.getTitle());
		descriptionLabel.setText(dataModel.getDescription());
	}

	private void updateMentorList()
	{
		ObservableList<Person> list = FXCollections.observableArrayList();
		for (Link l : dataModel.getLinks().get("mentors"))
		{
			int i = l.fetchPage().getEntityId();
			if (Person.retrieve(i) != null)
			{
				list.add(Person.retrieve(i));
			}
		}
		mentorList.setItems(list);
	};

	@FXML
	public void onSubmitClick(ActionEvent event)
	{
		Person person = peopleSelector.getSelectionModel().getSelectedItem();
		if (person != null)
		{
			dataModel.addMentor(person);
			dataModel.update();
			updateMentorList();
		}
		FXUtils.hideElement(addMentorContainer);
	}

	@FXML
	public void onCancelClick(ActionEvent event)
	{
		FXUtils.hideElement(addMentorContainer);
	}

}
