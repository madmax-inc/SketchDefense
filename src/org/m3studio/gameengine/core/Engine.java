package org.m3studio.gameengine.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

/**
 * @author madmax
 * @version 0.2 beta
 * <p>Engine is the base class, representing M3Studio 2D Game Engine.
 * In order to write the game, you must create one instance of Engine.</p>
 * 
 * <p>It contains 4 pipelines to handle the game state:</p>
 * <ol>
 * <li>The Rendering Pipeline - holds all the game objects to be rendered.</li>
 * <li>The Game Objects Pipeline - holds all the objects, that needs to recieve ingame events.</li>
 * <li>The Backgrounds Pipeline - holds all the backgrounds to be rendered.</li>
 * <li>The Animations Pipeline - holds all animations to be performed.</li>
 * </ol>
 * 
 * <p>An access to the pipelines is granted with corresponding methods:</p>
 * <ul>
 * <li>{@link #addVisibleGameObject()}</li>
 * <li>{@link #addGameObject()}</li>
 * <li>{@link #addBackground()}</li>
 * <li>{@link #addAnimation()}</li>
 * <li>{@link #removeVisibleGameObject()}</li>
 * <li>{@link #removeGameObject()}</li>
 * <li>{@link #removeBackground()}</li>
 * <li>{@link #removeAnimation()}</li>
 * </ul>
 */
public final class Engine implements SurfaceHolder.Callback, OnTouchListener {
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
	
	/**
	 * <p>Creates a Game Engine with specified parameters</p>
	 * @param context Context in which Engine is executed (ex. Activity, Service, e.t.c)
	 * @param limitFps Set this to true in order to limit fps by {@link fpsLimit}
	 * @param dummyColor Dummy color to repaint the visible surface by default
	 * @param fpsLimit Fps limit
	 */
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
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		
		cameraObject = new GameCameraObject(dm);
		addGameObject(cameraObject);
		
		//Creating touch handlers
		touchHandlers = new ArrayList<TouchHandler>();
		touchHandlers.add(new GameObjectTouchHandler(objectsRenderingPipeline));
		
		this.limitFps = limitFps;
		this.fpsLimit = fpsLimit;
		
		//Setting dummy color to fill the screen
		this.dummyColor = dummyColor;
	}
	
	/**
	 * <p>Creates a Game Engine with specified context, Black dummy color ({@link dummyColor}) and with no fps limit</p>
	 * @param context Context in which Engine is executed (ex. Activity, Service, e.t.c)
	 */
	public Engine(Context context) {
		this(context, false, Color.BLACK, 50);
	}
	
	
	/**
	 * @return Returns a view in which all the graphics will be rendered
	 */
	public SurfaceView getView() {
		return view;
	}
	
	
	/**
	 * @return Returns a SurfaceHolder, associated with internal view
	 */
	public SurfaceHolder getHolder() {
		return holder;
	}
	
	/**
	 * @return {@link Context} in which engine is executed.
	 */
	public Context getContext() {
		return context;
	}
	
	/**
	 * <p>Adds a touch handler</p>
	 * @param handler {@link TouchHandler} that handles touches.
	 */
	public void addTouchHandler(TouchHandler handler) {
		touchHandlers.add(handler);
	}
	
	//Pipelines handle
	/**
	 * <p>Adds {@link VisibleGameObject} to the rendering pipeline.</p>
	 * @param object Object to add.
	 */
	public void addVisibleGameObject(VisibleGameObject object) {
		objectsRenderingPipelineBuffer.add(object);
		
		addGameObject(object);
	}
	
	/**
	 * <p>Adds {@link GameObject} to the game objects pipeline.</p>
	 * @param object Object to add.
	 */
	public void addGameObject(GameObject object) {
		object.setEngine(this);
		
		gameObjectsPipelineBuffer.add(object);
	}
	
	/**
	 * <p>Adds an {@link Animation} to the animations pipeline.</p>
	 * @param animation Animation to add.
	 */
	public void addAnimation(Animation animation) {
		animation.setEngine(this);
		
		animationsPipelineBuffer.add(animation);
	}
	
	
	/**
	 * <p>Adds a {@link Background} to the backgrounds pipeline.</p>
	 * @param background Background to add
	 */
	public void addBackground(Background background) {
		backgroundsRenderingPipelineBuffer.add(background);
	}
	
	/**
	 * <p>Removes a {@link VisibleGameObject} from the rendering pipeline.</p>
	 * @param object Object to remove.
	 */
	public void removeVisibleGameObject(VisibleGameObject object) {
		objectsRenderingPipelineBuffer.remove(object);
		
		removeGameObject(object);
	}
	
	/**
	 * <p>Removes a {@link GameObject} from the game objects pipeline.</p>
	 * @param object Object to remove.
	 */
	public void removeGameObject(GameObject object) {
		gameObjectsPipelineBuffer.remove(object);
	}
	
	/**
	 * <p>Removes a {@link Animation} from the animations pipeline.</p>
	 * @param animation
	 */
	public void removeAnimation(Animation animation) {
		animationsPipelineBuffer.remove(animation);
	}
	
	
	/**
	 * <p>Removes a {@link Background} from the backgrounds pipeline.</p>
	 * @param background
	 */
	public void removeBackground(Background background) {
		backgroundsRenderingPipelineBuffer.remove(background);
	}
	
	
	/**
	 * @return Returns the {@link GameCameraObject}, representing a game camera.
	 */
	public GameCameraObject getCameraObject() {
		return cameraObject;
	}
	
	/**
	 * <p>Method, called from the drawing thread to redraw the screen.</p>
	 * @param canvas {@link Canvas} to draw on.
	 */
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
