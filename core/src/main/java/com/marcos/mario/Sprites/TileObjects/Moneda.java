package com.marcos.mario.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.marcos.mario.Main;
import com.marcos.mario.Scenes.Hud;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Items.ItemDef;
import com.marcos.mario.Sprites.Items.Mushroom;
import com.marcos.mario.Sprites.Mario;

public class Moneda extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int MONEDA_VACIA = 1243; // ID de la textura de la moneda vacía

    public Moneda(PantallaJugar screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("World-Tiles");
        fixture.setUserData(this);
        setCategoryFilter(Main.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(getCell().getTile().getId() == MONEDA_VACIA)
            Main.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {

            Main.manager.get("audio/sounds/coin.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(MONEDA_VACIA));
            Hud.addScore(100);
        }
    }
}

