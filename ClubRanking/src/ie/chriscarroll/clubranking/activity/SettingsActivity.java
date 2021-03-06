package ie.chriscarroll.clubranking.activity;

import ie.chriscarroll.clubranking.R;
import ie.chriscarroll.clubranking.util.ClubRankingConsts;
import ie.chriscarroll.clubranking.util.Util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author Chris Carroll chris.carroll1@gmail.com
 */
public class SettingsActivity extends Activity implements OnClickListener
{
	private View buttonDownloadRankings;
	private View buttonDownloadCrests;
	private View buttonDownloadJerseys;

	private ProgressBar progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	private int numOfClubs = 0;

	private SparseArray<String[]> clubMap = new SparseArray<String[]>();

	private static final int STATUS_DIALOG_ID = 0;

	/**
	 * Called when the activity is first created.
	 */
	// TODO: Should this be public???
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		findViews();
		setUpListeners();

		clubMap = Util.getClubMap(getApplicationContext());
	}

	@Override
	public void onClick(View v)
	{
		long startTime = 0;
		long endTime = 0;
		long totalSeconds = 0;
		switch (v.getId())
		{
			case R.id.buttonDownloadRankings:
				startTime = System.currentTimeMillis();
				getClubRankings();
				endTime = System.currentTimeMillis();
				totalSeconds = endTime - startTime;
				System.out.println("Total time to complete function was " + (totalSeconds) + " milliseconds");
				break;
			case R.id.buttonDownloadCrests:
				startTime = System.currentTimeMillis();
				getClubCrests();
				endTime = System.currentTimeMillis();
				totalSeconds = endTime - startTime;
				System.out.println("Total time to complete function was " + (totalSeconds) + " milliseconds");
				break;
			case R.id.buttonDownloadJerseys:
				getClubJerseys();
				break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case STATUS_DIALOG_ID:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setIcon(android.R.drawable.btn_star);
				builder.setMessage("");
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(getApplicationContext(), "Dialog Closed", Toast.LENGTH_SHORT).show();
						return;
					}
				});
				return builder.create();
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		super.onPrepareDialog(id, dialog);
		switch (id)
		{
			case STATUS_DIALOG_ID:
				// Some initialization needed.
				AlertDialog myDialog = (AlertDialog) dialog;
				SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				myDialog.setMessage("Download completed at " + dFormat.format(new Date()));
				break;
		}
		return;
	}

	/**
	 * Finds all views in settings.xml
	 */
	private void findViews()
	{
		buttonDownloadRankings = findViewById(R.id.buttonDownloadRankings);
		buttonDownloadCrests = findViewById(R.id.buttonDownloadCrests);
		buttonDownloadJerseys = findViewById(R.id.buttonDownloadJerseys);
		progressBar = (ProgressBar) findViewById(R.id.progressBarDownload);
	}

	/**
	 * Sets up listeners on all views
	 */
	private void setUpListeners()
	{
		buttonDownloadRankings.setOnClickListener(this);
		buttonDownloadCrests.setOnClickListener(this);
		buttonDownloadJerseys.setOnClickListener(this);
	}

	/**
	 * Download and save club rankings from external website
	 */
	private void getClubRankings()
	{
		try
		{
			// TODO: Error handling if url is unreachable, don't allow app to
			// crash
			// for starters, check that an network connection exists
			URL url = new URL(ClubRankingConsts.CLUB_RANKINGS_HTTP_URL);
			URLConnection conn = url.openConnection();

			// TODO: set the connection timeout to 5 seconds?
			// conn.setConnectTimeout(5000);
			// conn.setReadTimeout(10000);

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// TODO: having to read until the third line is not very safe
			String line = rd.readLine();
			line = rd.readLine();
			line = rd.readLine();

			FileOutputStream fos = openFileOutput(ClubRankingConsts.CLUB_RANKINGS_FILENAME, Context.MODE_PRIVATE);
			fos.write(line.getBytes());
			fos.close();

			InputStream input = new BufferedInputStream(openFileInput(ClubRankingConsts.CLUB_RANKINGS_FILENAME));
			byte[] buffer = new byte[8192];

			try
			{
				Log.i("ClubRankingActivity.parseData", "writing contents");
				for (int length = 0; (length = input.read(buffer)) != -1;)
				{
					System.out.write(buffer, 0, length);
				}
				Log.i("ClubRankingActivity.parseData", "finished writing");
			}
			finally
			{
				input.close();
				showDialog(STATUS_DIALOG_ID);
			}

		}
		catch (Exception e)
		{
			Log.e("ClubRankingActivity.parseData", e.toString());
		}
	}

	/**
	 * Download and save club crests from external website
	 */
	// TODO: Create .nomedia file
	private void getClubCrests()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			// <uses-permission
			// android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
			File folder = new File(Environment.getExternalStorageDirectory() + ClubRankingConsts.CREST_SAVE_LOCATION);
			try
			{
				if (!folder.exists())
				{
					boolean dir = new File(Environment.getExternalStorageDirectory() + ClubRankingConsts.CREST_SAVE_LOCATION).mkdirs();
					Log.v("creating directory", Boolean.toString(dir));
				}
				try
				{
					numOfClubs = clubMap.size();
					progressBar.setMax(numOfClubs);
					Thread myThread = new Thread(new Runnable() {
						public void run()
						{
							while (progressBarStatus < numOfClubs)
							{
								// process some tasks
								progressBarStatus = downloadCrest(progressBarStatus);

								// Update the progress bar
								progressBarHandler.post(new Runnable() {
									public void run()
									{
										progressBar.setProgress(progressBarStatus);
									}
								});
							}
							// thread almost complete
							try
							{
								//TODO: java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
								//SettingsActivity.this.showDialog(STATUS_DIALOG_ID);
								File noMedia = new File(Environment.getExternalStorageDirectory() + ClubRankingConsts.CREST_SAVE_LOCATION, ".nomedia");
								noMedia.createNewFile();
							}
							catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					myThread.start();
					myThread.getState();

				}
				catch (NullPointerException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				// showDialog(STATUS_DIALOG_ID);
			}
		}
		else
		{
			Log.e("Battle.getAllCrests", "External Storage Directory not mounted.");
		}
	}

	public int downloadCrest(int argCount)
	{
		while (argCount < numOfClubs)
		{
			String[] myClubDetails = clubMap.get(argCount);
			if (myClubDetails != null)
			{
				try
				{
					String clubId = myClubDetails[1];
					File imageOutputFile = new File(Environment.getExternalStorageDirectory() + ClubRankingConsts.CREST_SAVE_LOCATION, clubId
							+ ClubRankingConsts.CREST_IMAGE_EXT);
					FileOutputStream fos = new FileOutputStream(imageOutputFile);
					getImage(ClubRankingConsts.CREST_HTTP_URL + clubId + ClubRankingConsts.CREST_IMAGE_EXT).compress(Bitmap.CompressFormat.PNG, 100,
							fos);
					fos.close();
				}
				catch (Exception e)
				{
					Log.e("Battle.getAllCrests", "error with FileOutputStream for count = " + argCount);
				}
			}
			else
			{
				Log.e("Battle.getAllCrests", "null for count = " + argCount);
			}
			return ++argCount;
		}
		return ++argCount;
	}

	/**
	 * Download and save club rankings from external website
	 */
	// TODO: Create .nomedia file
	private void getClubJerseys()
	{
		// TODO:
	}

	private Bitmap getImage(String fileUrl)
	{

		URL myFileUrl = null;
		try
		{
			myFileUrl = new URL(fileUrl);
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			Bitmap bmImg = BitmapFactory.decodeStream(is);
			return bmImg;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
