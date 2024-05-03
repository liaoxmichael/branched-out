package models.recommender;

public class StrategyFactory
{
	private StrategyFactory()
	{

	} // no need to instantiate

	public static RecommendationStrategy constructStrategy(RecommendationStrategyKind type)
	{
		switch (type)
		{
		case ALL:
			return new RecommendAll();
		case BY_SITE:
			return new RecommendBySitePreference();
		case BY_SKILL:
			return new RecommendBySkill();
		case BY_TYPE:
			return new RecommendByTypePreference();
		default: // should not ever happen but just in case
			return null;
		}
	}
}
