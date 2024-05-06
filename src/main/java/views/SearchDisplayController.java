package views;

import java.io.IOException;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.Entity;
import models.ViewTransitionHandler;

public class SearchDisplayController<T extends Entity>
{
	ViewTransitionHandler viewModel;

	@FXML
	private ListView<T> listView;
	
	@FXML
	private Label searchTypeLabel;

	public void setModels(ObservableList<T> entities, ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;

		listView.getSelectionModel().selectedItemProperty().addListener((e) ->
		{
			try
			{
				onClickItem();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		listView.setItems(entities);
	}

	@FXML
	void onClickItem() throws IOException
	{
		T model = listView.getSelectionModel().getSelectedItem();
//		System.out.println(model);
		viewModel.showJobPosting(model);
	}

}
