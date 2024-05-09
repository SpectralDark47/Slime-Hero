package com.mygdx.slimehero;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Logger;

import java.util.Iterator;

public class SlimeHero extends ApplicationAdapter {
	private Texture heroImage;
	private Texture enemyImage;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle hero;
	private Rectangle enemy;
	private Array<Rectangle> enemies;
	private Texture backgroundTexture;
	private long enemySpawnTime;
	@Override
	public void create () {
		// load the images for the hero and an enemy, 32x32 pixels each
		heroImage = new Texture(Gdx.files.internal("hero.png"));
		enemyImage = new Texture(Gdx.files.internal("enemy.png"));
		backgroundTexture = new Texture(Gdx.files.internal("Background_dirt.png"));

		// a Camera and a SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 360, 640);

		batch = new SpriteBatch();

		// adding the Hero
		hero = new Rectangle();
		hero.x = 360 / 2 - 32 / 2;
		hero.y = 16;
		hero.width = 32;
		hero.height = 32;


		//enemy array
		enemies = new Array<Rectangle>();
		spawnEnemy();
	}

	//spawn enemies at random x values
	private void spawnEnemy() {
		Rectangle enemy = new Rectangle();
		enemy.x = MathUtils.random(0, 360 - 32);
		enemy.y = 640;
		enemy.width = 32;
		enemy.height = 32;
		enemies.add(enemy);
		enemySpawnTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		// rendering the background, hero, and enemies
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(backgroundTexture,0,0,360,640);
		batch.draw(heroImage, hero.x, hero.y);
		for(Rectangle enemy: enemies){
			batch.draw(enemyImage, enemy.x, enemy.y);
		}
		batch.end();

		// making the Hero move (Touch/Mouse)
		if(Gdx.input.isTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			hero.x = touchPos.x - 64/2;
		}

		//clamping the hero and spawning enemies
		if(hero.x < 0) hero.x = 0;
		if(hero.x > 800 - 64) hero.x = 800-64;
		if(TimeUtils.nanoTime() - enemySpawnTime > 100000000) spawnEnemy();
		for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext(); ) {
			Rectangle enemy = iter.next();
			enemy.y -= 200 * Gdx.graphics.getDeltaTime();

			if(enemy.y + 64 < 0) iter.remove();
		}



	}

	@Override
	public void dispose () {
		enemyImage.dispose();
		heroImage.dispose();
		backgroundTexture.dispose();
		batch.dispose();
	}


}