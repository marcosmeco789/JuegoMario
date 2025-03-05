package com.marcos.mario.Screens;

import static com.marcos.mario.Main.currentMapNumber;

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

public class PantallaSelector implements Screen {
    private final Main game;
    private Texture background;
    private Texture[] optionImages;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;

    // Opciones actualizadas con "Tutorial", "Nivel 1" y "Nivel 2"
    private String[] options;

    public PantallaSelector(Main game) {
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
        optionImages = new Texture[3]; // Solo 3 opciones ahora
        optionImages[0] = new Texture("jugar.png");
        optionImages[1] = new Texture("jugar.png");
        optionImages[2] = new Texture("jugar.png");

        // Cargar la imagen de fondo
        background = new Texture("background.jpg");

        // Establecer las opciones del menú según el idioma
        updateOptions();
    }

    private void updateOptions() {
        // Obtener las opciones del menú desde el IdiomaManager
        options = new String[] {
            game.getIdiomaManager().getString("tutorial"),
            game.getIdiomaManager().getString("nivel1"),
            game.getIdiomaManager().getString("nivel2")
        };
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

            // Añadir ClickListener a la etiqueta (o imagen)
            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Manejar el clic, por ejemplo:
                    if (label.getText().toString().equals(game.getIdiomaManager().getString("tutorial"))) {
                        currentMapNumber = 1;
                        game.setScreen(new PantallaJugar(game, 1)); // 1 para tutorial
                    } else if (label.getText().toString().equals(game.getIdiomaManager().getString("nivel1"))) {
                        currentMapNumber = 2;
                        game.setScreen(new PantallaJugar(game, 2)); // 2 para nivel 1
                    } else if (label.getText().toString().equals(game.getIdiomaManager().getString("nivel2"))) {
                        currentMapNumber = 3;
                        game.setScreen(new PantallaJugar(game, 3)); // 3 para nivel 2
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
