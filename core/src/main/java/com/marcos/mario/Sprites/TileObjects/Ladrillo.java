package com.marcos.mario.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.marcos.mario.Main;
import com.marcos.mario.Scenes.Hud;
import com.marcos.mario.Screens.PantallaJugar;

public class Ladrillo extends InteractiveTileObject {
    public Ladrillo(PantallaJugar screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(com.marcos.mario.Main.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Ladrillo", "Colision");
        setCategoryFilter(Main.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        Main.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
