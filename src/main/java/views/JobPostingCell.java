package views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import models.TestJobPostingModel;
import models.TransitionalViewModel;
import models.ViewTransitionHandler;

public class JobPostingCell extends ListCell<TestJobPostingModel>
{
	
	AnchorPane view;
	JobPosting job;
	JobPostingCellController controller;
	ViewTransitionHandler model;
	
	public JobPostingCell(ListView<TestJobPostingModel> view, TransitionalViewModel model) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(TransitionalViewModel.class.getResource("../views/JobPostingCellView.fxml")); 

		this.view = loader.load();
		this.model = model;

		controller = loader.getController();
		
		setText(null);
	}
	
	@Override
	protected void updateItem(TestJobPostingModel item, boolean empty) 
	{
		job = item;
		
		if (empty || item == null)
		{
			setGraphic(null);
		}
		else 
		{
			controller.setModels(item, model);
			setGraphic(view);
		}
		super.updateItem(item, empty);
	}
	
}
