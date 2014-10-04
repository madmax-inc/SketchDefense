package org.m3studio.sketchdefense;
/*
import org.m3studio.gameengine.core.Background;
import org.m3studio.gameengine.core.Engine;
import org.m3studio.gameengine.core.ResourceFactory;
import org.m3studio.gameengine.core.Sprite;
import org.m3studio.gameengine.core.Vector;
import org.m3studio.gameengine.core.VisibleSpriteGameObject;
*/
import android.app.Activity;
import android.os.Bundle;
//import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*		Engine engine = new Engine(this);
		
		Sprite hero = ResourceFactory.getInstance().makeSpriteFromResourceStrip(R.drawable.strip3, 2, 10, 10, new Vector(20, 20));
		Background background = ResourceFactory.getInstance().makeBackgroundFromResource(R.drawable.back, new Vector(0, 0));
		
		engine.addBackground(background);
		
		int count = 15;
		
		for (int i = 0; i < count; i++) {
			VisibleSpriteGameObject obj = new VisibleSpriteGameObject(hero, new Vector((float) (Math.random() * 1500), (float) (Math.random() * 1000)), 0.0f);
			obj.setScale(2);
			
			engine.addVisibleGameObject(obj);
			obj.addAnimationToAnimationPipeline();
		}
*/		
		setContentView(R.layout.activity_start);
		
//		Toast.makeText(this, "Tap on the sprites in order to have some fun =)", Toast.LENGTH_LONG).show();
	}

}
