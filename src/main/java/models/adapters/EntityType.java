package models.adapters;

import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static java.util.Map.entry;

public enum EntityType {
	COMPANY("Companies"), JOB_POSTING("Job Postings"), PERSON("People"), SKILL("Skills");

	public final String label;
	private static final Map<String, EntityType> labelToEnumMap = Map.ofEntries(entry("Companies", COMPANY),
			entry("Job Postings", JOB_POSTING), entry("People", PERSON), entry("Skills", SKILL));

	EntityType(String label)
	{
		this.label = label;
	}

	public static EntityType labelToEnum(String label)
	{
		return labelToEnumMap.get(label);
	}

	public static ObservableList<String> getEnumItems()
	{
		ObservableList<String> list = FXCollections.observableArrayList();
		for (EntityType type : EntityType.values())
		{
			list.add(type.label);
		}
		return list;
	}
}
