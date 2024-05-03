package models.adapters;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import models.Link;
import models.Skill;

public class SkillFX extends Skill
{
	SimpleIntegerProperty pageIdProperty;
	ObservableMap<String, List<Link>> observableLinks;
	ObservableList<String> observableExternalWebLinks;
	SimpleStringProperty titleProperty;
	SimpleStringProperty descriptionProperty;

	public SkillFX(Skill skill)
	{
		pageIdProperty = new SimpleIntegerProperty();
		pageIdProperty.set(skill.getPageId());
		
		observableLinks = FXCollections.observableMap(skill.getLinks());
		observableExternalWebLinks = FXCollections.observableList(skill.getExternalWebLinks());

		titleProperty = new SimpleStringProperty();
		titleProperty.set(skill.getTitle());

		descriptionProperty = new SimpleStringProperty();
		descriptionProperty.set(skill.getDescription());

		
	}

	@Override
	public String getTitle()
	{
		return titleProperty.get();
	}

	@Override
	public void setTitle(String title)
	{
		titleProperty.set(title);
	}

	public SimpleStringProperty titleProperty()
	{
		return titleProperty;
	}

	@Override
	public String getDescription()
	{
		return descriptionProperty.get();
	}

	@Override
	public void setDescription(String description)
	{
		descriptionProperty.set(description);
	}

	public SimpleStringProperty descriptionProperty()
	{
		return descriptionProperty;
	}

	@Override
	public int getPageId()
	{
		return pageIdProperty.get();
	}

	@Override
	public void setPageId(int pageId)
	{
		pageIdProperty.set(pageId);
	}

	public SimpleIntegerProperty pageIdProperty()
	{
		return pageIdProperty;
	}

}
