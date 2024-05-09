package views;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Company;
import models.Link;
import models.Person;
import models.Skill;
import models.SkillProficiency;
import models.User;
import models.ViewTransitionHandlerInterface;
import models.WorkExperience;
import models.SkillProficiency.ProficiencyLevel;
import models.adapters.Displayable;
import static models.adapters.FXUtils.*;

public class UserController
{
	ViewTransitionHandlerInterface viewModel; // if time: add follow button to diff users
	User dataModel;

	@FXML
	private Button addJobButton;

	@FXML
	private VBox addJobContainer;

	@FXML
	private Button addSkillButton;

	@FXML
	private HBox addSkillContainer;

	@FXML
	private ImageView bannerImage;

	@FXML
	private Label bioLabel;

	@FXML
	private TextArea biographyTextArea;

	@FXML
	private Button cancelJobButton;

	@FXML
	private Button cancelSkillButton;

	@FXML
	private ChoiceBox<Company> companySelector;

	@FXML
	private Button editProfileButton;

	@FXML
	private TextArea jobDescriptionTextArea;

	@FXML
	private VBox jobsContainer;

	@FXML
	private TextField jobTitleTextField;

	@FXML
	private ListView<WorkExperience> jobsList;

	@FXML
	private Label nameLabel;

	@FXML
	private TextField nameTextField;

	@FXML
	private Label numFollowersLabel;

	@FXML
	private Label numFollowingLabel;

	@FXML
	private ChoiceBox<String> proficiencyLevelSelector;

	@FXML
	private ImageView profileImage;

	@FXML
	private Label pronounsLabel;

	@FXML
	private TextField pronounsTextField;

	@FXML
	private ChoiceBox<Skill> skillSelector;

	@FXML
	private VBox skillsContainer;

	@FXML
	private ListView<SkillProficiency> skillsList;

	@FXML
	private Button submitJobButton;

	@FXML
	private Button submitSkillButton;

	@FXML
	private HBox followersContainer;

	@FXML
	private HBox followingContainer;

	boolean isCompany;
	boolean currentlyEditing;
	Person personModel;
	Company companyModel;

