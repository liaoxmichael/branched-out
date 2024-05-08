package models.recommender;

import static java.util.Map.entry;

import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum JobType {
	INTERNSHIP("Internship"), SEASONAL("Seasonal"), TEMPORARY("Temporary"), VOLUNTEER("Volunteer"),
	FULL_TIME("Full-time"), PART_TIME("Part-time"), OTHER("Other");

	public final String label;
	static final Map<String, JobType> labelToEnumMap = Map.ofEntries(entry("Internship", INTERNSHIP),
			entry("Seasonal", SEASONAL), entry("Temporary", TEMPORARY), entry("Volunteer", VOLUNTEER),
			entry("Full-time", FULL_TIME), entry("Part-time", PART_TIME), entry("Other", OTHER));

	JobType(String label)
	{
		this.label = label;
	}

	public static JobType labelToEnum(String label)
	{
		return labelToEnumMap.get(label);
	}

	public static ObservableList<String> getEnumItems()
	{
		ObservableList<String> list = FXCollections.observableArrayList();
		for (JobType type : JobType.values())
		{
			list.add(type.label);
		}
		return list;
	}
}
