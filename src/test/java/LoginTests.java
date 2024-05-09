import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Main;
import models.ViewTransitionHandler;
import views.LoginController;

@ExtendWith(ApplicationExtension.class)
class LoginTests
{

	@Start
	public void start(Stage stage) throws Exception
	{
		TestUtils.start(stage);
		TestUtils.initDatabase();
	}

	@Test
	void testLogin(FxRobot robot)
	{
		
	}

}
