package org.m3studio.sketchdefense;

import org.m3studio.gameengine.core.Engine;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Engine engine = new Engine(this);
		
		FifteensScene scene = new FifteensScene(engine);
		
		setContentView(engine.getView());
		scene.start();
	}
	
}
