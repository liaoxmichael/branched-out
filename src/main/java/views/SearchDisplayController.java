package views;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.JobPosting;
import models.Skill;
import models.User;
import models.ViewTransitionHandlerInterface;
import models.adapters.Displayable;
import javafx.scene.control.Label;

public class SearchDisplayController
{
	ViewTransitionHandlerInterface viewModel;

	@FXML
	private ListView<Displayable> listView;

	@FXML
	private Label infoLabel;

	public void setModels(ObservableList<Displayable> entities, String newLabel, ViewTransitionHandlerInterface viewModel)
	{
		this.viewModel = viewModel;

		listView.getSelectionModel().selectedItemProperty().addListener((e) ->
		{
			onClickItem();
		});

		infoLabel.setText(newLabel);
		listView.setItems(entities);
	}

	void onClickItem()
	{
		Displayable model = listView.getSelectionModel().getSelectedItem();
		if (model instanceof JobPosting)
		{
			viewModel.showJobPosting((JobPosting) model);
		} else if (model instanceof User)
		{
			viewModel.showProfile((User) model);
		} else if (model instanceof Skill)
		{
			viewModel.showSkill((Skill) model);
		}

	}

}
