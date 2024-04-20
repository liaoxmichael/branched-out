package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.BranchedOutModel;
import models.TransitionalViewModel;

public class MainController
{
	TransitionalViewModel tvm;
	BranchedOutModel model;

	public void setModels(BranchedOutModel newModel, TransitionalViewModel tvm)
	{
		this.tvm = tvm;
		model = newModel;
	}
	
    @FXML
    void onClickProfile(ActionEvent event) throws IOException {
    	tvm.showProfile();
    }

    @FXML
    void onClickSearch(ActionEvent event) throws IOException {
    	tvm.showSearchDisplay();
    }

    @FXML
    void onClickSkill(ActionEvent event) throws IOException {
    	tvm.showSkill();
    }
    
    @FXML
    void onClickHome(ActionEvent event) throws IOException {
    	tvm.showMain();
    }
}
