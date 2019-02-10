package com.mygdx.game.entityattributes;

//import com.mygdx.game.tiles.Tile;
//import com.mygdx.game.tiles.UnknownTile;
//import com.mygdx.game.world.World;

public class FieldOfView {

	private static final long serialVersionUID = 8423708046333921801L;

//	private World world;
	private int depth;
	
	private boolean[][] visible;
	public boolean isVisible(int x, int y, int z){
		return z == depth && x >= 0 && y >= 0 && x < visible.length && y < visible[0].length && visible[x][y];
	}
	
//	private Tile[][][] tiles;
	/*public Tile tile(int x, int y, int z){
		*//*if (x < 0 || x > world.worldSize() - 1 || y < 0 || y > world.worldSize() - 1 || z < 0 || z > World.depth - 1) {
			return new Tile("bounds");
		} else {
			return tiles[x][y][z];
		}*//*
		return null;
	}*/
	
	public FieldOfView() {
		// Constructor is used only for serialisation.
	}
	
	/*public FieldOfView(World world){
		this.world = world;
		this.visible = new boolean[world.worldSize()][world.worldSize()];
		this.tiles = new Tile[world.worldSize()][world.worldSize()][World.depth];
		
		for (int x = 0; x < world.worldSize(); x++){
			for (int y = 0; y < world.worldSize(); y++){
				for (int z = 0; z < World.depth; z++){
					tiles[x][y][z] = new Tile();
					tiles[x][y][z].setTileType(new UnknownTile());
				}
			}
		}
	}*/
	
	/*public void update(World world, float wx, float wy, float wz, int r){
		this.world = world;
		depth = wz;
		visible = new boolean[world.worldSize()][world.worldSize()];
		
		for (int x = -r; x < r; x++){
			for (int y = -r; y < r; y++){
				if (x*x + y*y > r*r)
					continue;
				
				if (wx + x < 0 || wx + x >= world.worldSize() || wy + y < 0 || wy + y >= world.worldSize())
					continue;
				
				for (Point2 p : new Line(wx, wy, wx + x, wy + y)){
					Tile tile = world.tile(p.x, p.y, wz);
					visible[p.x][p.y] = true;
					tiles[p.x][p.y][wz] = tile; 
					
					if (tile.isOpaque())
						break;
				}
			}
		}
	}*/
}