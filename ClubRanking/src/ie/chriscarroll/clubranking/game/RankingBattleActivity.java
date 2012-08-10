package ie.chriscarroll.clubranking.game;

import ie.chriscarroll.clubranking.R;
import ie.chriscarroll.clubranking.util.ClubRankingConsts;
import ie.chriscarroll.clubranking.util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Chris Carroll chris.carroll1@gmail.com
 */
public class RankingBattleActivity extends Activity implements OnClickListener
{

	private TextView battleQuestion;
	private TextView progressText;
	private Button aboveButton;
	private Button belowButton;
	private ProgressBar progressBar;
	private Integer currentClubRank;
	private Integer favClubRanking;
	private Integer progress = 0;
	private String clubName = null;
	private String clubId = null;
	private int numberOfTeams = 0;
	private ImageView imView1;
	private ImageView imView2;
	private HashMap<Integer, String[]> clubMap = new HashMap<Integer, String[]>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);

		findViews();
		setUpListeners();
		clubMap = Util.getClubMap(getApplicationContext());
		// reset();
		getRandomFavClubRanking();
		setQuestion();
		progressBar.setMax(206);
		progressText.setText("0 correct");
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
	/**
	 * Finds and assigns all views in main.xml
	 */
	private void findViews()
	{
		battleQuestion = (TextView) findViewById(R.id.battlequestion);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		aboveButton = (Button) findViewById(R.id.above);
		belowButton = (Button) findViewById(R.id.below);
		progressText = (TextView) findViewById(R.id.progress);
		imView1 = (ImageView) findViewById(R.id.imageView1);
		imView2 = (ImageView) findViewById(R.id.imageView2);
	}

	/**
	 * Sets up listeners on all views
	 */
	private void setUpListeners()
	{
		aboveButton.setOnClickListener(this);
		belowButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.above:
				if (currentClubRank < favClubRanking)
				{
					progressBar.setProgress(progress++);
					progressText.setText(progress + " correct, " + clubName + " was ranked:" + currentClubRank);
					setQuestion();

				}
				else
				{
					progress = 0;
					progressBar.setProgress(progress);
					clubMap = Util.getClubMap(getApplicationContext());
					setQuestion();
					progressText.setText(progress + " correct ");
					getRandomFavClubRanking();

				}
				break;
			case R.id.below:
				if (currentClubRank > favClubRanking)
				{
					progressBar.setProgress(progress++);
					progressText.setText(progress + " correct, " + clubName + " was ranked:" + currentClubRank);
					setQuestion();

				}
				else
				{
					progress = 0;
					progressBar.setProgress(progress);
					clubMap = Util.getClubMap(getApplicationContext());
					setQuestion();
					progressText.setText(progress + " correct ");
					getRandomFavClubRanking();
				}
				break;
		}

	}

	
	private void getRandomFavClubRanking()
	{
		Random randomGenerator = new Random();
		favClubRanking = randomGenerator.nextInt(numberOfTeams);
	}

	private void reset()
	{
		// Resources res = getResources();
		// String[] countryRankings =
		// res.getStringArray(R.array.rankings_array);
		// for (int i = 0; i < countryRankings.length; i++) {
		// clubMap.put(i, countryRankings[i]);
		// }
	}

	private void setQuestion()
	{
		String question;
		Random randomGenerator = new Random();

		clubName = null;
		clubId = null;

		if (clubMap.size() > 1)
		{
			while (clubName == null || currentClubRank == favClubRanking)
			{
				currentClubRank = randomGenerator.nextInt(numberOfTeams);
				String[] myClubDetails = clubMap.get(currentClubRank);
				clubName = myClubDetails[0];
				clubId = myClubDetails[1];
			}
			// remove country
			clubMap.remove(currentClubRank);

			File dir = Environment.getExternalStorageDirectory();

			Bitmap image1 = BitmapFactory.decodeFile(dir + ClubRankingConsts.CREST_SAVE_LOCATION + clubId + ClubRankingConsts.CREST_IMAGE_EXT);
			Bitmap image2 = BitmapFactory.decodeFile(dir + "/uefa.images/temp/" + clubMap.get(favClubRanking)[1] + ClubRankingConsts.CREST_IMAGE_EXT);

			imView1.setImageBitmap(image1);
			imView2.setImageBitmap(image2);
			question = "\nIs " + clubName + " ranked higher than " + clubMap.get(favClubRanking)[0] + " ?";
		}
		else
		{
			question = "You Win!";
		}

		battleQuestion.setText(question);
		aboveButton.setText("Above " + clubMap.get(favClubRanking)[0]);
		belowButton.setText("Below " + clubMap.get(favClubRanking)[0]);
	}

}
