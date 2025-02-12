package com.marcos.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marcos.mario.Main;

public class PantallaMenu implements Screen {
    private final Main game;
    private BitmapFont font;
    private String[] opciones = {"Jugar", "Opciones", "Records", "Creditos", "Salir"};
    private int seleccion = -1;
    private GlyphLayout glyphLayout;
    private float touchY;
    private float touchX;
    private Texture background;

    private OrthographicCamera camera;
    private Viewport viewport;

    public PantallaMenu(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
        camera.position.set(Main.V_WIDTH / 2f, Main.V_HEIGHT / 2f, 0);
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();
        background = new Texture("background.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        font.draw(game.batch, "Jugar", Main.V_WIDTH / 2f, Main.V_HEIGHT / 2f);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            touchX = Gdx.input.getX();
            touchY = Gdx.input.getY();
            if (touchX > Main.V_WIDTH / 2f - 50 && touchX < Main.V_WIDTH / 2f + 50 &&
                touchY > Main.V_HEIGHT / 2f - 20 && touchY < Main.V_HEIGHT / 2f + 20) {
                game.setScreen(new PantallaJugar(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
    }
}
