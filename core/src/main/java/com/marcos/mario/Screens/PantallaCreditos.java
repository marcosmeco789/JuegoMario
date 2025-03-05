package com.marcos.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.marcos.mario.Main;

public class PantallaCreditos implements Screen {

    private final Main game;
    private Stage stage;
    private Table table;
    private Label.LabelStyle labelStyle;
    private BitmapFont font;
    private Texture background;
    private Texture backButtonImage;

    public PantallaCreditos(Main game) {
        this.game = game;

        // Genera una fuente más nítida usando FreeType
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("super-mario-bros-nes.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Carga la imagen de fondo
        background = new Texture("background.jpg");
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Main.V_WIDTH, Main.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        // Crear y añadir las etiquetas
        Label label1 = new Label("Hecho por: Marcos Gonzalez Ferreira", labelStyle);
        Label label2 = new Label("Fuente de letra por: TheWolfBunny", labelStyle);
        Label label3 = new Label("Assets por: VEXED", labelStyle);
        Label label4 = new Label("Iconos por: Rad Potato", labelStyle);

        table.add(label1).padBottom(10f).row();
        table.add(label2).padBottom(10f).row();
        table.add(label3).padBottom(10f).row();
        table.add(label4).padBottom(10f).row();

        // Añadir el botón de "volver" con texto
        Label backLabel = new Label(game.getIdiomaManager().getString("volver"), new Label.LabelStyle(font, Color.BLACK));
        ClickListener backClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PantallaMenu(game));
            }
        };

        backLabel.addListener(backClickListener);

        table.row().padTop(20f);
        table.add(backLabel).colspan(2).center();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        font.dispose();
        background.dispose();
    }
}
