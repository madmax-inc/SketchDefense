package org.m3studio.sketchdefense;

import org.m3studio.gameengine.Engine;
import org.m3studio.gameengine.LagrangeInterpolator;
import org.m3studio.gameengine.Sprite;
import org.m3studio.gameengine.SpriteFactory;
import org.m3studio.gameengine.Vector;
import org.m3studio.gameengine.VisibleGameObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Engine engine = new Engine(this);
		
		Sprite hero = SpriteFactory.getInstance().makeSpriteFromResourceStrip(R.drawable.strip3, 2, 10, 10, new Vector(20, 20));
		Sprite background = SpriteFactory.getInstance().makeSpriteFromResourceStrip(R.drawable.back, 1, 1, 1, new Vector(0, 0));
		
		VisibleGameObject back = new VisibleGameObject(background, new Vector(0.0f, 0.0f), -3.0f);
		
		engine.addVisibleGameObject(back);
		
		int count = 15;
		
		for (int i = 0; i < count; i++) {
			VisibleGameObject obj = new VisibleGameObject(hero, new Vector((float) (Math.random() * 1500), (float) (Math.random() * 1000)), 0.0f);
			obj.setScale(2);
			
			engine.addVisibleGameObject(obj);
			obj.addAnimationToAnimationPipeline();
		}
		
		setContentView(engine.getView());
	}

}
