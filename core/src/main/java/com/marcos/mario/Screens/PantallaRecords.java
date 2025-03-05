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
import com.marcos.mario.Tools.RecordManager;

public class PantallaRecords implements Screen {
    private final Main game;
    private BitmapFont font;
    private PantallaRecords pantallaRecords;
    private Texture background;
    private Stage stage;
    private Label.LabelStyle labelStyle;
    private Texture backButtonImage;
    private Table table;
    private Texture level1Image;
    private Texture level2Image;
    private RecordManager recordManager;
    private Label bestTimeLabel1;
    private Label bestTimeLabel2;

    public PantallaRecords(Main game) {
        this.game = game;
        this.recordManager = new RecordManager();

        PantallaOpciones pantallaOpciones = new PantallaOpciones(game);
        pantallaOpciones.setPantallaRecords(pantallaRecords);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("super-mario-bros-nes.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        generator.dispose();

        background = new Texture("background.jpg");
        backButtonImage = new Texture("atras.png");
        level1Image = new Texture("records.png");
        level2Image = new Texture("records.png");

        labelStyle = new Label.LabelStyle(font, Color.WHITE);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Main.V_WIDTH, Main.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        actualizarRecords();

        Image backButton = new Image(backButtonImage);
        Label backLabel = new Label(game.getIdiomaManager().getString("volver"), labelStyle);

        ClickListener backClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PantallaMenu(game));
            }
        };
        backButton.addListener(backClickListener);
        backLabel.addListener(backClickListener);

        table.add(backButton).padRight(16f);
        table.add(backLabel).left();
        table.row().padTop(20f);
    }

    public void actualizarRecords() {
        int bestTimeLevel1 = recordManager.getBestTime(2);  // Nivel 1
        int bestTimeLevel2 = recordManager.getBestTime(3);  // Nivel 2

        // Verifica los tiempos leídos
        Gdx.app.log("PantallaRecords", "Tiempos leídos: Nivel 1 = " + bestTimeLevel1 + ", Nivel 2 = " + bestTimeLevel2);

        Image level1ImageActor = new Image(level1Image);
        bestTimeLabel1 = new Label(
            "Nivel 1: " + (bestTimeLevel1 == 9999 ? "N/A" : bestTimeLevel1 + " segundos"),
            labelStyle
        );

        table.clear();

        table.add(level1ImageActor).padRight(16f);
        table.add(bestTimeLabel1).left();
        table.row().padTop(20f);

        Image level2ImageActor = new Image(level2Image);
        bestTimeLabel2 = new Label(
            "Nivel 2: " + (bestTimeLevel2 == 9999 ? "N/A" : bestTimeLevel2 + " segundos"),
            labelStyle
        );

        table.add(level2ImageActor).padRight(16f);
        table.add(bestTimeLabel2).left();
        table.row().padTop(20f);
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
    public void dispose() {
        stage.dispose();
        font.dispose();
        background.dispose();
        backButtonImage.dispose();
        level1Image.dispose();
        level2Image.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
