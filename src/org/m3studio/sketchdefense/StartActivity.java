package org.m3studio.sketchdefense;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO Set immersive mode
		if (android.os.Build.VERSION.SDK_INT >= 19)
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
		
		//Creating typeface
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/sketchdefense.otf");
		
		//Setting layout
		setContentView(R.layout.activity_start);
		
		//Setting typeface
		Button startGameButton = (Button) findViewById(R.id.button_startGame);
		Button shopButton = (Button) findViewById(R.id.button_shop);
		Button achievementsButton = (Button) findViewById(R.id.button_achievements);
		Button leaderboardButton = (Button) findViewById(R.id.button_leaderboard);
		
		startGameButton.setTypeface(typeface);
		shopButton.setTypeface(typeface);
		achievementsButton.setTypeface(typeface);
		leaderboardButton.setTypeface(typeface);
	}
	
	public void onStartGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

}
