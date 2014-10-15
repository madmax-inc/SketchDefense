package org.m3studio.gameengine.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.m3studio.gameengine.utils.TouchCameraControlller;

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
	//Collections
	private TreeSet<VisibleGameObject> objectsRenderingPipeline;
	private TreeSet<Background> backgroundsRenderingPipeline;
	//private TreeSet<GuiObject> guiObjectsPipeline;
	
	private ArrayList<GameObject> gameObjectsPipeline;
	private ArrayList<Animation> animationsPipeline;
	
	//Collections Buffers
	private CollectionBuffer<VisibleGameObject> objectsRenderingPipelineBuffer;
	private CollectionBuffer<Background> backgroundsRenderingPipelineBuffer;
	//private CollectionBuffer<GuiObject> guiObjectsPipelineBuffer;
	private CollectionBuffer<GameObject> gameObjectsPipelineBuffer;
	private CollectionBuffer<Animation> animationsPipelineBuffer;
	
	private GameCameraObject cameraObject;
	
	private ArrayList<TouchHandler> touchHandlers;
	
	private boolean limitFps;
	private int fpsLimit;
	
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
		ResourceFactory.getInstance().setResources(context.getResources());
		
		//Creating pipelines
		objectsRenderingPipeline = new TreeSet<VisibleGameObject>();
		backgroundsRenderingPipeline = new TreeSet<Background>();
		gameObjectsPipeline = new ArrayList<GameObject>();
		animationsPipeline = new ArrayList<Animation>();
		
		//Creating pipelines buffers
		objectsRenderingPipelineBuffer = new CollectionBuffer<VisibleGameObject>();
		backgroundsRenderingPipelineBuffer = new CollectionBuffer<Background>();
		gameObjectsPipelineBuffer = new CollectionBuffer<GameObject>();
		animationsPipelineBuffer = new CollectionBuffer<Animation>();
		
		//Creating camera object
		cameraObject = new GameCameraObject();
		addGameObject(cameraObject);
		
		//Creating touch handlers
		touchHandlers = new ArrayList<TouchHandler>();
		touchHandlers.add(new GameObjectTouchHandler(objectsRenderingPipeline));
		touchHandlers.add(new TouchCameraControlller(cameraObject));
		
		this.limitFps = limitFps;
		this.fpsLimit = fpsLimit;
		
		//Setting dummy color to fill the screen
		this.dummyColor = dummyColor;
	}
	
	public Engine(Context context) {
		this(context, true, Color.BLACK, 35);
	}
	
	
	public SurfaceView getView() {
		return view;
	}
	
	public SurfaceHolder getHolder() {
		return holder;
	}
	
	//Pipelines handle
	public void addVisibleGameObject(VisibleGameObject object) {
		objectsRenderingPipelineBuffer.add(object);
		
		addGameObject(object);
	}
	
	public void addGameObject(GameObject object) {
		object.setEngine(this);
		
		gameObjectsPipelineBuffer.add(object);
	}
	
	public void addAnimation(Animation animation) {
		animation.setEngine(this);
		
		animationsPipelineBuffer.add(animation);
	}
	
	public void addBackground(Background background) {
		backgroundsRenderingPipelineBuffer.add(background);
	}
	
	public void removeVisibleGameObject(VisibleGameObject object) {
		objectsRenderingPipelineBuffer.remove(object);
		
		removeGameObject(object);
	}
	
	public void removeGameObject(GameObject object) {
		gameObjectsPipelineBuffer.remove(object);
	}
	
	public void removeAnimation(Animation animation) {
		animationsPipelineBuffer.remove(animation);
	}
	
	public void removeBackground(Background background) {
		backgroundsRenderingPipelineBuffer.remove(background);
	}
	
	public GameCameraObject getCameraObject() {
		return cameraObject;
	}
	
	public void redraw(Canvas canvas) {
		//Filling all the screen with dummy paint
		canvas.drawColor(dummyColor);
		
		//Creating projection matrix
		Matrix projection = cameraObject.getMatrix();
		
		//Drawing backgrounds
		for (Iterator<Background> it = backgroundsRenderingPipeline.iterator(); it.hasNext();) {
			Background back = it.next();

			Bitmap backgroundBitmap = back.getBitmap();
			Matrix matrix = back.getMatrix();
			matrix.postConcat(projection);

			canvas.drawBitmap(backgroundBitmap, matrix, null);
		}
		
		//Drawing objects
		for (Iterator<VisibleGameObject> it = objectsRenderingPipeline.iterator(); it.hasNext();) {
			VisibleGameObject object = it.next();

			Bitmap objectBitmap = object.getBitmap();

			Matrix matrix = object.getMatrix();

			matrix.postConcat(projection);

			canvas.drawBitmap(objectBitmap, matrix, null);
		}
		
		//Drawing GUI
		
		
		//Updating rendering pipelines
		backgroundsRenderingPipelineBuffer.doUpdate(backgroundsRenderingPipeline);
		objectsRenderingPipelineBuffer.doUpdate(objectsRenderingPipeline);
	}
	

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		//Creating threads
		drawingThread = new DrawingThread(this, limitFps, fpsLimit);
		gameObjectThread = new GameObjectThread(gameObjectsPipeline, gameObjectsPipelineBuffer);
		animationThread = new AnimationThread(animationsPipeline, animationsPipelineBuffer);
		
		drawingThread.setPriority(Thread.MAX_PRIORITY);
		gameObjectThread.setPriority(Thread.MIN_PRIORITY);
		animationThread.setPriority(Thread.MIN_PRIORITY);
		
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
		Matrix direct = cameraObject.getMatrix();
		direct.invert(inverse);
		
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
