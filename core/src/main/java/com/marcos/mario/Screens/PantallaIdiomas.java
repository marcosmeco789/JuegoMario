package com.marcos.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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

public class PantallaIdiomas implements Screen {

    private final Main game;
    private Texture[] optionImages;
    private Texture backButtonImage;
    private Stage stage;
    private Table table;
    private Label.LabelStyle labelStyle;
    private final String[] options = {"Castellano", "Inglés"};
    private BitmapFont font;
    private Texture background;
    private Preferences prefs;

    public PantallaIdiomas(Main game) {
        this.game = game;

        // Generate a sharper font using FreeType
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("super-mario-bros-nes.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Load images
        optionImages = new Texture[options.length];
        optionImages[0] = new Texture("idiomas.png");
        optionImages[1] = new Texture("idiomas.png");
        backButtonImage = new Texture("atras.png");

        // Load background image
        background = new Texture("background.jpg");

        // Load preferences
        prefs = Gdx.app.getPreferences("MyPreferences");
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Main.V_WIDTH, Main.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        for (int i = 0; i < options.length; i++) {
            Image image = new Image(optionImages[i]);
            Label label = new Label(options[i], labelStyle);

            ClickListener clickListener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String selectedLanguage = label.getText().toString().equals("Castellano") ? "es" : "en";

                    prefs.putString("language", selectedLanguage);
                    prefs.flush();

                    game.getIdiomaManager().cambiarIdioma(selectedLanguage); // Cambia el idioma en Android
                    game.setScreen(new PantallaIdiomas(game)); // Reinicia la pantalla
                }
            };

            image.addListener(clickListener);
            label.addListener(clickListener);

            table.add(image).padRight(16f);
            table.add(label).left();
            table.row().padTop(20f);
        }

        // Add back button with text
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
        backButtonImage.dispose();
    }
}
