package views;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Company;
import models.Person;
import models.User;
import models.ViewTransitionHandler;

public class LoginController
{
	ViewTransitionHandler viewModel;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField userIdField;

	@FXML
	private Label errorMessageLabel;

	public void setModels(ViewTransitionHandler viewModel)
	{
		this.viewModel = viewModel;
	}

	@FXML
	void onClickLogin(ActionEvent event) throws IOException
	{
		User user = verifyUser(userIdField.getText(), passwordField.getText());
		if (user == null)
		{
			passwordField.setText(null);
			errorMessageLabel.setText("Wrong username or password. Try again.");
		} else {
			errorMessageLabel.setText(null);
			viewModel.showMainFromLogin(user);
		}
	}

	private User verifyUser(String username, String pass)
	{
		ArrayList<User> users = new ArrayList<User>();
		users.addAll(Person.retrieveAll());
		users.addAll(Company.retrieveAll());

		for (User u : users)
		{
			if (u.verifyUser(username, pass))
			{
				return u;
			}
		}
		return null;
	}

}
