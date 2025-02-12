package com.marcos.mario.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Mario;

public abstract class Enemigo extends Sprite {
    protected World world;
    protected PantallaJugar screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemigo(PantallaJugar screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemigo();
        velocity = new Vector2(-1, -2);
        b2body.setActive(false);
    }

    protected abstract void defineEnemigo();
    public abstract void update(float dt);
    public abstract void hitOnHead(Mario mario);
    public abstract  void onEnemyHit(Enemigo enemigo);


    public void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }
}
