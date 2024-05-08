package models.recommender;

import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static java.util.Map.entry;

public enum JobSite {
	ON_SITE("On Site"), HYBRID("Hybrid"), REMOTE("Remote");

	public final String label;
	private static final Map<String, JobSite> labelToEnumMap = Map.ofEntries(entry("On Site", ON_SITE),
			entry("Hybrid", HYBRID), entry("Remote", REMOTE));

	JobSite(String label)
	{
		this.label = label;
	}

	public static JobSite labelToEnum(String label)
	{
		return labelToEnumMap.get(label);
	}

	public static ObservableList<String> getEnumItems()
	{
		ObservableList<String> list = FXCollections.observableArrayList();
		for (JobSite site : JobSite.values())
		{
			list.add(site.label);
		}
		return list;
	}
}
