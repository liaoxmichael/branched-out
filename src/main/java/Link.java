
public class Link extends IdentifiableObject
{
	
	Page page;
	enum RelationshipType {
		HAS_SKILL,
		REQUIRES_SKILL,
		
		HAS_PROJECTS,
		
		FOLLOWING_PERSON,
		FOLLOWER_PERSON,
		MENTOR_PERSON,
		CONTRIBUTOR_PERSON,
		COORDINATOR_PERSON,
		JOB_APPLICANT_PERSON,
		
		FROM_COMPANY,
	}
	RelationshipType relation;

	public Link(int id)
	{
		super(id);
	}

}
