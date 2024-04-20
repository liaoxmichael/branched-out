package views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import models.TestJobPostingModel;
import models.TransitionalViewModel;

public class JobPostingCellController
{
	TestJobPostingModel model;
	TransitionalViewModel tvm;
	
    @FXML
    private Label company;

    @FXML
    private Label jobTitle;

	public void setModels(TestJobPostingModel newModel, TransitionalViewModel tvm)
	{
		this.tvm = tvm;
		model = newModel;
		loadData();
	}

    @FXML
    void onClickJobPosting(MouseEvent event) throws IOException {
    	tvm.showJobPosting(model);
    }
    
    public void loadData()
    {
    	company.setText(model.company);
    	jobTitle.setText(model.title);
    }

}
