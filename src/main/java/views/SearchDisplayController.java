package views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.Entity;
import models.ViewTransitionHandler;

public class SearchDisplayController
{
	ViewTransitionHandler viewModel;
	Entity dataModel;

	@FXML
	private ListView<Entity> listView;
	
	@FXML
	private Label searchTypeLabel;

	public void setModels(BranchedOutModel newModel, TransitionalViewModel tvm)
	{
		this.viewModel = tvm;
		dataModel = newModel;
		
		loadData();

		jobView.getSelectionModel().selectedItemProperty().addListener((e) ->
		{
			try
			{
				onClickJob();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	@FXML
	void onClickJob() throws IOException
	{
		TestJobPostingModel model = jobView.getSelectionModel().getSelectedItem();
//		System.out.println(model);
		viewModel.showJobPosting(model);
	}

	public void loadData()
	{
		jobView.setCellFactory(new Callback<ListView<TestJobPostingModel>, ListCell<TestJobPostingModel>>()
		{

			@Override
			public ListCell<TestJobPostingModel> call(ListView<TestJobPostingModel> param)
			{
				// TODO Auto-generated method stub
				try
				{
					return new JobPostingCell(param, viewModel);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

		});

		jobView.setItems(dataModel.getJobs());
	}

}
