package com.afuera.naves;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Naves extends ApplicationAdapter {
	SpriteBatch batch;
	Texture imgNave, imgMisil;
	Array<Rectangle> marcianos;
	Array<Rectangle> misiles;
	Rectangle nave;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgNave = new Texture("ship.png");
		imgMisil = new Texture("missile_score.png");

		marcianos = new Array<Rectangle>();
		misiles = new Array<Rectangle>();
		nave = new Rectangle();
		nave.x = 0;
		nave.y = 0;
		nave.width = imgNave.getWidth();
		nave.height = imgNave.getHeight();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
		    nave.x += 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
		    nave.x -= 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
		    nave.y += 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
		    nave.y -= 10;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
		    Rectangle misil = new Rectangle();
		    misil.width = imgMisil.getWidth();
		    misil.height = imgMisil.getHeight();
		    misil.x = nave.x + nave.width / 2 - misil.width / 2;
		    misil.y = nave.y + nave.height;
		    misiles.add(misil);
		    Sound sonido = Gdx.audio.newSound(new FileHandle("disparo.mp3"));
		    sonido.play();
        }

        for (Rectangle misil : misiles) {
		    misil.y += 10;
        }

		batch.begin();
		batch.draw(imgNave, nave.x, nave.y);
		for (Rectangle misil : misiles) {
		    batch.draw(imgMisil, misil.x, misil.y);
        }
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgNave.dispose();
	}
}
