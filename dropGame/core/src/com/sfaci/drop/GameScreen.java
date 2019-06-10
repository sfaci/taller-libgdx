package com.sfaci.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Pantalla del juego, donde el usuario juega la partida
 *
 * @author Santiago Faci
 * @version Taller Desarrollo de Videojuegos Junio 2019
 */
public class GameScreen implements Screen {

    Texture dropSprite;
    Texture bucketSprite;
    Sound dropSound;
    Music rainMusic;
    Rectangle bucket;
    Array<Rectangle> drops;
    long timeLastDrop;
    long score;

    SpriteBatch spriteBatch;
    BitmapFont font;
    OrthographicCamera camera;

    public GameScreen() {
        // Carga las imágenes del juego
        dropSprite = new Texture(Gdx.files.internal("droplet.png"));
        bucketSprite = new Texture(Gdx.files.internal("bucket.png"));

        // Carga los sonidos del juego
        dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));

        // Inicia la música de fondo del juego en modo bucle
        rainMusic.setLooping(true);

        // Representa el cubo en el juego
        // Hay que tener el cuenta que la imagen del cubo es de 64x64 px
        bucket = new Rectangle();
        bucket.x = 1024 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        // Genera la lluvia
        drops = new Array<Rectangle>();
        generateDrops();

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);
    }

    @Override
    public void render(float delta) {
        // Pinta el fondo de la pantalla de azul oscuro (RGB + alpha)
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        // Limpia la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualiza la cámara
        camera.update();

        // Pinta la imágenes del juego en la pantalla
		/* setProjectionMatrix hace que el objeto utilice el
		 * sistema de coordenadas de la cámara, que
		 * es ortogonal
		 * Es recomendable pintar todos los elementos del juego
		 * entras las mismas llamadas a begin() y end()
		 */
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(bucketSprite, bucket.x, bucket.y);
        for (Rectangle drop : drops)
            spriteBatch.draw(dropSprite, drop.x, drop.y);
        font.draw(spriteBatch, score + " puntos", 1024 - 100, 768 - 50);
        spriteBatch.end();

		/*
		 * Mueve el cubo pulsando en la pantalla
		 */
        if (Gdx.input.isTouched()) {
            Vector3 position = new Vector3();
            position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			/*
			 * Transforma las coordenadas de la posición
			 * al sistema de coordenadas de la cámara
			 */
            camera.unproject(position);
            bucket.x = position.x - 64 /2;
        }

		/*
		 * Mueve el cubo pulsando las teclas LEFT y RIGHT
		 */
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

		/*
		 * Comprueba que el cubo no se salga de los
		 * límites de la pantalla
		 */
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 1024 - 64)
            bucket.x = 1024 - 64;

		/*
		 * Genera nuevas gotas dependiendo del tiempo que ha
		 * pasado desde la última
		 */
        if (TimeUtils.nanoTime() - timeLastDrop > 100000000)
            generateDrops();

		/*
		 * Actualiza las posiciones de las gotas
		 * Si la gota llega al suelo se elimina
		 * Si la gota toca el cubo suena y se elimina
		 */
        Iterator<Rectangle> iter = drops.iterator();
        while (iter.hasNext()) {
            Rectangle drop = iter.next();
            drop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (drop.y + 64 < 0)
                iter.remove();
            if (drop.overlaps(bucket)) {
                dropSound.play();
                iter.remove();
                score++;
            }
        }
    }

    /*
     * Genera una gota de lluvia en una posición aleatoria
     * de la pantalla y anota el momento de generarse
     */
    private void generateDrops() {
        Rectangle drop = new Rectangle();
        drop.x = MathUtils.random(0, 1024 - 64);
        drop.y = 768;
        drop.width = 64;
        drop.height = 64;
        drops.add(drop);
        timeLastDrop = TimeUtils.nanoTime();
    }

    /*
     * Método que se invoca cuando esta pantalla es
     * la que se está mostrando
     */
    @Override
    public void show() {
        rainMusic.play();
    }

    /*
     * Método que se invoca cuando esta pantalla
     * deja de ser la principal
     */
    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // Libera los recursos utilizados
        dropSprite.dispose();
        bucketSprite.dispose();
        dropSound.dispose();
        rainMusic.dispose();

        spriteBatch.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
