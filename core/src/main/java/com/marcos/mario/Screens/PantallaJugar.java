package com.marcos.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marcos.mario.Main;
import com.marcos.mario.Scenes.Hud;
import com.marcos.mario.Sprites.Enemies.Enemigo;
import com.marcos.mario.Sprites.Items.Item;
import com.marcos.mario.Sprites.Items.ItemDef;
import com.marcos.mario.Sprites.Items.Mushroom;
import com.marcos.mario.Sprites.Mario;
import com.marcos.mario.Sprites.TileObjects.Ladrillo;
import com.marcos.mario.Sprites.TileObjects.Moneda;
import com.marcos.mario.Tools.B2WorldCreator;
import com.marcos.mario.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class PantallaJugar implements Screen {
    private Main game;
    private TextureAtlas atlas;
    private Mario player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    // variables del Tiled
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // variables Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    public PantallaJugar(Main game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gamecam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = Main.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();
    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }


    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }



    public void handleInput(float dt){
        //control our player using immediate impulses

        if (player.currentState != Mario.State.DEAD){
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y == 0 && !player.isJumping) {
                player.b2body.applyLinearImpulse(new Vector2(0, 3.95f), player.b2body.getWorldCenter(), true);
                player.isJumping = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

            if (player.b2body.getLinearVelocity().y == 0) {
                player.isJumping = false;
            }
        }

    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for (Enemigo enemigo : creator.getEnemigos()) {
            enemigo.update(dt);
            if (enemigo.getX() < player.getX() + 224 / Main.PPM) {
                enemigo.b2body.setActive(true);
            }
        }

        for (Item item : items) {
            item.update(dt);
        }
        hud.update(dt);

        if (player.currentState != Mario.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        //renderiza el mapa
        renderer.render();

        //renderiza Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemigo enemigo : creator.getEnemigos()) {
            enemigo.draw(game.batch);
        }

        for (Item item : items) {
            item.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new PantallaGameOver(game));
            dispose();
        }

    }

    public boolean gameOver() {
        if (player.currentState == Mario.State.DEAD && player.getStateTimer() > 2) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
