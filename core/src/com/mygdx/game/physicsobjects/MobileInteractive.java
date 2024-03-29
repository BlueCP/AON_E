package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public abstract class MobileInteractive extends MobileObject {

	public MobileInteractive(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags) {
		super(collisionObject, texture, id, tags);

		physicsId = Util.getMobileInteractiveId(this.id);
		collisionObject.setUserValue(physicsId);
	}

//	public abstract void clicked(PlayScreen.MouseEvent mouseEvent, PlayScreen playScreen);

}
