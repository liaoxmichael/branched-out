import java.util.Objects;

public class Link implements Identifiable
{

	int id;
	Page page;

	enum RelationshipType {
		HAS_SKILL, REQUIRES_SKILL,

		HAS_PROJECT,

		FOLLOWING_USER, FOLLOWER_USER, MENTOR_PERSON, CONTRIBUTOR_PERSON, COORDINATOR_PERSON, JOB_APPLICANT_PERSON,

		FROM_COMPANY,
	}

	RelationshipType relation;

	public Link(Page page, RelationshipType type, IdentifiableObjectManager manager)
	{
		id = manager.getNextId();
		this.page = page;
		relation = type;

		manager.register(this);
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(page, relation);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Link other = (Link) obj;
		return Objects.equals(page, other.page) && relation == other.relation;
	}

	@Override
	public String toString()
	{
		return "Link [id=" + id + ", page=" + page + ", relation=" + relation + "]";
	}

}
