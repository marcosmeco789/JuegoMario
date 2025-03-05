package com.marcos.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.marcos.mario.Scenes.Hud;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Screens.PantallaMenu;
import com.marcos.mario.Tools.IdiomaManager;
import com.marcos.mario.Tools.IdiomaManagerImpl;

public class Main extends Game {
    public static final int V_WIDTH = 400; // Ancho de la ventana
    public static final int V_HEIGHT = 225; // Alto de la ventana
    public static final float PPM = 100;

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short MARIO_HEAD_BIT = 512;
    public static final short ESCALERA_BIT = 1024;
    public static final short TECHO_ESCALERA_BIT = 2048;
    public static final short MUERTE_BIT = 4096;
    public static final short OBJETO_PINCHOS_BIT = 8192;
    public static int currentMapNumber = 1;

    private Hud hud;


    public SpriteBatch batch; // Usado para dibujar en todas las pantallas

    public static AssetManager manager; // Carga los recursos del juego

    private IdiomaManager idiomaManager; // Manejador de idiomas

    // 🔹 Nuevo constructor para recibir el IdiomaManager
    public Main(IdiomaManager idiomaManager) {
        this.idiomaManager = idiomaManager;
    }

    public IdiomaManager getIdiomaManager() {
        return idiomaManager;
    }

    public void cambiarIdioma(String nuevoIdioma) {
        idiomaManager = new IdiomaManagerImpl(nuevoIdioma);
    }


    public Hud getHud() {
        return hud; // Método para obtener la instancia de Hud
    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("audio/music/mario_music.ogg", Music.class);
        manager.load("audio/sounds/coin.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
        manager.load("audio/sounds/powerup.wav", Sound.class);
        manager.load("audio/sounds/powerdown.wav", Sound.class);
        manager.load("audio/sounds/stomp.wav", Sound.class);
        manager.load("audio/sounds/mariodie.wav", Sound.class);
        manager.finishLoading();

        hud = new Hud(batch);

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
