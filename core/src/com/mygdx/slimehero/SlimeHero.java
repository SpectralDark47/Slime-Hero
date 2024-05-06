package com.mygdx.slimehero;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class SlimeHero extends ApplicationAdapter {
	private Texture heroImage;
	private Texture enemyImage;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle hero;
	private Rectangle enemy;

	@Override
	public void create () {
		// load the images for the hero and an enemy, 32x32 pixels each
		heroImage = new Texture(Gdx.files.internal("Player_walk_cycle-1.png"));
		enemyImage = new Texture(Gdx.files.internal("Enemy_placeholder_1.png"));

		// a Camera and a SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 360, 640);

		batch = new SpriteBatch();

		// adding the Hero
		hero = new Rectangle();
		hero.x = 360 / 2 - 16 / 2;
		hero.y = 20;
		hero.width = 16;
		hero.height = 32;

		enemy = new Rectangle();
		enemy.x = 180 / 2 - 16 / 2;
		enemy.y = 20;
		enemy.width = 16;
		enemy.height = 32;
	}

	@Override
	public void render () {
		// rendering the Bucket
		ScreenUtils.clear(0,0,0.2f,1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(heroImage, hero.x, hero.y);
		batch.draw(enemyImage, enemy.x, enemy.y);
		batch.end();

		// making the Hero move (Touch/Mouse)
		if(Gdx.input.isTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			hero.x = touchPos.x - 64/2;
		}

		// making the Hero move (Keyboard)
		int moveAmount = 0;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveAmount -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveAmount += 200 * Gdx.graphics.getDeltaTime();
		//handle collision with enemy and finish movement
		float heroPrevX = hero.x;
		hero.x += moveAmount;
		if(hero.overlaps(enemy)) hero.x = heroPrevX;

		if(hero.x < 0) hero.x = 0;
		if(hero.x > 800 - 64) hero.x = 800-64;
	}

	@Override
	public void dispose () {
		enemyImage.dispose();
		heroImage.dispose();
		batch.dispose();
	}
}
