package models.adapters;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import models.Company;
import models.Link;

public class CompanyFX extends Company
{
	SimpleIntegerProperty pageIdProperty;
	ObservableMap<String, List<Link>> observableLinks;
	ObservableList<String> observableExternalWebLinks;

	public CompanyFX(Company company)
	{
		pageIdProperty = new SimpleIntegerProperty();
		pageIdProperty.set(company.getPageId());

		observableLinks = FXCollections.observableMap(company.getLinks());
		observableExternalWebLinks = FXCollections.observableList(company.getExternalWebLinks());
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
