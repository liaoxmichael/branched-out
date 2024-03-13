import java.util.ArrayList;

public class Company extends User
{

	public Company(String name, String email, IdentifiableObjectManager manager)
	{
		super(name, email, manager);
		links.put("projects", new ArrayList<Link>());
	}

	@Override
	public String toString()
	{
		return "Company [name=" + name + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", avatarURL="
				+ avatarURL + ", bannerURL=" + bannerURL + ", id=" + id + ", links=" + links + ", externalWebLinks="
				+ externalWebLinks + "]";
	}

}
