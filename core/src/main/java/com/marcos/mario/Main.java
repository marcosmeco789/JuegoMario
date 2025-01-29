package com.marcos.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.marcos.mario.Screens.PantallaMenu;

public class Main extends Game {
    public static final int V_WIDTH = 400; // Ancho de la ventana
    public static final int V_HEIGHT = 208; // Alto de la ventana
    public static final float PPM = 100;

    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;


    public SpriteBatch batch; // Usado para dibujar en todas las pantallas

    public static AssetManager manager; // Carga los recursos del juego

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("audio/music/mario_music.ogg", Music.class);
        manager.load("audio/sounds/coin.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.finishLoading();


        setScreen(new PantallaMenu(this)); // Muestra la pantalla del menú
    }

    @Override
    public void render() {
        super.render(); // Renderiza la pantalla actual

    }

    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }
}
