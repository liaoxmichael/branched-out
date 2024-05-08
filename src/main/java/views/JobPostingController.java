package views;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import models.Company;
import models.JobPosting;
import models.Skill;
import models.SkillProficiency;
import models.SkillProficiency.ProficiencyLevel;
import models.User;
import models.ViewTransitionHandler;
import models.adapters.FXUtils;
import models.recommender.JobSite;
import models.recommender.JobType;

public class JobPostingController
{

	ViewTransitionHandler viewModel;
	JobPosting dataModel;

	@FXML
	private Button addSkillButton;

	@FXML
	private HBox addSkillContainer;

	@FXML
	private Button cancelSkillButton;

	@FXML
	private Label companyNameLabel;

	@FXML
	private Label descriptionLabel;

	@FXML
	private TextArea descriptionTextArea;

	@FXML
	private Button editJobPostingButton;

	@FXML
	private Label locationLabel;

	@FXML
	private TextField locationTextField;

	@FXML
	private Button logoButton;

	@FXML
	private Label numApplicantsLabel;

	@FXML
	private ChoiceBox<String> proficiencyLevelSelector;

	@FXML
	private Label siteLabel;

	@FXML
	private ChoiceBox<String> siteSelector;

	@FXML
	private ChoiceBox<Skill> skillSelector;

	@FXML
	private ListView<SkillProficiency> skillsList;

	@FXML
	private Button submitSkillButton;

	@FXML
	private Label titleLabel;

	@FXML
	private TextField titleTextField;

	@FXML
	private Label typeLabel;

	@FXML
	private ChoiceBox<String> typeSelector;

	boolean currentlyEditing;
	Company company;

	public void setModels(JobPosting newModel, User currentUser, ViewTransitionHandler viewModel)
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
				editJobPostingButton.setDisable(false);
				addSkillButton.setDisable(false);
				siteSelector.setItems(JobSite.getEnumItems());
				typeSelector.setItems(JobType.getEnumItems());
				skillSelector.setItems(FXCollections.observableArrayList(Skill.retrieveAll()));
				proficiencyLevelSelector.setItems(ProficiencyLevel.getEnumItems());
			} else
			{
				editJobPostingButton.setDisable(true);
				addSkillButton.setDisable(true);
			}

			// handle hidden editing fields
			currentlyEditing = false;
			FXUtils.hideElement(titleTextField);
			FXUtils.hideElement(descriptionTextArea);
			FXUtils.hideElement(typeSelector);
			FXUtils.hideElement(siteSelector);
			FXUtils.hideElement(locationTextField);

			FXUtils.hideElement(addSkillContainer);
			company = dataModel.fetchCompany();
			loadData();
		}
	}

	private void exitEditingMode()
	{
		FXUtils.hideElement(titleTextField);
		FXUtils.hideElement(descriptionTextArea);
		FXUtils.hideElement(typeSelector);
		FXUtils.hideElement(siteSelector);
		FXUtils.hideElement(locationTextField);
		updateDataModel();
		loadData();
		FXUtils.showElement(titleLabel);
		FXUtils.showElement(descriptionLabel);
		FXUtils.showElement(typeLabel);
		FXUtils.showElement(siteLabel);
		FXUtils.showElement(locationLabel);
	}

	private void enterEditingMode()
	{
		FXUtils.hideElement(titleLabel);
		FXUtils.hideElement(descriptionLabel);
		FXUtils.hideElement(typeLabel);
		FXUtils.hideElement(siteLabel);
		FXUtils.hideElement(locationLabel);

		titleTextField.setText(dataModel.getTitle());
		FXUtils.showElement(titleTextField);

		descriptionTextArea.setText(dataModel.getDescription());
		FXUtils.showElement(descriptionTextArea);

		typeSelector.setValue(dataModel.getType().label);
		FXUtils.showElement(typeSelector);

		siteSelector.setValue(dataModel.getSite().label);
		FXUtils.showElement(siteSelector);

		locationTextField.setText(dataModel.getLocation());
		FXUtils.showElement(locationTextField);
	}

	private void updateDataModel()
	{
		dataModel.setTitle(titleTextField.getText());
		dataModel.setDescription(descriptionTextArea.getText());
		dataModel.setLocation(locationTextField.getText());
		dataModel.setSite(JobSite.labelToEnum(siteSelector.getSelectionModel().getSelectedItem()));
		dataModel.setType(JobType.labelToEnum(typeSelector.getSelectionModel().getSelectedItem()));

		dataModel.update();
	}

	@FXML
	void onClickEdit(ActionEvent event)
	{
		currentlyEditing = !currentlyEditing;
		if (currentlyEditing)
		{
			editJobPostingButton.setText("Save");
			enterEditingMode();

		} else
		{
			editJobPostingButton.setText("Edit");
			// hide all textfields, commit changes, show all labels
			exitEditingMode();

			siteLabel.setText(dataModel.getSite().label);

			titleLabel.setText(dataModel.getTitle());
		}
	}

	@FXML
	void onClickLogo(ActionEvent event)
	{
		viewModel.showProfile(company);
	}

	@FXML
	void onClickCompanyName(MouseEvent event)
	{
		viewModel.showProfile(company);
	}

	@FXML
	void onClickAdd(ActionEvent event)
	{
		FXUtils.showElement(addSkillContainer);
	}

	@FXML
	void onClickCancel(ActionEvent event)
	{
		FXUtils.hideElement(addSkillContainer);
	}

	@FXML
	void onClickSubmit(ActionEvent event)
	{
		Skill skill = skillSelector.getSelectionModel().getSelectedItem();
		ProficiencyLevel level = ProficiencyLevel.labelToEnum(proficiencyLevelSelector.getSelectionModel().getSelectedItem());
		dataModel.addRequiredSkill(skill, level);
		dataModel.update();
		skillsList.setItems(FXCollections.observableArrayList(dataModel.getRequiredSkills()));
		
		FXUtils.hideElement(addSkillContainer);
	}

	private void loadData()
	{
		titleLabel.setText(dataModel.getTitle());
		companyNameLabel.setText(company.getName());
		descriptionLabel.setText(dataModel.getDescription());
		locationLabel.setText(dataModel.getLocation());
		siteLabel.setText(dataModel.getSite().label);
		numApplicantsLabel.textProperty().bind(
				Bindings.size(FXCollections.observableArrayList(dataModel.getLinks().get("applicants"))).asString());
		typeLabel.setText(dataModel.getType().label);
		
		skillsList.setItems(FXCollections.observableArrayList(dataModel.getRequiredSkills()));
	}

}