	public void setModels(User newModel, User currentUser, ViewTransitionHandlerInterface viewModel)
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
				editProfileButton.setDisable(false);
				addSkillButton.setDisable(false);
				addJobButton.setDisable(false);
				proficiencyLevelSelector.setItems(ProficiencyLevel.getEnumItems());
				skillSelector.setItems(FXCollections.observableArrayList(Skill.retrieveAll()));
				companySelector.setItems(FXCollections.observableArrayList(Company.retrieveAll()));
			} else
			{
				editProfileButton.setDisable(true);
				addSkillButton.setDisable(true);
				addJobButton.setDisable(true);
			}
			isCompany = dataModel instanceof Company;
			if (isCompany)
			{
				companyModel = (Company) dataModel;
			} else
			{
				personModel = (Person) dataModel;
			}

			currentlyEditing = false;
			hideElement(biographyTextArea);
			hideElement(nameTextField);
			hideElement(pronounsTextField);

			hideElement(addSkillContainer);
			hideElement(addJobContainer);

			bannerImage.setImage(new Image(dataModel.getBannerURL(), true)); // background load images
			profileImage.setImage(new Image(dataModel.getAvatarURL(), true));
			loadData();
		}
	}

	private void exitEditingMode()
	{
		hideElement(biographyTextArea);
		hideElement(nameTextField);
		if (!isCompany)
		{
			hideElement(pronounsTextField);
		}
		updateDataModel();
		loadData();
		showElement(bioLabel);
		showElement(nameLabel);
		if (!isCompany)
		{
			showElement(pronounsLabel);
		}
	}

	private void enterEditingMode()
	{
		hideElement(bioLabel);
		hideElement(nameLabel);
		if (!isCompany)
		{
			hideElement(pronounsLabel);
		}

		biographyTextArea.setText(dataModel.getBio());
		showElement(biographyTextArea);

		nameTextField.setText(dataModel.getName());
		showElement(nameTextField);
		if (!isCompany)
		{
			pronounsTextField.setText(personModel.getPronouns());
			showElement(pronounsTextField);
		}
	}

	private void updateDataModel()
	{
		dataModel.setBio(biographyTextArea.getText());
		dataModel.setName(nameTextField.getText());
		if (!isCompany)
		{
			personModel.setPronouns(pronounsTextField.getText());
		}
		dataModel.update();
	}

	private void onClickSkill()
	{
		SkillProficiency prof = skillsList.getSelectionModel().getSelectedItem();
		viewModel.showSkill(prof.getSkill());
	}

	private void onClickJob()
	{
		WorkExperience job = jobsList.getSelectionModel().getSelectedItem();
		viewModel.showProfile(job.fetchCompany());
	}

	private void loadData()
	{
		loadCommonData();
		if (!isCompany)
		{
			pronounsLabel.setText(personModel.getPronouns());
			skillsList.setItems(FXCollections.observableArrayList(personModel.getSkills()));
			skillsList.getSelectionModel().selectedItemProperty().addListener((e) ->
			{
				onClickSkill();
			});

			jobsList.setItems(FXCollections.observableArrayList(personModel.getJobs()));
			jobsList.getSelectionModel().selectedItemProperty().addListener((e) ->
			{
				onClickJob();
			});
		} else
		{
			pronounsLabel.setText("company");
			hideElement(skillsContainer);
			hideElement(jobsContainer);
		}
	}

	private void loadCommonData()
	{
		nameLabel.setText(dataModel.getName());
		bioLabel.setText(dataModel.getBio());

		numFollowersLabel.textProperty().bind(
				Bindings.size(FXCollections.observableArrayList(dataModel.getLinks().get("followers"))).asString());
		numFollowingLabel.textProperty().bind(
				Bindings.size(FXCollections.observableArrayList(dataModel.getLinks().get("following"))).asString());
	}

	@FXML
	void onClickEdit(ActionEvent event)
	{
		currentlyEditing = !currentlyEditing;
		if (currentlyEditing)
		{
			editProfileButton.setText("Save");
			enterEditingMode();
		} else
		{
			editProfileButton.setText("Edit");
			exitEditingMode();
		}
	}

	@FXML
	public void onClickAddJob(ActionEvent event)
	{
		showElement(addJobContainer);
	}

	@FXML
	public void onClickAddSkill(ActionEvent event)
	{
		showElement(addSkillContainer);
	}

	@FXML
	void onClickCancelJob(ActionEvent event)
	{
		hideElement(addJobContainer);
	}

	@FXML
	void onClickCancelSkill(ActionEvent event)
	{
		hideElement(addSkillContainer);
	}

	@FXML
	void onClickSubmitJob(ActionEvent event)
	{
		Company company = companySelector.getSelectionModel().getSelectedItem();
		if (company != null && !jobTitleTextField.getText().isEmpty())
		{
			personModel.addJob(jobTitleTextField.getText(), jobDescriptionTextArea.getText(), company);
			personModel.update();
			jobsList.setItems(FXCollections.observableArrayList(personModel.getJobs()));
		}
		hideElement(addJobContainer);
	}

	@FXML
	void onClickSubmitSkill(ActionEvent event)
	{
		Skill skill = skillSelector.getSelectionModel().getSelectedItem();
		ProficiencyLevel level = ProficiencyLevel
				.labelToEnum(proficiencyLevelSelector.getSelectionModel().getSelectedItem());
		if (skill != null && level != null)
		{
			personModel.addSkill(skill, level);
			personModel.update();
			skillsList.setItems(FXCollections.observableArrayList(personModel.getSkills()));
		}
		hideElement(addSkillContainer);
	}

	@FXML
	void onClickFollowers(MouseEvent event)
	{
		ObservableList<Displayable> list = FXCollections.observableArrayList();
		for (Link l : dataModel.getLinks().get("followers"))
		{
			int i = l.fetchPage().getEntityId();
			if (Person.retrieve(i) != null)
			{
				list.add(Person.retrieve(i));
			} else if (Company.retrieve(i) != null)
			{
				list.add(Company.retrieve(i));
			}
		}
		viewModel.showSearchDisplay(list, "Followers of " + dataModel.getName());
	}

	@FXML
	void onClickFollowing(MouseEvent event)
	{
		ObservableList<Displayable> list = FXCollections.observableArrayList();
		for (Link l : dataModel.getLinks().get("following"))
		{
			int i = l.fetchPage().getEntityId();
			if (Person.retrieve(i) != null)
			{
				list.add(Person.retrieve(i));
			} else if (Company.retrieve(i) != null)
			{
				list.add(Company.retrieve(i));
			}
		}

		viewModel.showSearchDisplay(list, "Users " + dataModel.getName() + " is following");
	}
}
