package ie.chriscarroll.clubranking.util;

/**
 * @author Chris Carroll chris.carroll1@gmail.com
 */
public class ClubRankingConsts
{
	public static final String CLUB_RANKINGS_HTTP_URL = "http://checkargos.com/Rankings/Rankings.php?rankingType=club";
	public static final String CLUB_RANKINGS_FILENAME = "uefa_club_rankings.txt";

	//TODO: Future enhancements - implement FIFA country world ranking
	//public static final String COUNTRY_RANKINGS_HTTP_URL = "http://checkargos.com/Rankings/Rankings.php?rankingType=country";
	//public static final String COUNTRY_RANKINGS_FILENAME = "fifa_country_rankings.txt";

	public static final String CREST_HTTP_URL = "http://img.uefa.com/imgml/TP/teams/logos/70x70/";
	public static final String CREST_IMAGE_EXT = ".png";
	public static final String CREST_SAVE_LOCATION = "/clubranking/images/crests/";

	public static final String JERSEY_HTTP_URL = "http://img.uefa.com/imgml/TP/teams/kits/80x105/";
	public static final String JERSEY_IMAGE_EXT = "_1.png";
	public static final String JERSEY_SAVE_LOCATION = "/clubranking/images/jerseys/";

}
