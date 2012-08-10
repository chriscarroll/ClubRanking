package ie.chriscarroll.clubranking.activity;

import ie.chriscarroll.clubranking.R;
import ie.chriscarroll.clubranking.game.RankingBattleActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Chris Carroll chris.carroll1@gmail.com
 */
public class ClubRankingActivity extends Activity implements OnClickListener
{
	private View buttonGame;
	private View buttonSettings;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViews();
		setUpListeners();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.buttonGame:
				Intent rankingBattle = new Intent(this, RankingBattleActivity.class);
				startActivity(rankingBattle);
				break;
			case R.id.buttonSettings:
				Intent settings = new Intent(this, SettingsActivity.class);
				startActivity(settings);
				break;
		}
	}

	/**
	 * Finds all views in main.xml
	 */
	private void findViews()
	{
		buttonGame = findViewById(R.id.buttonGame);
		buttonSettings = findViewById(R.id.buttonSettings);
	}

	/**
	 * Sets up listeners on all views
	 */
	private void setUpListeners()
	{
		buttonGame.setOnClickListener(this);
		buttonSettings.setOnClickListener(this);
	}
}
