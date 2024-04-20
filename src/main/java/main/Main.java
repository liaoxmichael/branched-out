package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("views/LoginView.fxml")); // we can change this later if need be

		BorderPane view = loader.load();
		
		// TODO

//		BranchedOutModel branchedOutModel = new BranchedOutModel();
//
//		LoginController controller = loader.getController();
//		TransitionalViewModel model = new TransitionalViewModel(view, branchedOutModel);
//		controller.setModels(branchedOutModel, model);

		Scene s = new Scene(view);
		stage.setScene(s);
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
