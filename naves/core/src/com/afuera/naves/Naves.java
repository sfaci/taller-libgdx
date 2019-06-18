package com.afuera.naves;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Naves extends ApplicationAdapter {
	SpriteBatch batch;
	Texture imgNave, imgMisil, imgMarciano;
	Array<Rectangle> marcianos;
	Array<Rectangle> misiles;
	Rectangle nave;
	long tiempoEntreMarcianos, ultimoMarciano;
	boolean pausa;
	BitmapFont fuente;
	int puntos, vidas;
	boolean fin;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgNave = new Texture("ship.png");
		imgMisil = new Texture("missile_score.png");
		imgMarciano = new Texture("ufo.png");
		fuente = new BitmapFont();

		marcianos = new Array<Rectangle>();
		misiles = new Array<Rectangle>();
		nave = new Rectangle();
		nave.x = 0;
		nave.y = 0;
		nave.width = imgNave.getWidth();
		nave.height = imgNave.getHeight();

		tiempoEntreMarcianos = 200;
		ultimoMarciano = 0;
		pausa = false;
		vidas = 3;
		puntos = 0;
		fin = false;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (fin) {
		    batch.begin();
		    fuente.getData().scale(2);
		    fuente.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2);
            fuente.getData().scale(-2);
            fuente.draw(batch, "Pulsa ENTER para volver a jugar",
                    Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 60);
		    batch.end();

		    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
		        fin = false;
		        marcianos.clear();
		        misiles.clear();
		        vidas = 3;
		        puntos = 0;
            }
		    return;
        }

		comprobarTeclado();
		if (!pausa) {
            comprobarBordes();
            moverMisiles();
            generarMarcianos();
            moverMarcianos();
        }

		pintarPantalla();
	}

	private void generarMarcianos() {
	    if (TimeUtils.millis() - ultimoMarciano > tiempoEntreMarcianos) {
            Rectangle marciano = new Rectangle();
            marciano.width = imgMarciano.getWidth();
            marciano.height = imgMarciano.getHeight();
            marciano.y = Gdx.graphics.getHeight();
            marciano.x = MathUtils.random(Gdx.graphics.getWidth() - marciano.width);
            marcianos.add(marciano);
            ultimoMarciano = TimeUtils.millis();
        }
    }

	private void moverMarcianos() {
	    for (Rectangle marciano : marcianos) {
	        marciano.y -= 4;

	        if (marciano.y < 0 - marciano.height) {
	            marcianos.removeValue(marciano, true);
            }

            if (marciano.overlaps(nave)) {
	            marcianos.removeValue(marciano, true);
	            Sound sonido = Gdx.audio.newSound(new FileHandle("explosion.mp3"));
	            sonido.play();
	            vidas--;
	            if (vidas == 0) {
                    fin = true;
                }
            }
        }
    }

	private void pintarPantalla() {
        batch.begin();
        batch.draw(imgNave, nave.x, nave.y);
        for (Rectangle misil : misiles)
            batch.draw(imgMisil, misil.x, misil.y);
        for (Rectangle marciano : marcianos)
            batch.draw(imgMarciano, marciano.x, marciano.y);

        fuente.draw(batch, "Vidas: " + vidas, 20, Gdx.graphics.getHeight() - 30);
        fuente.draw(batch, "Puntos: " + puntos, Gdx.graphics.getWidth() - 100,
                Gdx.graphics.getHeight() - 30);
        batch.end();
    }

	private void moverMisiles() {
        for (Rectangle misil : misiles) {
            misil.y += 10;

            for (Rectangle marciano : marcianos) {
                if (misil.overlaps(marciano)) {
                    misiles.removeValue(misil, true);
                    marcianos.removeValue(marciano, true);
                    Sound sonido = Gdx.audio.newSound(new FileHandle("explosion.mp3"));
                    sonido.play();
                    puntos += 10;
                }
            }
        }
    }

	private void comprobarBordes() {
        if (nave.x < 0)
            nave.x = 0;

        if (nave.x > Gdx.graphics.getWidth() - nave.width)
            nave.x = Gdx.graphics.getWidth() - nave.width;

        if (nave.y < 0)
            nave.y = 0;

        if (nave.y > Gdx.graphics.getHeight() - nave.height)
            nave.y = Gdx.graphics.getHeight() - nave.height;
    }

	private void comprobarTeclado() {
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            nave.x = MathUtils.random(Gdx.graphics.getWidth() - nave.width);
            nave.y = MathUtils.random(Gdx.graphics.getHeight() - nave.height);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            pausa = !pausa;
    }
	
	@Override
	public void dispose () {
		batch.dispose();
		imgNave.dispose();
	}
}
