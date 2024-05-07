package models.recommender;

public enum JobType {
	INTERNSHIP("Internship"), SEASONAL("Seasonal"), TEMPORARY("Temporary"), VOLUNTEER("Volunteer"),
	FULL_TIME("Full-time"), PART_TIME("Part-time"), OTHER("Other");

	public final String label;

	JobType(String label)
	{
		this.label = label;
	}
}
