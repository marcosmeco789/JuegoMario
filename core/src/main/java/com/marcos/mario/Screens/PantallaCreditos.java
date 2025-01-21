package com.marcos.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.marcos.mario.Main;

public class PantallaCreditos implements Screen {
    private final Main game; // Referencia a la clase principal
    private BitmapFont font; // Fuente para el texto
    private String[] opciones = {"Volver al menu principal"};
    private int seleccion = -1; // Opción seleccionada, inicialmente no hay ninguna
    private GlyphLayout glyphLayout; // Usado para medir el texto
    private float touchY; // Posición del toque en Y para detectar la opción seleccionada
    private float touchX; // Posición del toque en X para detectar la opción seleccionada
    private Texture background; // Textura para la imagen de fondo

    public PantallaCreditos(Main game) {
        this.game = game;

        // Genera la nueva fuente desde el archivo TTF
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("super-mario-bros-nes.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32; // Tamaño de la fuente
        font = generator.generateFont(parameter);
        font.getData().setScale(1.5f); // Aumenta el tamaño de la fuente
        generator.dispose(); // Libera el generador de fuentes

        glyphLayout = new GlyphLayout(); // Inicializa el objeto GlyphLayout
        background = new Texture("background.png");
    }

    @Override
    public void render(float delta) {
        // Limpia la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibuja la imagen de fondo
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Obtiene el alto y ancho de la pantalla
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // Dibuja la opción del menú
        int numOpciones = opciones.length;

        // Establece la altura base del texto
        float optionHeight = font.getCapHeight() * 4f; // Altura del texto escalado (ajustable)
        float totalHeight = optionHeight * numOpciones; // Altura total ocupada por las opciones

        // Dibuja el texto de la opción, centrado
        for (int i = 0; i < numOpciones; i++) {
            // Mide el texto
            glyphLayout.setText(font, opciones[i]);

            // Calcula la posición centrada en la pantalla (horizontal y vertical)
            float x = (width - glyphLayout.width) / 2f; // Centrado horizontal
            float y = (height - totalHeight) / 2f + (numOpciones - i - 1) * (optionHeight + optionHeight / 2f) - 270;

            // Si la opción es seleccionada o tocada, la cambia a amarillo
            if (i == seleccion) {
                font.setColor(1, 1, 0, 1); // Amarillo para la opción seleccionada
            } else {
                font.setColor(0, 0, 0, 1); // Negro para las otras opciones
            }

            // Dibuja el texto
            font.draw(game.batch, opciones[i], x, y);
        }
        game.batch.end();

        // Detección táctil
        if (Gdx.input.isTouched()) {
            touchX = Gdx.input.getX(); // Obtiene la coordenada X del toque
            touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invierte la coordenada Y del toque

            // Detecta la opción seleccionada basada en el toque
            for (int i = 0; i < opciones.length; i++) {
                // Mide el tamaño de cada opción
                glyphLayout.setText(font, opciones[i]);

                // Calcula el área de la opción en el eje Y
                float top = (height - totalHeight) / 2f + (numOpciones - i - 1) * optionHeight + optionHeight / 2f + glyphLayout.height / 2 - 370;
                float bottom = top - glyphLayout.height - 10;

                // Calcula el área de la opción en el eje X (debe estar dentro del texto)
                float left = (width - glyphLayout.width) / 2f;
                float right = left + glyphLayout.width;

                // Si el toque está dentro del área de la opción (en los límites de X e Y del texto)
                if (touchY > bottom && touchY < top && touchX > left && touchX < right) {
                    seleccion = i; // Selecciona la opción si está tocada dentro del texto
                    break;
                }
            }
        } else if (seleccion != -1) {
            // Ejecuta la selección solo cuando el toque ya no está activo
            manejarSeleccion();
            seleccion = -1; // Resetea la selección después de manejarla
        }
    }

    private void manejarSeleccion() {
        if (seleccion == -1) return; // Si no hay selección, no hacer nada

        switch (seleccion) {
            case 0: // Volver al menú principal
                game.setScreen(new PantallaMenu(game));
                break;
        }

        // Resetea la selección después de hacer la elección
        seleccion = -1;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        font.dispose();
        background.dispose(); // Libera la textura de la imagen de fondo
    }
}
