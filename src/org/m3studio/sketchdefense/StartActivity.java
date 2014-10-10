package org.m3studio.sketchdefense;

import org.m3studio.gameengine.utils.graph.GraphPath;
import org.m3studio.gameengine.utils.graph.GraphPathAStar;
import org.m3studio.gameengine.utils.graph.GraphVertex;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
		
		//Testing graph engine
		GraphVertex a = new GraphVertex();
		GraphVertex b = new GraphVertex();
		GraphVertex c = new GraphVertex();
		GraphVertex d = new GraphVertex();
		GraphVertex e = new GraphVertex();
		
		ConcreteGraphEdge ab = new ConcreteGraphEdge(a, b, 2);
		ConcreteGraphEdge ac = new ConcreteGraphEdge(a, c, 3);
		ConcreteGraphEdge bd = new ConcreteGraphEdge(b, d, 2);
		ConcreteGraphEdge cd = new ConcreteGraphEdge(c, d, 1);
		ConcreteGraphEdge be = new ConcreteGraphEdge(b, e, 4);
		ConcreteGraphEdge de = new ConcreteGraphEdge(d, e, 1);
		
		GraphPathAStar astar = new GraphPathAStar();
		GraphPath result = astar.getPathBetween(a, e);
		
		Log.d("GRAPH", result.toString());
	}

}
