package views;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.JobPosting;
import models.Skill;
import models.User;
import models.ViewTransitionHandler;
import models.adapters.Displayable;

public class SearchDisplayController
{
	ViewTransitionHandler viewModel;

	@FXML
	private ListView<Displayable> listView;

	@FXML
	private Label searchTypeLabel;

	public void setModels(ObservableList<Displayable> entities, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
		
		listView.getSelectionModel().selectedItemProperty().addListener((e) ->
		{
			onClickItem();
		});

		listView.setItems(entities);
	}

	@FXML
	void onClickItem()
	{
		Displayable model = listView.getSelectionModel().getSelectedItem();
		if (model instanceof JobPosting)
		{
			viewModel.showJobPosting((JobPosting) model);
		} else if (model instanceof User) {
			viewModel.showProfile((User) model);
		} else if (model instanceof Skill) {
			viewModel.showSkill((Skill) model);
		}
		
	}

}
