package com.marcos.mario.Scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class JumpButton extends Actor {
    private Texture buttonTexture;
    private boolean pressed;

    public JumpButton() {
        // Carga la imagen del botón de salto desde assets
        buttonTexture = new Texture("ARROWUP.png");
        float scale = 10.5f;
        setSize(buttonTexture.getWidth() * scale, buttonTexture.getHeight() * scale);

        // Listener para detectar cuando se pulsa el botón
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressed = false;
            }
        });
    }

    public boolean isPressed() {
        return pressed;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(buttonTexture, getX(), getY(), getWidth(), getHeight());
    }

    // Libera los recursos cuando ya no se necesiten
    public void dispose() {
        buttonTexture.dispose();
    }
}
