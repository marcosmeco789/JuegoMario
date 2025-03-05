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
import com.marcos.mario.Scenes.Hud;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Screens.PantallaMenu;
import com.marcos.mario.Tools.RecordManager;

public class PantallaVictoria implements Screen {
    private final Main game;
    private BitmapFont font;
    private Texture background;
    private Texture[] optionImages;
    private Stage stage;
    private Table table;
    private Label.LabelStyle labelStyle;
    private final String[] options = {"Volver a jugar", "Menu"};
    private Hud hud;
    private Label currentTimeLabel;
    private RecordManager recordManager;
    private int nivelActual;
    private int remainingTime; // Este ya lo tienes en tu clase
    private Label bestTimeLabel;

    public PantallaVictoria(Main game, int remainingTime, int nivelActual) {
        this.game = game;
        this.nivelActual = nivelActual;
        this.remainingTime = remainingTime;
        this.recordManager = new RecordManager();

        // Actualizar el mejor tiempo
        if (nivelActual != 0) { // Ignorar el tutorial
            recordManager.updateBestTime(nivelActual, remainingTime);
        }

        stage = new Stage(new FitViewport(Main.V_WIDTH, Main.V_HEIGHT));
        background = new Texture("background.jpg");

        // Generar una fuente más nítida usando FreeType
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("super-mario-bros-nes.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Cargar imágenes
        optionImages = new Texture[options.length];
        optionImages[0] = new Texture("reinicioRecords.png");
        optionImages[1] = new Texture("atras.png");

        // Inicializar el estilo de la etiqueta
        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        // Inicializar la etiqueta del tiempo actual
        currentTimeLabel = new Label("Completado en: " + remainingTime + " segundos", labelStyle);

        // Inicializar la etiqueta del mejor tiempo
        int bestTime = recordManager.getBestTime(nivelActual); // Obtener el mejor tiempo
        bestTimeLabel = new Label(
            "Mejor tiempo: " + (bestTime == Integer.MAX_VALUE ? "N/A" : bestTime + " segundos"), labelStyle);

        // Crear la tabla y agregar las etiquetas
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(currentTimeLabel).colspan(2).padBottom(20f);
        table.row();
        table.add(bestTimeLabel).colspan(2).padBottom(20f); // Mostrar el mejor tiempo
        table.row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        for (int i = 0; i < options.length; i++) {
            Image image = new Image(optionImages[i]);
            Label label = new Label(options[i], labelStyle);

            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (label.getText().toString().equals("Volver a jugar")) {
                        game.setScreen(new PantallaJugar(game, Main.currentMapNumber));
                        dispose();
                    } else if (label.getText().toString().equals("Menu")) {
                        game.setScreen(new PantallaMenu(game));
                        dispose();
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
    }
}
