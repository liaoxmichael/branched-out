package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.Entity;
import models.ViewTransitionHandler;

public class LoginController
{
	ViewTransitionHandler viewModel;
	Entity dataModel;

	public void setModels(Entity newModel, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;
	}
	
    @FXML
    void onClickLogin(ActionEvent event) throws IOException {
    	viewModel.showMain();
    }
	
}
