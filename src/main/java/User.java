
public abstract class User extends Entity
{
	// could set default values here if needed
	String name;
	String bio;
	String email;
	String phone;
	String avatarURL;
	String bannerURL;

	public User(int id, Page page, String name)
	{
		super(id, page);
		this.name = name;
	}
	
	public void followUser(User user) {
		
	}
	
	public void unfollowUser(User user) {
		
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

	
	
}
