package com.mygdx.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AON_E;
import com.mygdx.game.entities.Player;
import com.mygdx.game.screens.PlayScreen;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.settings.VideoSettings;

public class Camera {

	public OrthographicCamera orthographicCamera;
	public Viewport viewport;

	public Vector3 pos;
	Vector3 screenShakeDisplacement;
	public Vector2 isoPos;
	//private int followedEntity;

	private ScreenShakeAction screenShakeAction; // Screen shakes get their own place because they are different to all other actions; they would normally not wait in a queue.
	private Queue<Array<LimitedCameraAction>> limitedActions;
	private Array<UnlimitedCameraAction> unlimitedActions;
	
	Camera() {
		orthographicCamera = new OrthographicCamera();
		viewport = new FitViewport(AON_E.WORLD_WIDTH, AON_E.WORLD_HEIGHT, orthographicCamera);
//		orthographicCamera.position.set(orthographicCamera.viewportWidth/2, orthographicCamera.viewportHeight/2, 0);
		orthographicCamera.position.set(0, 0, 0);
		orthographicCamera.update();

		screenShakeAction = new ScreenShakeAction();
		limitedActions = new Queue<>();
		unlimitedActions = new Array<>();
		
		pos = new Vector3();
		screenShakeDisplacement = new Vector3();
	}
	
	public void update(Player player, PlayScreen playScreen) {
		pos.sub(screenShakeDisplacement);
		screenShakeDisplacement.setZero();

		if (limitedActions.size == 0 && unlimitedActions.size == 0) {
			pos = player.pos.cpy();
		}
		/*
		else if (followedEntityExists(entities)) {
			if (followedEntity == 0) {
				pos = player.pos.cpy();
			} else {
				pos = entities.allEntities.get(followedEntity).pos.cpy();
			}
		}
		*/

		updateScreenShakeAction();

		updateUnlimitedCameraActions(playScreen);
		
		updateLimitedCameraActions();

		pos.add(screenShakeDisplacement);
		
		/*
		if (limitedActions.size == 0 && unlimitedActions.size == 0) {
			zoom = 1;
		}
		*/
	}

	private void updateScreenShakeAction() {
		if (screenShakeAction.lifetime > 0 && VideoSettings.isScreenShakeEnabled()) {
			screenShakeAction.lifetime -= Gdx.graphics.getDeltaTime();
			screenShakeAction.update(this);
		}
	}

	private void updateUnlimitedCameraActions(PlayScreen playScreen) {
		for (int i = 0; i < unlimitedActions.size; i ++) {
			UnlimitedCameraAction action = unlimitedActions.get(i);
			action.update(playScreen);
			if (action.toBeDestroyed) {
				unlimitedActions.removeIndex(i);
				i --;
			}
		}
	}

	private void updateLimitedCameraActions() {
		if (limitedActions.size > 0) {
			for (int i = 0; i < limitedActions.first().size; i ++) {
				LimitedCameraAction action = limitedActions.first().get(i);
				action.update(this);
				action.lifetime -= Gdx.graphics.getDeltaTime();
				if (action.lifetime <= 0) {
					limitedActions.first().removeIndex(i);
					if (limitedActions.first().size == 0) {
						limitedActions.removeIndex(0);
						if (limitedActions.size == 0) {
							break;
						}
					}
					i --;
				}
			}
		}
	}
	
	/*
	private boolean followedEntityExists(Entities entities) {
		if (followedEntity == 0) {
			return true;
		}
		for (Entity entity: entities.allEntities) {
			if (entity.getId() == followedEntity) {
				return true;
			}
		}
		// If no entity with that id has been found
		followedEntity = -1;
		return false;
	}
	*/
	
	private void testForEmptyQueue() {
		if (limitedActions.size == 0) {
			limitedActions.addFirst(new Array<>());
		}
	}

	public void screenShake(float lifetime, float magnitude) {
		screenShakeAction.build(lifetime, magnitude);
	}
	
	public void addCutNow(float lifetime, Vector3 pos) {
		testForEmptyQueue();
		limitedActions.first().add(new CutAction(lifetime, pos));
	}
	
	public void addCutToQueue(float lifetime, Vector3 pos) {
		Array<LimitedCameraAction> array = new Array<>();
		array.add(new CutAction(lifetime, pos));
		limitedActions.addLast(array);
	}
	
	public void addHardPanNow(float lifetime, Vector3 pos) {
		testForEmptyQueue();
		limitedActions.first().add(new HardPanAction(lifetime, this.pos, pos));
	}
	
	public void addHardPanToQueue(float lifetime, Vector3 pos) {
		Array<LimitedCameraAction> array = new Array<>();
		array.add(new HardPanAction(lifetime, this.pos, pos));
		limitedActions.addLast(array);
	}
	
	public void addSoftPanNow(float lifetime, Vector3 pos) {
		testForEmptyQueue();
		limitedActions.first().add(new SoftPanAction(lifetime, this.pos, pos));
	}
	
	public void addSoftPanToQueue(float lifetime, Vector3 pos) {
		Array<LimitedCameraAction> array = new Array<>();
		array.add(new SoftPanAction(lifetime, this.pos, pos));
		limitedActions.addLast(array);
	}
	
	public void addSoftZoomNow(float lifetime, float targetZoom) {
		testForEmptyQueue();
		limitedActions.first().add(new SoftZoomAction(lifetime, orthographicCamera.zoom, targetZoom));
	}
	
	public void addSoftZoomToQueue(float lifetime, float targetZoom) {
		Array<LimitedCameraAction> array = new Array<>();
		array.add(new SoftZoomAction(lifetime, orthographicCamera.zoom, targetZoom));
		limitedActions.addLast(array);
	}
	
	public void addHardZoomNow(float lifetime, float targetZoom) {
		testForEmptyQueue();
		limitedActions.first().add(new HardZoomAction(lifetime, orthographicCamera.zoom, targetZoom));
	}
	
	public void addHardZoomToQueue(float lifetime, float targetZoom) {
		Array<LimitedCameraAction> array = new Array<>();
		array.add(new HardZoomAction(lifetime, orthographicCamera.zoom, targetZoom));
		limitedActions.addLast(array);
	}
	
	public void addInstantZoom(float zoom) {
		unlimitedActions.add(new InstantZoomAction(zoom));
	}
	
	float getZoom() {
		return orthographicCamera.zoom;
	}
	
	void setZoom(float zoom) {
		orthographicCamera.zoom = zoom;
	}
	
}
