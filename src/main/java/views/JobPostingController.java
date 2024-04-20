package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.TestJobPostingModel;
import models.TransitionalViewModel;

public class JobPostingController
{

	TransitionalViewModel tvm;
	TestJobPostingModel model;
	
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


	public void setModels(TestJobPostingModel newModel, TransitionalViewModel tvm)
	{
		System.out.println(newModel.locationName);
		this.tvm = tvm;
		model = newModel;
		loadData();
	}

	@FXML
	void onClickBack(ActionEvent event) throws IOException
	{
		tvm.showSearchDisplay();
	}

	public void loadData()
	{
		title.setText(model.title);
		companyName.setText(model.company);
		datePosted.setText(model.datePosted);
		desc.setText(model.desc);
//		System.out.println(model.locationName);
		locationName.setText(model.locationName);
		site.setText(model.site);
		numApplicants.setText(String.valueOf(model.numApplicants));
		type.setText(model.type);
		
	}

}
