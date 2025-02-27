package com.marcos.mario.Scenes;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class VirtualJoystick extends Actor {
    private Texture baseTexture;
    private Texture knobTexture;
    private Vector2 knobPosition;
    private float radius;

    public VirtualJoystick() {
        // Cargar las imágenes desde el directorio assets
        baseTexture = new Texture("joystick_base.png");
        knobTexture = new Texture("joystick_knob.png");

        // Definir el tamaño del actor igual al de la base del joystick
        setSize(baseTexture.getWidth(), baseTexture.getHeight());

        // Posicionar inicialmente el knob en el centro del joystick
        knobPosition = new Vector2(
            getX() + getWidth() / 2 - knobTexture.getWidth() / 2,
            getY() + getHeight() / 2 - knobTexture.getHeight() / 2
        );

        // El radio es la mitad del ancho de la base
        radius = getWidth() / 2f;

        // Añadir listener para detectar toques y arrastres
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updateKnob(x, y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updateKnob(x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Al soltar, el knob vuelve al centro
                knobPosition.set(
                    getX() + getWidth() / 2 - knobTexture.getWidth() / 2,
                    getY() + getHeight() / 2 - knobTexture.getHeight() / 2
                );
            }
        });
    }

    /**
     * Actualiza la posición del knob según la posición del toque.
     * Se limita el movimiento al radio del joystick.
     */
    private void updateKnob(float x, float y) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float deltaX = x - centerX;
        float deltaY = y - centerY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > radius) {
            float norm = radius / distance;
            deltaX *= norm;
            deltaY *= norm;
        }

        knobPosition.set(
            getX() + centerX + deltaX - knobTexture.getWidth() / 2,
            getY() + centerY + deltaY - knobTexture.getHeight() / 2
        );
    }

    /**
     * Devuelve un vector con la dirección y magnitud (en porcentaje, rango [-1,1])
     * del movimiento del knob respecto al centro del joystick.
     */
    public Vector2 getKnobPercentage() {
        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;
        float percentX = ( (knobPosition.x + knobTexture.getWidth() / 2f) - centerX ) / radius;
        float percentY = ( (knobPosition.y + knobTexture.getHeight() / 2f) - centerY ) / radius;
        return new Vector2(percentX, percentY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Dibujar la base y luego el knob
        batch.draw(baseTexture, getX(), getY());
        batch.draw(knobTexture, knobPosition.x, knobPosition.y);
    }

    // Es recomendable crear un método para liberar recursos cuando ya no se necesite el joystick
    public void dispose() {
        baseTexture.dispose();
        knobTexture.dispose();
    }
}
