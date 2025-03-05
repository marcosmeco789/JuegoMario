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
import com.marcos.mario.Sprites.Enemies.Abeja;
import com.marcos.mario.Sprites.Enemies.Enemigo;
import com.marcos.mario.Sprites.Enemies.Goomba;
import com.marcos.mario.Sprites.Enemies.Pajaro;
import com.marcos.mario.Sprites.Enemies.Pinchos;
import com.marcos.mario.Sprites.Enemies.Turtle;
import com.marcos.mario.Sprites.TileObjects.Ladrillo;
import com.marcos.mario.Sprites.TileObjects.Moneda;



public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;
    private Array<Abeja> abejas;
    private Array<Pajaro> pajaros;



    public B2WorldCreator(PantallaJugar screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;



        for (MapObject object : map.getLayers().get("Final").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            fdef.isSensor = true; // Make it a sensor
            fdef.filter.categoryBits = Main.OBJECT_BIT;
            body.createFixture(fdef).setUserData("final");
        }


// Capa "Pinchos"
        for (MapObject object : map.getLayers().get("Pinchos").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            BodyDef pinchosBdef = new BodyDef();
            pinchosBdef.type = BodyDef.BodyType.StaticBody;
            pinchosBdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM,
                (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            Body pinchosBody = world.createBody(pinchosBdef);

            PolygonShape pinchosShape = new PolygonShape();
            pinchosShape.setAsBox((rect.getWidth() / 2) / Main.PPM,
                (rect.getHeight() / 2) / Main.PPM);

            FixtureDef pinchosFdef = new FixtureDef();
            pinchosFdef.shape = pinchosShape;
            pinchosFdef.filter.categoryBits = Main.OBJETO_PINCHOS_BIT;
            pinchosFdef.filter.maskBits = Main.MARIO_BIT; // Only Mario can collide with spikes
            pinchosFdef.isSensor = true; // Set the spikes as sensors

            pinchosBody.createFixture(pinchosFdef).setUserData(new Pinchos());

            pinchosShape.dispose();
        }

        for (MapObject object : map.getLayers().get("Muerte").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Main.MUERTE_BIT;
            body.createFixture(fdef).setUserData("muerte");
        }

        // Creation of ladder and ceiling ladder fixtures
        for (MapObject object : map.getLayers().get("Escalera").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            String tipo = object.getProperties().get("tipo", String.class);

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;

            if ("escalera".equals(tipo)) {
                fdef.isSensor = true; // Make the ladder a sensor
                fdef.filter.categoryBits = Main.ESCALERA_BIT;
                body.createFixture(fdef).setUserData("escalera");
            } else if ("techoEscalera".equals(tipo)) {
                fdef.isSensor = false; // The ceiling of the ladder is not a sensor
                fdef.filter.categoryBits = Main.TECHO_ESCALERA_BIT;
                body.createFixture(fdef).setUserData("techoEscalera");
            }
        }

        // creacion suelo fixture
        for (MapObject object : map.getLayers().get("Ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Moneda(screen, object);
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
        for(MapObject object : map.getLayers().get("Bricks").getObjects().getByType(RectangleMapObject.class)){
            new Ladrillo(screen, object);
        }


        // creacion monedas fixture
        for(MapObject object : map.getLayers().get("Coins").getObjects().getByType(RectangleMapObject.class)){

            new Moneda(screen, object);
        }

        // creacion goombas
        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get("Goombas").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
        }


        abejas = new Array<Abeja>();
        for (MapObject object : map.getLayers().get("Abeja").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            abejas.add(new Abeja(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
        }

        pajaros = new Array<>();
        for (MapObject object : map.getLayers().get("Pajaro").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            pajaros.add(new Pajaro(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
        }


        // creacion tortugas
        turtles = new Array<Turtle>();
        for (MapObject object : map.getLayers().get("Turtles").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
        }


        // creacion GoombaBounds
        for (MapObject object : map.getLayers().get("GoombaBounds").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);
            body = world.createBody(bdef);

            shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);


            fdef = new FixtureDef();
            fdef.shape = shape;
            fdef.isSensor = true; // Make it a sensor so Mario can pass through
            fdef.filter.categoryBits = Main.OBJECT_BIT; // Set the category bit
            fdef.filter.maskBits = Main.ENEMY_BIT; // Only collide with enemies

            body.createFixture(fdef).setUserData("goomba_bound");

            // Ensure the body is always active
            body.setActive(true);
        }



    }




    

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public Array<Abeja> getAbejas() {
        return abejas;
    }

    public Array<Pajaro> getPajaros() {
        return pajaros;
    }

    public Array<Enemigo> getEnemigos() {
        Array<Enemigo> enemigos = new Array<Enemigo>();
        enemigos.addAll(goombas);
        enemigos.addAll(turtles);
        enemigos.addAll(abejas);
        enemigos.addAll(pajaros);
        return enemigos;
    }
}
