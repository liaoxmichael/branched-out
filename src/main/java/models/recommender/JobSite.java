package models.recommender;

public enum JobSite {
	ON_SITE("On Site"), HYBRID("Hybrid"), REMOTE("Remote");

	public final String label;

	JobSite(String label)
	{
		this.label = label;
	}
}
