package views;

import models.BranchedOutModel;
import models.TransitionalViewModel;

public class SkillController
{
	TransitionalViewModel tvm;
	BranchedOutModel model;

	public void setModels(BranchedOutModel newModel, TransitionalViewModel tvm)
	{
		this.tvm = tvm;
		model = newModel;
	}
	
}
