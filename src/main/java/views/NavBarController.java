package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import models.Company;
import models.JobPosting;
import models.Person;
import models.Skill;
import models.User;
import models.ViewTransitionHandler;
import models.adapters.Displayable;
import models.adapters.EntityType;

public class NavBarController
{
	ViewTransitionHandler viewModel;
	User dataModel;

	@FXML
	private ChoiceBox<String> entityTypeSelector;

	@FXML
	private Button homeButton;

	@FXML
	private Button logoutButton;

	@FXML
	private Button searchButton;

	public void setModels(User newModel, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		dataModel = newModel;

		entityTypeSelector.setItems(EntityType.getEnumItems());
	}

	@FXML
	void onClickLogout(ActionEvent event)
	{
		viewModel.showLogin();
	}

	@FXML
	void onClickSearch(ActionEvent event)
	{
		String type = entityTypeSelector.getSelectionModel().getSelectedItem();
		if (type != null)
		{
			ObservableList<Displayable> entities = FXCollections.observableArrayList();
			switch (type) // bad to convert and reconvert after passing but so be it
			{
			case "Companies":
				entities.addAll(Company.retrieveAll());
				break;
			case "Job Postings":
				entities.addAll(JobPosting.retrieveAll());
				break;
			case "People":
				entities.addAll(Person.retrieveAll());
				break;
			case "Skills":
				entities.addAll(Skill.retrieveAll());
				break;
			}

			viewModel.showSearchDisplay(entities, type);
		}
	}

	@FXML
	void onClickHome(ActionEvent event)
	{
		viewModel.showMain(dataModel);
	}
}
