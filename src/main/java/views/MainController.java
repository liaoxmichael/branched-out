package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.Entity;
import models.ViewTransitionHandler;

public class MainController
{
	ViewTransitionHandler viewModel;
	Entity dataModel;

	public void setModels(Entity newModel, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;
	}
	
	// TODO controller stuff
    @FXML
    void onClickProfile(ActionEvent event) {
    	// need some way to poll which user is being clicked on...
//    	viewModel.showProfile((User) dataModel);
    }

    @FXML
    void onClickSearch(ActionEvent event) {
//    	viewModel.showSearchDisplay();
    }

    @FXML
    void onClickSkill(ActionEvent event) {
//    	viewModel.showSkill();
    }
    
    @FXML
    void onClickHome(ActionEvent event) {
//    	viewModel.showMain();
    }
}
