package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game implements InputProcessor {



	public static final float WORLD_WIDTH = 1000;
	public static final float WORLD_HEIGHT = 700;

	AssetManager manager = new AssetManager();
	SpriteBatch batch;
	Texture img;
	Player p1;
	Player2 p2;
	Sprite obj;
	OrthographicCamera camera;
	Viewport viewport;
	
	@Override
	public void create () {


		batch = new SpriteBatch();

//		asset buat player 1
		manager.load("Sprite1/Attack1.png", Texture.class);
		manager.load("Sprite1/Death.png", Texture.class);
		manager.load("Sprite1/Take Hit - white silhouette.png", Texture.class);
		manager.load("Sprite1/Idle.png", Texture.class);
		manager.load("Sprite1/Run.png", Texture.class);
		manager.load("Sprite1/Jump.png", Texture.class);
		manager.load("Lost City Cover.jpeg", Texture.class);

//		asset buat player 2
		manager.load("Sprite2/Attack1.png", Texture.class);
		manager.load("Sprite2/Death.png", Texture.class);
		manager.load("Sprite2/Take Hit.png", Texture.class);
		manager.load("Sprite2/Idle.png", Texture.class);
		manager.load("Sprite2/Run.png", Texture.class);
		manager.load("Sprite2/Jump.png", Texture.class);

		manager.finishLoading();
		img = manager.get("Lost City Cover.jpeg");
		obj = new Sprite(img);
		obj.setSize(WORLD_WIDTH, WORLD_HEIGHT);
		obj.setPosition(0, 0);
		float aspect_ratio = (float )Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
		camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);

//		obj.flip(false, true);


//		Texture img = manager.get("Idle.png");
//		obj = new Sprite(img);
//		obj.flip(false, true);
//		obj.setPosition(200,200);



		p1 = new Player();
		p1.setX(100);
		p1.setY(140);


		p2 = new Player2();
		p2.setX(1000-100);
		p2.setY(140);
		Gdx.input.setInputProcessor(this);



	}

	@Override
	public void resize (int width, int height) {
		// viewport must be updated for it to work properly
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		obj.draw(batch);
		p2.draw(batch);
		p1.draw(batch);

		batch.end();
		this.update();
	}

	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
		img.dispose();

	}


	public void update () {
		p1.update();
		p2.update();
	}


	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.UP) {
			p1.setMove(Player.Direction.JUMP);
		}
		if (keycode == Input.Keys.RIGHT){
			p1.setMove(Player.Direction.RIGHT);
		}
		if (keycode == Input.Keys.LEFT) {
			p1.setMove(Player.Direction.LEFT);
		}
		if (keycode == Input.Keys.CONTROL_RIGHT) {
			p1.Attack(Player.Action.ATTACK);
		}

		if (keycode == Input.Keys.UP) {
			p2.setMove(Player2.Direction.JUMP);
		}
		if (keycode == Input.Keys.D){
			p2.setMove(Player2.Direction.RIGHT);
		}
		if (keycode == Input.Keys.A) {
			p2.setMove(Player2.Direction.LEFT);
		}
		if (keycode == Input.Keys.CONTROL_LEFT) {
			p2.Attack(Player2.Action.ATTACK);
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.LEFT && p1.getDirection() == Player.Direction.LEFT)
			p1.Stop();
		else if(keycode == Input.Keys.RIGHT && p1.getDirection() == Player.Direction.RIGHT)
			p1.Stop();
		else if(keycode == Input.Keys.UP && p1.getDirection() == Player.Direction.JUMP)
			p1.Stop();
		p1.Attack(Player.Action.NO_ATTACK);



		if(keycode == Input.Keys.A && p2.getDirection() == Player2.Direction.LEFT)
			p2.Stop();
		else if(keycode == Input.Keys.D && p2.getDirection() == Player2.Direction.RIGHT)
			p2.Stop();
		else if(keycode == Input.Keys.W && p2.getDirection() == Player2.Direction.JUMP)
			p2.Stop();
		p2.Attack(Player2.Action.NO_ATTACK);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	public static TextureRegion[] CreateAnimationFrames(Texture tex, int frameWidth, int frameHeight, int frameCount, boolean flipx, boolean flipy)
	{
		//akan membuat frame animasi dari texture, texture dipotong2 sebesar frameWidth x frameHeight
		// frame akan diambil dari posisi kiri atas ke kanan bawah
		TextureRegion[][] tmp = TextureRegion.split(tex, frameWidth, frameHeight);
		TextureRegion[] frames = new TextureRegion[frameCount];
		int index = 0;
		int row = tex.getHeight() / frameHeight;
		int col = tex.getWidth() / frameWidth;
		for (int i = 0; i < row && index < frameCount; i++) {
			for (int j = 0; j < col && index < frameCount; j++) {
				frames[index] = tmp[i][j];
				frames[index].flip(flipx, flipy);
				index++;
			}
		}
		return frames;
	}

	public AssetManager getManager() {
		return manager;
	}
}
