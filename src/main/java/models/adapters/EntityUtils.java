package models.adapters;

import static java.util.Map.entry;

import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Company;
import models.JobPosting;
import models.Person;
import models.Project;
import models.Skill;

public final class EntityUtils
{
	public final static ObservableList<String> entityTypes = FXCollections.observableArrayList("Companies",
			"Job Postings", "People", "Skills", "Projects");

	public static Map<String, Class<?>> typeToClassMap = Map.ofEntries(entry("Companies", Company.class),
			entry("Job Postings", JobPosting.class), entry("People", Person.class), entry("Projects", Project.class),
			entry("Skills", Skill.class));

	private EntityUtils() // do not instantiate
	{

	}
}
