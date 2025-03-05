package com.marcos.mario.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.marcos.mario.Main;
import com.marcos.mario.Scenes.Hud;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Mario;

public class Ladrillo extends InteractiveTileObject {
    public Ladrillo(PantallaJugar screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Main.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {

            setCategoryFilter(Main.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            Main.manager.get("audio/sounds/breakblock.wav", Sound.class).play();


    }

}
