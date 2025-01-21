package com.marcos.mario.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.marcos.mario.Main;
import com.marcos.mario.Scenes.Hud;

public class Ladrillo extends InteractiveTileObject{
    public Ladrillo(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(com.marcos.mario.Main.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Ladrillo", "Colision");
        setCategoryFilter(Main.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
    }
}
