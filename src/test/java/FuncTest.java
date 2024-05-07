import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

@ExtendWith(ApplicationExtension.class)
class FuncTest
{

	@Start
	public void start(Stage stage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../views/MainView.fxml"));

		AnchorPane view = loader.load();

//		SumController controller = loader.getController();
//		AdderModel model = new AdderModel();
//		controller.setModel(model);

		Scene s = new Scene(view);
		stage.setScene(s);
		stage.show();
	}

	@Test
	void test()
	{
		fail("Not yet implemented");
	}

}
