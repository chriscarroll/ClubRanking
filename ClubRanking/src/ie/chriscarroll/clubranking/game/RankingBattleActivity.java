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
import android.widget.ImageButton;
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
	private ImageButton teamOneButton;
	private ImageButton teamTwoButton;
	private ProgressBar progressBar;
	private Integer currentClubRank;
	private Integer favClubRank;
	private Integer progress = 0;
	private String currentClubName = null;
	private String currentClubId = null;
	private int numberOfTeams = 0;
	private ImageView imView1;
	private ImageView imView2;
	private HashMap<Integer, String[]> clubMap = new HashMap<Integer, String[]>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);

		findViews();
		setUpListeners();
		resetGame();
		getRandomFavClubRanking();
		setQuestion();
		progressBar.setMax(206);
		progressText.setText("0 correct");
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.teamOne:
				if (currentClubRank < favClubRank)
				{
					progressBar.setProgress(progress++);
					progressText.setText(progress + " correct. " + currentClubName + " was ranked #" + currentClubRank);
					setQuestion();

				}
				else
				{
					progress = 0;
					progressBar.setProgress(progress);
					resetGame();
					setQuestion();
					progressText.setText(progress + " correct ");
					getRandomFavClubRanking();

				}
				break;
			case R.id.teamTwo:
				if (currentClubRank > favClubRank)
				{
					progressBar.setProgress(progress++);
					progressText.setText(progress + " correct. " + currentClubName + " was ranked #" + currentClubRank);
					setQuestion();

				}
				else
				{
					progress = 0;
					progressBar.setProgress(progress);
					resetGame();
					setQuestion();
					progressText.setText(progress + " correct ");
					getRandomFavClubRanking();
				}
				break;
		}
	}

	/**
	 * Finds and assigns all views in main.xml
	 */
	private void findViews()
	{
		battleQuestion = (TextView) findViewById(R.id.battlequestion);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		teamOneButton = (ImageButton) findViewById(R.id.teamOne);
		teamTwoButton = (ImageButton) findViewById(R.id.teamTwo);
		progressText = (TextView) findViewById(R.id.progress);
		imView1 = (ImageView) findViewById(R.id.imageView1);
		imView2 = (ImageView) findViewById(R.id.imageView2);
	}

	/**
	 * Sets up listeners on all views
	 */
	private void setUpListeners()
	{
		teamOneButton.setOnClickListener(this);
		teamTwoButton.setOnClickListener(this);
	}

	private void getRandomFavClubRanking()
	{
		numberOfTeams = clubMap.size();
		Random randomGenerator = new Random();
		favClubRank = randomGenerator.nextInt(numberOfTeams);
	}

	private void resetGame()
	{
		clubMap = Util.getClubMap(getApplicationContext());
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

		currentClubName = null;
		currentClubId = null;

		if (clubMap.size() > 1)
		{
			while (currentClubName == null || currentClubRank == favClubRank)
			{
				currentClubRank = randomGenerator.nextInt(numberOfTeams);
				String[] myClubDetails = clubMap.get(currentClubRank);
				currentClubName = myClubDetails[0];
				currentClubId = myClubDetails[1];
			}
			// remove country
			clubMap.remove(currentClubRank);

			File dir = Environment.getExternalStorageDirectory();

			Bitmap image1 = BitmapFactory.decodeFile(dir + ClubRankingConsts.CREST_SAVE_LOCATION + currentClubId + ClubRankingConsts.CREST_IMAGE_EXT);
			Bitmap image2 = BitmapFactory.decodeFile(dir + ClubRankingConsts.CREST_SAVE_LOCATION + clubMap.get(favClubRank)[1] + ClubRankingConsts.CREST_IMAGE_EXT);

			imView1.setImageBitmap(image1);
			imView2.setImageBitmap(image2);
			
			teamOneButton.setImageBitmap(image1);
			teamTwoButton.setImageBitmap(image2);
			question = "\nWhich is the highest ranked club?";
		}
		else
		{
			question = "You Win!";
		}

		battleQuestion.setText(question);

	}

}
