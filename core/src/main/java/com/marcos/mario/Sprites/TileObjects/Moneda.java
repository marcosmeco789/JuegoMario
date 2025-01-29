package com.marcos.mario.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marcos.mario.Main;
import com.marcos.mario.Scenes.Hud;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Items.ItemDef;
import com.marcos.mario.Sprites.Items.Mushroom;

public class Moneda extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Moneda(PantallaJugar screen,Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(Main.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Moneda", "Colision");
        if (getCell().getTile().getId() == BLANK_COIN){
            Main.manager.get("audio/sounds/bump.wav", com.badlogic.gdx.audio.Sound.class).play();
        }
        else {
            Main.manager.get("audio/sounds/coin.wav", com.badlogic.gdx.audio.Sound.class).play();
            screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Main.PPM), Mushroom.class));
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);


    }
}
