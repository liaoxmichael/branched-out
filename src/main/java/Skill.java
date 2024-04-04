
import java.util.ArrayList;

import org.springframework.web.client.RestClient;

public class Skill extends Post
{

	public Skill(String title, IdentifiableObjectManagerInterface manager)
	{
		super(title, manager);
		links.put("mentors", new ArrayList<Link>());
	}

	public void addMentor(Person person)
	{
		Link newLink = new Link(person.getPage(), Link.RelationshipType.MENTOR_PERSON, manager);
		if (links.get("mentors").indexOf(newLink) != -1) // if already mentor, terminate early
		{
			return;
		} // else

		links.get("mentors") // guaranteed because of constructor; else risky and could return null
				.add(newLink);
	}

	public boolean removeMentor(Person person)
	{
		return links.get("mentors").remove(new Link(person.getPage(), Link.RelationshipType.MENTOR_PERSON, manager));
	}

	public record SkillResponse(String request, boolean successful, String message, Skill data) {
	}

	public static final String RESOURCE = "skills";
	public static final String RESOURCE_DESC = "All the skills on Branched Out";

	public static Skill retrieve(int id)
	{
		RestClient client = RestClient.create();

		if (RestUtilities.doesResourceExist(id, RESOURCE))
		{
			SkillResponse response = client.get()
					.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(id))).retrieve()
					.body(SkillResponse.class);

			return response.data;
		}
		// else
		return null;
	}

	public boolean store()
	{
		RestClient client = RestClient.create();
		if (!RestUtilities.doesResourceExist(RESOURCE))
		{ // need to create the thing!
			RestUtilities.createResource(RESOURCE, RESOURCE_DESC);
		}
		SkillResponse result = client.post()
				.uri(RestUtilities.join(RestUtilities.TEAM_URI, RESOURCE, String.valueOf(getId()))).body(this)
				.retrieve().body(SkillResponse.class);
		return result.successful;
	}

	@Override
	public String toString()
	{
		return "Skill [title=" + title + ", description=" + description + ", id=" + id + ", links=" + links
				+ ", externalWebLinks=" + externalWebLinks + "]";
	}

}
