package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.User;
import models.ViewTransitionHandler;
import models.adapters.EntityUtils;

public class MainController
{
	ViewTransitionHandler viewModel;
	User dataModel;

	@FXML
	private ChoiceBox<String> entityTypeSelector;

	@FXML
	private Button homeButton;

	@FXML
	private Button profileButton;

	@FXML
	private TextField searchBar;

	@FXML
	private Button searchButton;

	public void setModels(User newModel, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;
		
		entityTypeSelector.setItems(EntityUtils.entityTypes);
	}

	@FXML
	void onClickProfile(ActionEvent event)
	{
		viewModel.showProfile(dataModel);
	}

	@FXML
	void onClickSearch(ActionEvent event)
	{
		String type = entityTypeSelector.getSelectionModel().getSelectedItem();
		
		
		viewModel.showSearchDisplay();
	}
	
	@FXML
	void onClickHome(ActionEvent event)
	{
    	viewModel.showMain(dataModel);
	}
}
