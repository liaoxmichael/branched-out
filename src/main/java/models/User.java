package models;

import java.util.ArrayList;
import java.util.Objects;

public abstract class User extends Entity
{
	// could set default values here if needed
	String name;
	String bio;
	String email;
	String phone;
	String avatarURL = "/person.circle.png"; // default
	String bannerURL = "/banner.png"; // default

	String password; // new

	public User()
	{
		super();
	}

	public User(String name, String email, IdentifiableObjectManagerInterface manager)
	{
		super(manager);
		this.name = name;
		this.email = email;
		links.put("following", new ArrayList<Link>());
		links.put("followers", new ArrayList<Link>());
		links.put("projects", new ArrayList<Link>());

		// set up page with editing privilege
		page.addEditor(this);
	}

	public void followUser(User user)
	{
		Link newLink = new Link(user.fetchPage(), Link.RelationshipType.FOLLOWING_USER, manager);
		int linkIndex = links.get("following").indexOf(newLink);

		if (linkIndex != -1) // if already following: early termination
		{
			return;
		} // else

		links.get("following") // guaranteed because of constructor; else risky and could return null
				.add(newLink);

		// it's reciprocal; need to add to other user's list
		user.getLinks().get("followers").add(new Link(page, Link.RelationshipType.FOLLOWER_USER, manager));
	}

	public void unfollowUser(User user)
	{
		Link target = new Link(user.fetchPage(), Link.RelationshipType.FOLLOWING_USER, manager);
		links.get("following").remove(target); // this will return false if it's not there; no big deal.
		// it's reciprocal; need to remove from other user's list
		user.getLinks().get("followers").remove(new Link(page, Link.RelationshipType.FOLLOWER_USER, manager));
	}

	public boolean verifyUser(String username, String pass)
	{
		if (password == null) {
			return email.equals(username);
		}
		return email.equals(username) && password.equals(pass);
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the bio
	 */
	public String getBio()
	{
		return bio;
	}

	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the avatarURL
	 */
	public String getAvatarURL()
	{
		return avatarURL;
	}

	/**
	 * @param avatarURL the avatarURL to set
	 */
	public void setAvatarURL(String avatarURL)
	{
		this.avatarURL = avatarURL;
	}

	/**
	 * @return the bannerURL
	 */
	public String getBannerURL()
	{
		return bannerURL;
	}

	/**
	 * @param bannerURL the bannerURL to set
	 */
	public void setBannerURL(String bannerURL)
	{
		this.bannerURL = bannerURL;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(avatarURL, bannerURL, bio, email, name, phone);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(avatarURL, other.avatarURL) && Objects.equals(bannerURL, other.bannerURL)
				&& Objects.equals(bio, other.bio) && Objects.equals(email, other.email)
				&& Objects.equals(name, other.name) && Objects.equals(phone, other.phone);
	}

	@Override
	public String toString()
	{
		return name;
	}

}
