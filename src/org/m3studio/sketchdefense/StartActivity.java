package org.m3studio.sketchdefense;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Set fullscreen mode
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
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
