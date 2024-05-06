package models.adapters;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import models.JobPosting;
import models.SkillProficiency;

public class JobPostingFX extends JobPosting
{
	SimpleIntegerProperty weeklyHoursProperty;
	SimpleStringProperty payRangeProperty;
	
	SimpleStringProperty locationProperty;
	
	ObservableList<SkillProficiency> observableRequiredSkills;
	
}
