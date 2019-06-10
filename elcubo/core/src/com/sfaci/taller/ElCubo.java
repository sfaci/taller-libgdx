package com.sfaci.taller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ElCubo extends ApplicationAdapter {
	SpriteBatch batch;
	Texture ship;
	Array<Rectangle> ufos;
	int xShip, yShip;
	int xUfo, yUfo;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ship = new Texture("ship.png");
		ufo = new Texture("ufo.png");

		xShip = 0;
		yShip = 0;

		xUfo = MathUtils.random(0, Gdx.graphics.getWidth());
		yUfo = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(ship, xShip, yShip);
		batch.draw(ufo, xUfo, yUfo);
		batch.end();

		yUfo -= 5;
		if (xUfo < xShip) {
		    xUfo += 4;
        }
        if (xUfo > xShip) {
		    xUfo -= 4;
        }

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xShip += 5;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
		    xShip -= 5;
        }
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		ship.dispose();
	}
}
