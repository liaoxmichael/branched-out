package models.adapters;

import models.JobPosting;

public class JobPostingFX extends JobPosting
{
	SimpleIntegerProperty weeklyHoursProperty;
	SimpleStringProperty payRangeProperty;
	
	SimpleStringProperty locationProperty;
	
	ObservableList<SkillProficiency> observableRequiredSkills;
	
}
