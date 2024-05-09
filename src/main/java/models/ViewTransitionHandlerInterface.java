package models;

import javafx.collections.ObservableList;
import models.adapters.Displayable;

public interface ViewTransitionHandlerInterface
{

	void showMain(User user);

	void showSearchDisplay(ObservableList<Displayable> entities, String newLabel);

	void showProfile(User user);

	void showSkill(Skill skill);

	void showJobPosting(JobPosting job);

	void showBlockError();

	void showLogin();

}