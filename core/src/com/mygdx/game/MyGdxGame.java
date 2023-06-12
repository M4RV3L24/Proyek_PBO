package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game implements InputProcessor {



	public static final float WORLD_WIDTH = 1000;
	public static final float WORLD_HEIGHT = 600;

	AssetManager manager = new AssetManager();
	SpriteBatch batch;
	Texture img;
	Player1 p1;
	Player2 p2;
	Sprite obj;
	OrthographicCamera camera;
	Viewport viewport;
	BitmapFont font;
	BitmapFontCache fontcache1, fontcache2;


	@Override
	public void create () {
		batch = new SpriteBatch();
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		fontParameter.fontFileName = "04b_25__.ttf";
		fontParameter.fontParameters.size = 24;
		fontParameter.fontParameters.color = Color.WHITE;
		fontParameter.fontParameters.borderColor = Color.BLACK;
		fontParameter.fontParameters.borderWidth = 2;
		fontParameter.fontParameters.flip = false;
		manager.load("04b_25__.ttf", BitmapFont.class, fontParameter);

//		FreetypeFontLoader.FreeTypeFontLoaderParameter bigfontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
//		bigfontParameter.fontFileName = "04b_25__.ttf";
//		bigfontParameter.fontParameters.size = 48;
//		bigfontParameter.fontParameters.color = Color.WHITE;
//		bigfontParameter.fontParameters.borderColor = Color.BLACK;
//		bigfontParameter.fontParameters.borderWidth = 2;
//		bigfontParameter.fontParameters.flip = false;
//		manager.load("04b_25__.ttf", BitmapFont.class, bigfontParameter);



//		asset buat player 1
		manager.load("Sprite1/Attack1.png", Texture.class);
		manager.load("Sprite1/Death.png", Texture.class);
		manager.load("Sprite1/Take Hit - white silhouette.png", Texture.class);
		manager.load("Sprite1/Idle.png", Texture.class);
		manager.load("Sprite1/Run.png", Texture.class);
		manager.load("Sprite1/Jump.png", Texture.class);
		manager.load("Lost City Cover.jpeg", Texture.class);
		manager.load("Sprite1/Fall.png", Texture.class);

//		asset buat player 2
		manager.load("Sprite2/Attack1.png", Texture.class);
		manager.load("Sprite2/Death.png", Texture.class);
		manager.load("Sprite2/Take Hit.png", Texture.class);
		manager.load("Sprite2/Idle.png", Texture.class);
		manager.load("Sprite2/Run.png", Texture.class);
		manager.load("Sprite2/Jump.png", Texture.class);
		manager.load("Sprite2/Fall.png", Texture.class);

		manager.finishLoading();
		font = manager.get("04b_25__.ttf");
		fontcache1 = new BitmapFontCache(font);
		fontcache2 = new BitmapFontCache(font);


		fontcache1.setText("Player 1 HP: 100", 120, 500);
		fontcache2.setText("Player 2 HP: 100", 650, 500);

		img = manager.get("Lost City Cover.jpeg");
		obj = new Sprite(img);
		obj.setSize(WORLD_WIDTH, WORLD_HEIGHT);
		obj.setPosition(0, 0);

		camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);

//		obj.flip(false, true);
//		Texture img = manager.get("Idle.png");
//		obj = new Sprite(img);
//		obj.flip(false, true);
//		obj.setPosition(200,200);



		p1 = new Player1();
		p1.setX(200);
		p1.setY(140);


		p2 = new Player2();
		p2.setX(1000-200);
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
		fontcache1.draw(batch);
		fontcache2.draw(batch);

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
		if (p1.canHit(p2)) {
			p2.setHP(p2.getHP()-p1.getDmg());
			fontcache2.setText(String.format("Player 2 HP: %.2f",p2.getHP()), 650, 400);
			p2.act = Player2.Action.HIITED;
		}
		else if (p2.canHit(p1)) {
			p1.setHP(p1.getHP()-p2.getDmg());
			fontcache1.setText(String.format("Player 1 HP: %.2f",p1.getHP()), 120, 400);
			p1.act = Player1.Action.HIITED;
		}
	}


	@Override
	public boolean keyDown(int keycode) {
		//Player 1 Control

		if (keycode == Input.Keys.W) {
			p1.Jump(Player1.State.JUMP);
		}
		if (keycode == Input.Keys.D){
			p1.setMove(Player1.Direction.RIGHT);
		}
		if (keycode == Input.Keys.A) {
			p1.setMove(Player1.Direction.LEFT);
		}
		if (keycode == Input.Keys.CONTROL_LEFT) {
			p1.Attack(Player1.Action.ATTACK);
		}
		if (keycode == Input.Keys.S){
			p1.Jump(Player1.State.FALL);
		}


		//Player 2 Control
		if (keycode == Input.Keys.UP) {
			p2.Jump(Player2.State.JUMP);
		}
		if (keycode == Input.Keys.RIGHT){
			p2.setMove(Player2.Direction.RIGHT);
		}
		if (keycode == Input.Keys.LEFT) {
			p2.setMove(Player2.Direction.LEFT);
		}
		if (keycode == Input.Keys.CONTROL_RIGHT) {
			p2.Attack(Player2.Action.ATTACK);
		}
		if (keycode == Input.Keys.DOWN) {
			p2.Jump(Player2.State.FALL);
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.A && p1.getDirection() == Player1.Direction.LEFT)
			p1.Stop();
		if(keycode == Input.Keys.D && p1.getDirection() == Player1.Direction.RIGHT)
			p1.Stop();
		if(keycode == Input.Keys.W && p1.getState() == Player1.State.JUMP)
			p1.Jump(Player1.State.FALL);
		p1.Attack(Player1.Action.NO_ATTACK);


		if(keycode == Input.Keys.LEFT && p2.getDirection() == Player2.Direction.LEFT)
			p2.Stop();
		else if(keycode == Input.Keys.RIGHT && p2.getDirection() == Player2.Direction.RIGHT)
			p2.Stop();
		else if(keycode == Input.Keys.UP && p2.getState() == Player2.State.JUMP)
			p2.Jump(Player2.State.FALL);
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
