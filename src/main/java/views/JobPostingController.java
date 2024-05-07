package views;

import java.text.SimpleDateFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.JobPosting;
import models.SkillProficiency;
import models.User;
import models.ViewTransitionHandler;

public class JobPostingController
{

	ViewTransitionHandler viewModel;
	JobPosting dataModel;

	@FXML
	private Label companyNameLabel;

	@FXML
	private Label datePostedLabel;

	@FXML
	private Label descriptionLabel;

	@FXML
	private Button editJobPostingButton;

	@FXML
	private Label locationLabel;

	@FXML
	private Button logoButton;

	@FXML
	private Label numApplicantsLabel;

	@FXML
	private Button recommendButton;

	@FXML
	private Label siteLabel;

	@FXML
	private ListView<SkillProficiency> skillsList;

	@FXML
	private Label titleLabel;

	@FXML
	private Label typeLabel;

	public void setModels(JobPosting newModel, User currentUser, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;

		if (dataModel.getPage().canEdit(currentUser))
		{
			editJobPostingButton.setDisable(false);
		} else
		{
			editJobPostingButton.setDisable(true);
		}

		loadData();
	}

	@FXML
	void onClickLogo(ActionEvent event)
	{
		viewModel.showProfile(dataModel.whichCompany());
	}

	public void loadData()
	{
		titleLabel.setText(dataModel.getTitle());
		companyNameLabel.setText(dataModel.whichCompany().getName());
		datePostedLabel.setText(new SimpleDateFormat("MMM d, yyyy HH:mm a").format(dataModel.getDatePosted()));
		descriptionLabel.setText(dataModel.getDescription());
		locationLabel.setText(dataModel.getLocation());
		siteLabel.setText(dataModel.getSite().label);
		numApplicantsLabel.setText(String.valueOf(dataModel.getLinks().get("applicants").size()));
		typeLabel.setText(dataModel.getType().label);
	}

}
