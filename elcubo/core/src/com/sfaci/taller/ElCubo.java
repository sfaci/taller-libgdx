package com.sfaci.taller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ElCubo extends ApplicationAdapter {
	SpriteBatch batch;
	Rectangle ship;
	Array<Rectangle> ufos;
	Array<Rectangle> misiles;
	Texture shipTexture;
	Texture ufoTexture;
	Texture misilTexture;
	final long TIEMPO_ENTRE_MARCIANOS = 500;
	long ultimo_marciano = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shipTexture = new Texture("ship.png");
		ufoTexture = new Texture("ufo.png");
		misilTexture = new Texture("missile_score.png");
		ufos = new Array<Rectangle>();
		misiles = new Array<Rectangle>();

		ship = new Rectangle();
		ship.x = 0;
		ship.y = 0;
		ship.width = 29;
		ship.height = 64;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Creo marcianos
        if (TimeUtils.millis() - ultimo_marciano > TIEMPO_ENTRE_MARCIANOS) {
            Rectangle ufo = new Rectangle();
            ufo.x = MathUtils.random(0, Gdx.graphics.getWidth() - 40);
            ufo.y = Gdx.graphics.getHeight() - 40;
            ufo.width = 40;
            ufo.height = 40;
            ufos.add(ufo);

            ultimo_marciano = TimeUtils.millis();
        }

		// Los marcianos caen
		for (Rectangle ufoo : ufos)
		    ufoo.y -= 4;

        // Mover misiles
        for (Rectangle misil : misiles)
            misil.y += 8;

		// Pintamos personaje y marcianos
		batch.begin();
		batch.draw(shipTexture, ship.x, ship.y);
		for (Rectangle ufoo : ufos)
		    batch.draw(ufoTexture, ufoo.x, ufoo.y);
		for (Rectangle misil : misiles)
		    batch.draw(misilTexture, misil.x, misil.y);
		batch.end();

		// Comprobar colisiones
        for (Rectangle ufoo : ufos) {
            if (ufoo.overlaps(ship)) {
                ufos.removeValue(ufoo, true);
                Sound sonidoExplosion = Gdx.audio.newSound(
                        new FileHandle("explosion.mp3"));
                sonidoExplosion.play();
            }

            if (ufoo.y < 0) {
                ufos.removeValue(ufoo, true);
            }

            for (Rectangle misil : misiles) {
                if (ufoo.overlaps(misil)) {
                    ufos.removeValue(ufoo, true);
                    misiles.removeValue(misil, true);
                    Sound sonidoExplosion = Gdx.audio.newSound(
                            new FileHandle("explosion.mp3"));
                    sonidoExplosion.play();
                }
            }
        }

		// Controlo la nave
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            ship.x += 5;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
		    ship.x -= 5;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Rectangle misil = new Rectangle();
            misil.width = 8;
            misil.height = 20;
            misil.y = ship.y + ship.height;
            misil.x = ship.x + ship.width / 2 - misil.width / 2;
            misiles.add(misil);
            Sound sonidoDisparo = Gdx.audio.newSound(
                    new FileHandle("disparo.mp3"));
            sonidoDisparo.play();
        }

        // Limites de la pantalla
        if (ship.x < 0)
            ship.x = 0;
        if (ship.x > Gdx.graphics.getWidth() - 29)
            ship.x = Gdx.graphics.getWidth() - 29;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shipTexture.dispose();
		ufoTexture.dispose();
	}
}
