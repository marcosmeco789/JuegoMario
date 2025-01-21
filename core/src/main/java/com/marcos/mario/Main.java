package com.marcos.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.marcos.mario.Screens.PantallaMenu;

public class Main extends Game {
    public static final int V_WIDTH = 400; // Ancho de la ventana
    public static final int V_HEIGHT = 208; // Alto de la ventana
    public static final float PPM = 100;
    public SpriteBatch batch; // Usado para dibujar en todas las pantallas

    public static final short DEFAULT_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PantallaMenu(this)); // Muestra la pantalla del menú
    }

    @Override
    public void render() {
        super.render(); // Renderiza la pantalla actual
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
