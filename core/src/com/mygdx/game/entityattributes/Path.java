package com.mygdx.game.entityattributes;

import java.util.List;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Point2;

public class Path {

  private static PathFinder pf = new PathFinder();

  private List<Point2> points;
  public List<Point2> points() { return points; }

  public Path(PlayScreen session, Entity entity, int x, int y){
//	  points = pf.findPath(session,
//			  			   entity,
//						   new Point2(entity.getStatus().getXPos(), entity.getStatus().getYPos(), entity.getStatus().getZPos()),
//						   new Point2(x, y, entity.getStatus().getZPos()),
//						   300);
  }
}