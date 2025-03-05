package com.marcos.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
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

public class PantallaOpciones implements Screen {

    private final Main game;
    private RecordManager recordManager = new RecordManager();
    private Texture[] optionImages;
    private Texture backButtonImage;
    private Stage stage;
    private Table table;
    private Label.LabelStyle labelStyle;
    private String[] options;
    private boolean isVolumeActivated = true;
    private Image volumeImage;
    private BitmapFont font;
    private Texture background;
    private boolean isVolumeOn = true;
    private Preferences prefs;
    private PantallaRecords pantallaRecords;

    public PantallaOpciones(Main game) {
        this.game = game;

        // Actualiza las opciones usando getString() para obtener los textos del idioma correcto
        options = new String[] {
            game.getIdiomaManager().getString("volumen"),
            game.getIdiomaManager().getString("reiniciarRecords"),
            game.getIdiomaManager().getString("idioma")
        };

        // Genera una fuente más nítida usando FreeType
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("super-mario-bros-nes.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Carga las imágenes
        optionImages = new Texture[options.length];
        optionImages[0] = new Texture("volumenActivado.png");
        optionImages[1] = new Texture("reinicioRecords.png");
        optionImages[2] = new Texture("seleccionarIdioma.png");
        backButtonImage = new Texture("atras.png");

        // Carga la imagen de fondo
        background = new Texture("background.jpg");

        // Carga las preferencias
        prefs = Gdx.app.getPreferences("MyPreferences");
        isVolumeActivated = prefs.getBoolean("volumeActivated", true);
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

            if (i == 0) {
                volumeImage = image;
                updateVolumeImage();
            }

            ClickListener clickListener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Maneja el click
                    if (label.getText().toString().equals(game.getIdiomaManager().getString("volumen"))) {
                        toggleVolume();
                    } else if (label.getText().toString().equals(game.getIdiomaManager().getString("reiniciarRecords"))) {
                        resetRecords();
                        recordManager.resetRecords();
                    } else if (label.getText().toString().equals(game.getIdiomaManager().getString("idioma"))) {
                        game.setScreen(new PantallaIdiomas(game));
                    }
                }
            };

            image.addListener(clickListener);
            label.addListener(clickListener);

            table.add(image).padRight(16f);
            table.add(label).left();
            table.row().padTop(20f);
        }

        // Agrega el botón de "volver"
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

    private void toggleVolume() {
        isVolumeActivated = !isVolumeActivated;
        updateVolumeImage();
        prefs.putBoolean("volumeActivated", isVolumeActivated);
        prefs.flush();

        if (isVolumeActivated) {
            Main.manager.get("audio/music/mario_music.ogg", Music.class).play();
        } else {
            Main.manager.get("audio/music/mario_music.ogg", Music.class).pause();
        }
    }

    private void updateVolumeImage() {
        if (isVolumeActivated) {
            volumeImage.setDrawable(new Image(new Texture("volumenActivado.png")).getDrawable());
        } else {
            volumeImage.setDrawable(new Image(new Texture("volumenDesactivado.png")).getDrawable());
        }
    }

    public void setPantallaRecords(PantallaRecords pantallaRecords) {
        this.pantallaRecords = pantallaRecords;
    }

    private void resetRecords() {
        Gdx.app.log("PantallaOpciones", "Reseteando records");

        RecordManager recordManager = new RecordManager();

        // Elimina las claves de los registros antes de restablecer los tiempos
        prefs.remove("bestTimeLevel2");
        prefs.remove("bestTimeLevel3");
        prefs.flush();  // Asegúrate de que los cambios se guarden

        // Verifica que las claves hayan sido eliminadas correctamente
        Gdx.app.log("PantallaOpciones", "Nivel 1: " + prefs.getInteger("bestTimeLevel2", -1));
        Gdx.app.log("PantallaOpciones", "Nivel 2: " + prefs.getInteger("bestTimeLevel3", -1));

        // Ahora, actualiza los registros con el nuevo valor
        recordManager.updateBestTime(2, 9999);  // Resetea el tiempo del nivel 1
        recordManager.updateBestTime(3, 9999);  // Resetea el tiempo del nivel 2

        // Verifica que los registros se hayan actualizado correctamente
        Gdx.app.log("PantallaOpciones", "Tiempos después de reset: " +
            recordManager.getBestTime(2) + ", " + recordManager.getBestTime(3));

        if (pantallaRecords != null) {
            // Asegúrate de que la pantalla de records se actualice
            pantallaRecords.actualizarRecords();
        }

        Gdx.input.vibrate(500);
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
