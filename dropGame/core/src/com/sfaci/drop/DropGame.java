package com.sfaci.drop;

import com.badlogic.gdx.Game;

/**
 * Clase principal del proyecto principal del game
 *
 * Ejemplo de game de la Wiki oficial de libgdx (https://github.com/libgdx/libgdx/wiki/A-simple-game)
 * Los recursos utilizados son propiedad de sus respectivos dueños:
 * audio waterdrop.wav, de junggle (http://www.freesound.org/people/junggle/sounds/30341/)
 * audio undertreeinrain.mp3, de acclivity (http://www.freesound.org/people/acclivity/sounds/28283/)
 * sprite droplet.png, de mvdv (https://www.box.com/s/peqrdkwjl6guhpm48nit)
 * sprite bucket.png sprite, de mvdv (https://www.box.com/s/605bvdlwuqubtutbyf4x )
 *
 * @author Santiago Faci
 * @version Taller Desarrollo de Videojuegos Junio 2019
 */

public class DropGame extends Game {

	@Override
	public void create () {
		setScreen(new MainMenuScreen());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
	}
}
