package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.JobPosting;
import models.TestJobPostingModel;
import models.TransitionalViewModel;
import models.ViewTransitionHandler;

public class JobPostingController
{

	ViewTransitionHandler viewModel;
	JobPosting dataModel;
	
    @FXML
    private Label companyName;

    @FXML
    private Label datePosted;

    @FXML
    private Label desc;

    @FXML
    private Label locationName;

    @FXML
    private Label numApplicants;

    @FXML
    private Button recommendJobBtn;

    @FXML
    private Label site;

    @FXML
    private Label title;

    @FXML
    private Label type;


	public void setModels(JobPosting newModel, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;
		loadData();
	}

	@FXML
	void onClickBack(ActionEvent event) throws IOException
	{
		viewModel.showSearchDisplay();
	}

	public void loadData()
	{
		title.setText(dataModel.getTitle());
		companyName.setText(dataModel.getLinks().get("company").get(0).getName());
		datePosted.setText(dataModel.datePosted);
		desc.setText(dataModel.desc);
//		System.out.println(model.locationName);
		locationName.setText(dataModel.locationName);
		site.setText(dataModel.site);
		numApplicants.setText(String.valueOf(dataModel.numApplicants));
		type.setText(dataModel.type);
		
	}

}
