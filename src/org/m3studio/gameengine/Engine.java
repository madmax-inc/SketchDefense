package org.m3studio.gameengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class Engine implements SurfaceHolder.Callback, OnTouchListener {
	private TreeSet<VisibleGameObject> objectsRenderingPipeline;
	private TreeSet<Background> backgroundsRenderingPipeline;
	//private TreeSet<GuiObject> guiObjectsPipeline;
	
	private ArrayList<GameObject> gameObjectsPipeline;
	private ArrayList<Animation> animationsPipeline;
	
	private Object globalObjectsMutex;
	
	private GameCameraObject cameraObject;
	
	private ArrayList<TouchHandler> touchHandlers;
	
	private DrawingThread drawingThread;
	private GameObjectThread gameObjectThread;
	private AnimationThread animationThread;
	
	private int dummyColor;
	
	private SurfaceView view;
	private SurfaceHolder holder;
	private Context context;
	
	public Engine(Context context, boolean limitFps, int dummyColor, int fpsLimit) {
		this.context = context;
		
		//Creating SurfaceView
		view = new SurfaceView(context);
		holder = view.getHolder();
		
		//Registering callbacks
		holder.addCallback(this);
		view.setOnTouchListener(this);
		
		//Initialising resources factory
		SpriteFactory.getInstance().setResources(context.getResources());
		
		//Creating pipelines
		objectsRenderingPipeline = new TreeSet<VisibleGameObject>();
		backgroundsRenderingPipeline = new TreeSet<Background>();
		gameObjectsPipeline = new ArrayList<GameObject>();
		animationsPipeline = new ArrayList<Animation>();
		
		//Creating objects mutex
		globalObjectsMutex = new Object();
		
		//Creating camera object
		cameraObject = new GameCameraObject();
		
		//Creating touch handlers
		touchHandlers = new ArrayList<TouchHandler>();
		touchHandlers.add(new GameObjectTouchHandler(this, objectsRenderingPipeline, globalObjectsMutex));
		touchHandlers.add(new TouchCameraControlller(cameraObject));
		
		//Creating threads
		drawingThread = new DrawingThread(this, limitFps, fpsLimit);
		gameObjectThread = new GameObjectThread(gameObjectsPipeline, globalObjectsMutex);
		animationThread = new AnimationThread(animationsPipeline, globalObjectsMutex);
		
		drawingThread.setPriority(Thread.MAX_PRIORITY);
		gameObjectThread.setPriority(Thread.MIN_PRIORITY);
		animationThread.setPriority(Thread.MIN_PRIORITY);
		
		//Setting dummy color to fill the screen
		this.dummyColor = dummyColor;
	}
	
	public Engine(Context context) {
		this(context, false, Color.BLACK, 50);
	}
	
	
	public SurfaceView getView() {
		return view;
	}
	
	public SurfaceHolder getHolder() {
		return holder;
	}
	
	//Pipelines handle
	public void addVisibleGameObject(VisibleGameObject object) {
		synchronized(objectsRenderingPipeline) {
			objectsRenderingPipeline.add(object);
		}
		
		addGameObject(object);
	}
	
	public void addGameObject(GameObject object) {
		object.setEngine(this);
		
		synchronized(gameObjectsPipeline) {
			gameObjectsPipeline.add(object);
		}
	}
	
	public void addAnimation(Animation animation) {
		animation.setEngine(this);
		
		synchronized(animationsPipeline) {
			animationsPipeline.add(animation);
		}
	}
	
	public void addBackground(Background background) {
		synchronized (backgroundsRenderingPipeline) {
			backgroundsRenderingPipeline.add(background);
		}
	}
	
	public void removeVisibleGameObject(VisibleGameObject object) {
		synchronized (objectsRenderingPipeline) {
			objectsRenderingPipeline.remove(object);
		}
		
		removeGameObject(object);
	}
	
	public void removeGameObject(GameObject object) {
		synchronized (gameObjectsPipeline) {
			gameObjectsPipeline.remove(object);
		}
	}
	
	public void removeAnimation(Animation animation) {
		synchronized (animationsPipeline) {
			animationsPipeline.remove(animation);
		}
	}
	
	public void removeBackground(Background background) {
		synchronized (backgroundsRenderingPipeline) {
			backgroundsRenderingPipeline.remove(background);
		}
	}
	
	public GameCameraObject getCameraObject() {
		return cameraObject;
	}
	
	public Canvas redraw(Canvas canvas) {
		//Filling all the screnn with dummy paint
		canvas.drawColor(dummyColor);
		
		//Creating projection matrix
		Matrix projection = cameraObject.getMatrix();
		
		//Drawing backgrounds
		synchronized (backgroundsRenderingPipeline) {
			for (Iterator<Background> it = backgroundsRenderingPipeline.iterator(); it.hasNext();) {
				Background back = it.next();
				
				Bitmap backgroundBitmap = back.getBitmap();
				Matrix matrix = new Matrix(back.getMatrix());
				matrix.postConcat(projection);
				
				canvas.drawBitmap(backgroundBitmap, matrix, null);
			}
		}
		
		//Drawing objects
		synchronized (objectsRenderingPipeline) {
			for (Iterator<VisibleGameObject> it = objectsRenderingPipeline.iterator(); it.hasNext();) {
				VisibleGameObject object = it.next();

				Sprite sprite = object.getSprite();
				int frameNum = object.getFrameNum();

				Bitmap spriteBitmap = sprite.getBitmap(frameNum);

				Matrix matrix = object.getMatrix();

				matrix.postConcat(projection);

				canvas.drawBitmap(spriteBitmap, matrix, null);
			}
		}
		
		
		//Drawing GUI
		
		return canvas;
	}
	

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		drawingThread.start();
		gameObjectThread.start();
		animationThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		drawingThread.Stop();
		gameObjectThread.Stop();
		animationThread.Stop();
		
		boolean flag = true;
		while (flag) {
			try {
				drawingThread.join();
				gameObjectThread.join();
				animationThread.join();
				flag = false;
			} catch (InterruptedException e) {
			}
		}
	}


	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int handlersCount = touchHandlers.size();
		int pointersCount = event.getPointerCount();
		
		//Map all the points to its scene coordinates
		Matrix inverse = new Matrix();
		cameraObject.getMatrix().invert(inverse);
		
		float posArray[] = new float[2 * pointersCount];
		
		for (int i = 0; i < pointersCount; i++) {
			posArray[2 * i] = event.getX(i);
			posArray[2 * i + 1] = event.getY(i);
		}
		
		inverse.mapPoints(posArray);
		
		Vector mappedPoints[] = new Vector[pointersCount];
		
		for (int i = 0; i < pointersCount; i++)
			mappedPoints[i] = new Vector(posArray[2 * i], posArray[2 * i + 1]);
		
		for (int i = 0; i < handlersCount; i++)
			if (touchHandlers.get(i).handleTouchEvent(event, mappedPoints))
				break;
		
		return true;
	}
}
