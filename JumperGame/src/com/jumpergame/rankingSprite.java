package com.jumpergame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import com.jumpergame.Manager.ResourcesManager;

public class rankingSprite extends Sprite{
	
	private ArrayList<users> ranking;
	private VertexBufferObjectManager vbom;
	public rankingSprite(VertexBufferObjectManager pSpriteVertexBufferObject)
	{
		super(240,400, ResourcesManager.getInstance().ranking_region, pSpriteVertexBufferObject);
		setScale(1.0f, 1.2f);
		ranking=new ArrayList<users>();
		vbom=pSpriteVertexBufferObject;
	}
	
	
	/**
	 * Change star`s tile index, depends on stars count.
	 * @param starsCount
	 */
	public void display(Scene scene, Camera camera)
	{
		// Change stars tile index, based on stars count (1-3)
		ranking = new ArrayList<users>(ResourcesManager.getInstance().userList.values()); 
		System.out.println("hahaha"+ResourcesManager.getInstance().userList.size());
		System.out.println("honhonhon"+ranking.size());
		Collections.sort(ranking, new Comparator<users>() {

			@Override
			public int compare(users arg0, users arg1) {				
		            return arg1.getScore() - arg0.getScore();
			}
	    });
		int y_init=620;
		int x_rank=1;
		int x_pos_rank=40;
		int x_pos_name=100;
		int x_pos_score=200;
		for (users p : ranking) {
			System.out.println("can you see me><");
			float[] coordinates1 = {x_pos_rank, y_init};
			float[] localCoordinates1 = convertSceneCoordinatesToLocalCoordinates(coordinates1);
			Text rankText = new Text(localCoordinates1[0],localCoordinates1[1], ResourcesManager.getInstance().mScoreFont, String.valueOf(x_rank), 50, new TextOptions(HorizontalAlign.LEFT), vbom);
			attachChild(rankText);
			float[] coordinates2 = {x_pos_name, y_init};
			float[] localCoordinates2 = convertSceneCoordinatesToLocalCoordinates(coordinates2);
			Text nameText = new Text(localCoordinates2[0], localCoordinates2[1], ResourcesManager.getInstance().mScoreFont, p.getName(), 50, new TextOptions(HorizontalAlign.LEFT), vbom);
			attachChild(nameText);
			float[] coordinates3 = {x_pos_score, y_init};
			float[] localCoordinates3 = convertSceneCoordinatesToLocalCoordinates(coordinates3);
			Text scoreText = new Text(localCoordinates3[0], localCoordinates3[1], ResourcesManager.getInstance().mScoreFont, String.valueOf(p.getScore()), 50, new TextOptions(HorizontalAlign.LEFT), vbom);
			attachChild(scoreText);
			x_rank++;
			y_init-=50;
	    }
		
		// Hide HUD
		camera.getHUD().setVisible(false);
		
		// Disable camera chase entity
		camera.setChaseEntity(null);
		
		// Attach our level complete panel in the middle of camera
		setPosition(camera.getCenterX(), camera.getCenterY());
		scene.attachChild(this);
	}

}

