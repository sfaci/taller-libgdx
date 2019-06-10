package com.sfaci.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Pantalla de inicio
 * Se presenta el menú de game
 *
 * @author Santiago Faci
 * @version Taller Desarrollo de Videojuegos Junio 2019
 */
public class MainMenuScreen implements Screen {

    SpriteBatch spriteBatch;
    BitmapFont font;
    OrthographicCamera camera;

    public MainMenuScreen() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        // Muestra un menú de inicio
        spriteBatch.begin();
        font.draw(spriteBatch, "Bienvenido a Drop!!!!", 100, 150);
        font.draw(spriteBatch, "Pulsa para empezar", 100, 120);
        spriteBatch.end();

		/*
		 * Si el usuario toca la pantalla se inicia la partida
		 */
        if (Gdx.input.isTouched()) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }
}
