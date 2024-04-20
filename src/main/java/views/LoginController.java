package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.BranchedOutModel;
import models.TransitionalViewModel;

public class LoginController
{
	TransitionalViewModel tvm;
	BranchedOutModel model;

	public void setModels(BranchedOutModel newModel, TransitionalViewModel tvm)
	{
		this.tvm = tvm;
		model = newModel;
	}
	
    @FXML
    void onClickLogin(ActionEvent event) throws IOException {
    	tvm.showMain();
    }
	
}
