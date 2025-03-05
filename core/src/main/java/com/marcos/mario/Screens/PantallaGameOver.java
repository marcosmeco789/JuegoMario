package com.marcos.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.marcos.mario.Main;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Screens.PantallaMenu;

public class PantallaGameOver implements Screen {
    private final Main game;
    private BitmapFont font;
    private Texture background;
    private Texture[] optionImages;
    private Label.LabelStyle labelStyle;
    private Stage stage;
    private Table table;
    private final String[] options = {"Volver a jugar", "Menu"};

    public PantallaGameOver(Main game) {
        this.game = game;

        // Generar una fuente más nítida utilizando FreeType
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("super-mario-bros-nes.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Cargar imágenes
        optionImages = new Texture[2];
        optionImages[0] = new Texture("reinicioRecords.png");
        optionImages[1] = new Texture("atras.png");

        // Cargar la imagen de fondo
        background = new Texture("background.jpg");

        // Inicializar el estilo de la etiqueta
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Main.V_WIDTH, Main.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        for (int i = 0; i < options.length; i++) {
            Image image = new Image(optionImages[i]);
            Label label = new Label(options[i], labelStyle);

            // Añadir ClickListener a la etiqueta (o imagen)
            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (label.getText().toString().equals("Volver a jugar")) {
                        game.setScreen(new PantallaJugar(game, Main.currentMapNumber));
                    } else if (label.getText().toString().equals("Menu")) {
                        game.setScreen(new PantallaMenu(game));
                    }
                }
            });

            table.add(image).padRight(16f);
            table.add(label).left();
            table.row().padTop(20f);
        }
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
        for (Texture texture : optionImages) {
            texture.dispose();
        }
        background.dispose();
    }
}
