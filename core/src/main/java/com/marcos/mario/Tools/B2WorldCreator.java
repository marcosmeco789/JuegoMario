package com.marcos.mario.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.marcos.mario.Main;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Enemies.Enemigo;
import com.marcos.mario.Sprites.Enemies.Goomba;
import com.marcos.mario.Sprites.Enemies.Turtle;
import com.marcos.mario.Sprites.TileObjects.Ladrillo;
import com.marcos.mario.Sprites.TileObjects.Moneda;



public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    public B2WorldCreator(PantallaJugar screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;




        // creacion suelo fixture
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


        // creacion tuberia fixture
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Main.OBJECT_BIT;

            body.createFixture(fdef);
        }


        // creacion ladrillos fixture
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {


            new Ladrillo(screen, object);
        }


        // creacion monedas fixture
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            new Moneda(screen, object);
        }

        // creacion goombas
        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
        }

        // creacion tortugas
        turtles = new Array<Turtle>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
        }


    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public Array<Enemigo> getEnemigos() {
        Array<Enemigo> enemigos = new Array<Enemigo>();
        enemigos.addAll(goombas);
        enemigos.addAll(turtles);
        return enemigos;
    }
}
