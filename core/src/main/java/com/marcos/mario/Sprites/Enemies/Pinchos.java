package com.marcos.mario.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Pinchos {
    public void onContact(Fixture fixtureA, Fixture fixtureB) {
        Gdx.app.log("Pinchos", "Jugador tocó los pinchos");
    }
}
