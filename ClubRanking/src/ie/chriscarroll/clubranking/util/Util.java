package ie.chriscarroll.clubranking.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

/**
 * @author Chris Carroll chris.carroll1@gmail.com
 */
public class Util
{
	/**
	 * Method to return club data.
	 * @param context
	 * @return
	 */
	public static SparseArray<String[]> getClubMap(Context context)
	{
		SparseArray<String[]> clubMap = new SparseArray<String[]>();
		try
		{
			File file = context.getFileStreamPath(ClubRankingConsts.CLUB_RANKINGS_FILENAME);
			if(file.exists())
			{
				InputStream is = new BufferedInputStream(context.getApplicationContext().openFileInput(ClubRankingConsts.CLUB_RANKINGS_FILENAME));

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

	
	
	
	// private boolean checkExternalMedia(){
		// boolean mExternalStorageAvailable = false;
		// boolean mExternalStorageWriteable = false;
		// String state = Environment.getExternalStorageState();
		// if (Environment.MEDIA_MOUNTED.equals(state)) {
		// // We can read and write the media
		// mExternalStorageAvailable = mExternalStorageWriteable = true;
		// } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		// // We can only read the media
		// mExternalStorageAvailable = true;
		// mExternalStorageWriteable = false;
		// } else {
		// // Something else is wrong. It may be one of many other states,
		// but all we need
		// // to know is we can neither read nor write
		// Log.i(TAG,"State="+state+" Not good");
		// mExternalStorageAvailable = mExternalStorageWriteable = false;
		// }
		// Log.i(TAG,"Available="+mExternalStorageAvailable+"
		// Writeable="+mExternalStorageWriteable+" State"+state);
		// return (mExternalStorageAvailable && mExternalStorageWriteable);
		// }
}
