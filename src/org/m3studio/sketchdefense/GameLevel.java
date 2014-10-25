package org.m3studio.sketchdefense;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.res.AssetFileDescriptor;

import org.m3studio.gameengine.core.Vector;

public class GameLevel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Vector> creepsSpawnPoints;
	private ArrayList<Vector> towersPoints;
	private ArrayList<Vector> pathPoints;
	private Vector creepsTarget;

	private GameLevel() {
		creepsSpawnPoints = new ArrayList<Vector>();
		towersPoints = new ArrayList<Vector>();
		pathPoints = new ArrayList<Vector>();
		creepsTarget = new Vector(); 
	}
	
	public static GameLevel readLevelFromAsset(AssetFileDescriptor asset) throws IOException, ClassNotFoundException {
		FileInputStream fileStream = asset.createInputStream();
		ObjectInputStream objStream = new ObjectInputStream(fileStream);
		
		GameLevel gameLevel = (GameLevel) objStream.readObject();
		
		return gameLevel;
	}
}
