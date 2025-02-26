package com.marcos.mario.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marcos.mario.Main;

public class OnScreenControls {
    private Stage stage;
    private Viewport viewport;

    private boolean jumpPressed, moveRight, moveLeft;

    public OnScreenControls(SpriteBatch batch) {
        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.bottom().left();
        table.setFillParent(true);

        // Botones de movimiento
        ImageButton leftButton = createButton("left_button.png", 80, 80);
        leftButton.addListener(new MovementListener(() -> moveLeft = true, () -> moveLeft = false));
        ImageButton rightButton = createButton("left_button.png", 80, 80);
        rightButton.addListener(new MovementListener(() -> moveRight = true, () -> moveRight = false));

        // Botón de acción (Saltar)
        ImageButton jumpButton = createButton("up_button.png", 40, 40);
        jumpButton.addListener(new MovementListener(() -> jumpPressed = true, () -> jumpPressed = false));

        table.add(leftButton).size(80, 80).padRight(10);
        table.add(rightButton).size(80, 80).padRight(10);
        table.add(jumpButton).size(40, 40).padRight(10);

        stage.addActor(table);
    }

    private ImageButton createButton(String texturePath, float width, float height) {
        Texture texture = new Texture(texturePath);
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawable;
        style.imageDown = drawable;
        style.up = null;
        style.down = null;

        ImageButton button = new ImageButton(style);
        button.setSize(width, height);
        button.getImage().setSize(width, height);
        button.getImageCell().size(width, height);
        button.setTransform(true);
        button.setScale(1.0f);

        return button;
    }

    private static class MovementListener extends InputListener {
        private final Runnable onPress, onRelease;

        public MovementListener(Runnable onPress, Runnable onRelease) {
            this.onPress = onPress;
            this.onRelease = onRelease;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            onPress.run();
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            onRelease.run();
        }
    }

    public void render(SpriteBatch batch) {
        stage.draw();
    }

    public void update() {
        stage.act();
    }

    public boolean isJumpPressed() {
        return jumpPressed;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
