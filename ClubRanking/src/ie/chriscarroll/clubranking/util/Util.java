package ie.chriscarroll.clubranking.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Util
{
	/**
	 * Method to return club data.
	 * @param context
	 * @return
	 */
	public static HashMap<Integer, String[]> getClubMap(Context context)
	{
		HashMap<Integer, String[]> clubMap = new HashMap<Integer, String[]>();
		try
		{
			File file = context.getFileStreamPath(ClubRankingConsts.RANKINGS_FILENAME);
			if(file.exists())
			{
				InputStream is = new BufferedInputStream(context.getApplicationContext().openFileInput(ClubRankingConsts.RANKINGS_FILENAME));

				byte[] buffer = new byte[is.available()];
				while (is.read(buffer) != -1);
				String jsontext = new String(buffer);
				JSONArray entries = new JSONArray(jsontext);

				for (int i = 0; i < entries.length(); i++)
				{
					JSONObject post = entries.getJSONObject(i);
					clubMap.put(i, new String[] { post.getString("clubName"), post.getString("clubId") });
				}
				return clubMap;
			}
			else{
				//TODO: create file
				return null;
			}
		}
		catch (Exception e)
		{
			Log.e("Util.getClubMap", e.toString());
			return null;
		}
	}

}
