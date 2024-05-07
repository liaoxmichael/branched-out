package models.adapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class EntityUtils
{
	public final static ObservableList<String> entityTypes = FXCollections.observableArrayList("Companies",
			"Job Postings", "People", "Skills", "Projects");

	private EntityUtils() // do not instantiate
	{

	}
	
	
}
