package com.mygdx.game.entityattributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Point2;

public class PathFinder {
	   private ArrayList<Point2> open;
	   private ArrayList<Point2> closed;
	   private HashMap<Point2, Point2> parents;
	   private HashMap<Point2,Float> totalCost;
	 
	   public PathFinder() {
			 this.open = new ArrayList<Point2>();
			 this.closed = new ArrayList<Point2>();
			 this.parents = new HashMap<Point2, Point2>();
			 this.totalCost = new HashMap<Point2, Float>();
	   }
	 
	   private float heuristicCost(Point2 from, Point2 to) {
			 return Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y));
	   }

	   private int costToGetTo(Point2 from) {
			 return parents.get(from) == null ? 0 : (1 + costToGetTo(parents.get(from)));
	   }
	 
	   private float totalCost(Point2 from, Point2 to) {
			 if (totalCost.containsKey(from))
				 return totalCost.get(from);
		   
			 float cost = costToGetTo(from) + heuristicCost(from, to);
			 totalCost.put(from, cost);
			 return cost;
	   }

	   private void reParent(Point2 child, Point2 parent){
			 parents.put(child, parent);
			 totalCost.remove(child);
	   }

	   public ArrayList<Point2> findPath(PlayScreen session, Entity entity, Point2 start, Point2 end, int maxTries) {
			 /*open.clear();
			 closed.clear();
			 parents.clear();
			 totalCost.clear();
	   
			 open.add(start);
		   
			 for (int tries = 0; tries < maxTries && open.size() > 0; tries++){
				   Point2 closest = getClosestPoint(end);
				 
				   open.remove(closest);
				   closed.add(closest);

				   if (closest.equals(end))
						 return createPath(start, closest);
				   else
						 checkNeighbors(session, entity, end, closest);
			 }*/
			 return null;
	   }

		private Point2 getClosestPoint(Point2 end) {
			Point2 closest = open.get(0);
			for (Point2 other : open){
				if (totalCost(other, end) < totalCost(closest, end))
					closest = other;
			}
			return closest;
		}

		/*private void checkNeighbors(PlayScreen session, Entity entity, Point2 end, Point2 closest) {
			for (Point2 neighbor : closest.neighbors8()) {
				if (closed.contains(neighbor)
				 || !entity.canEnter(session, neighbor.x, neighbor.y, entity.getStatus().getZPos())
				 && !neighbor.equals(end))
					 continue;
	
				if (open.contains(neighbor))
					reParentNeighborIfNecessary(closest, neighbor);
				else
					reParentNeighbor(closest, neighbor);
			}
		}*/

		private void reParentNeighbor(Point2 closest, Point2 neighbor) {
			reParent(neighbor, closest);
			open.add(neighbor);
		}

		private void reParentNeighborIfNecessary(Point2 closest, Point2 neighbor) {
			Point2 originalParent = parents.get(neighbor);
			double currentCost = costToGetTo(neighbor);
			reParent(neighbor, closest);
			double reparentCost = costToGetTo(neighbor);
  
			if (reparentCost < currentCost)
				open.remove(neighbor);
			else
				reParent(neighbor, originalParent);
		}

		private ArrayList<Point2> createPath(Point2 start, Point2 end) {
			ArrayList<Point2> path = new ArrayList<Point2>();

			while (!end.equals(start)) {
				path.add(end);
				end = parents.get(end);
			}

			Collections.reverse(path);
			return path;
		}
	}