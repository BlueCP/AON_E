package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Util;

public abstract class ImmobileInteractive extends ImmobileObject {

	public ImmobileInteractive(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags) {
		super(collisionObject, texture, id, tags);

		physicsId = Util.getImmobileInteractiveId(this.id);
		collisionObject.setUserValue(physicsId);
	}

//	public abstract void clicked(PlayScreen.MouseEvent mouseEvent, PlayScreen playScreen);

}
