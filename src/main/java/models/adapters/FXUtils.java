package models.adapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public final class FXUtils
{
	public final static ObservableList<String> entityTypes = FXCollections.observableArrayList("Companies",
			"Job Postings", "People", "Skills");

	private FXUtils() // do not instantiate
	{

	}

	public static void hideElement(Node n)
	{
		n.setVisible(false);
		n.setManaged(false);
	}

	public static void showElement(Node n)
	{
		n.setManaged(true);
		n.setVisible(true);
	}
}
