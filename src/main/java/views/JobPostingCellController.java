package views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import models.JobPosting;
import models.TestJobPostingModel;
import models.TransitionalViewModel;
import models.ViewTransitionHandler;

public class JobPostingCellController
{
	JobPosting model;
	ViewTransitionHandler viewModel;
	
    @FXML
    private Label company;

    @FXML
    private Label jobTitle;

	public void setModels(JobPosting newModel, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		model = newModel;
		loadData();
	}

    @FXML
    void onClickJobPosting(MouseEvent event) throws IOException {
    	viewModel.showJobPosting(model);
    }
    
    public void loadData()
    {
    	company.setText(model.company);
    	jobTitle.setText(model.title);
    }

}
