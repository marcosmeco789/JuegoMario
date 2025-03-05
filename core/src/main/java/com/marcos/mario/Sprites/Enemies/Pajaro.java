package com.marcos.mario.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.marcos.mario.Main;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Mario;

public class Pajaro extends Enemigo {
    private float stateTime;
    private Animation<TextureRegion> flyAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private boolean setToDestroy;
    private boolean destroyed;

    public Pajaro(PantallaJugar screen, float x, float y) {
        super(screen, x, y);
        Array<TextureRegion> frames = new Array<>();

        // Animación de vuelo
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 352, 16, 16));
        }
        flyAnimation = new Animation<>(0.1f, frames);
        frames.clear();

        // Animación de daño
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 368, 16, 16));
        }
        hurtAnimation = new Animation<>(0.08f, frames);
        frames.clear();

        stateTime = 0;
        setBounds(getX(), getY(), 16 / Main.PPM, 16 / Main.PPM);
        setToDestroy = false;
        destroyed = false;

        velocity = new Vector2(0.4f, 0.165f); // Se mueve en X y en Y de forma constante
    }

    @Override
    public void update(float dt) {
        stateTime += dt;

        if (setToDestroy && !destroyed) {
            setRegion(hurtAnimation.getKeyFrame(stateTime, false));
            if (hurtAnimation.isAnimationFinished(stateTime)) {
                world.destroyBody(b2body);
                destroyed = true;
                stateTime = 0;
            }
        } else if (!destroyed) {
            b2body.setLinearVelocity(velocity); // Movimiento constante en X y Y
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
        }
    }


    private TextureRegion getFrame(float dt) {
        TextureRegion region;

        if (setToDestroy && !destroyed) {
            region = hurtAnimation.getKeyFrame(stateTime, false);
        } else {
            region = flyAnimation.getKeyFrame(stateTime, true);
        }

        // Flip de la textura si cambia la dirección
        if ((velocity.x < 0 && !region.isFlipX()) || (velocity.x > 0 && region.isFlipX())) {
            region.flip(true, false);
        }

        return region;
    }

    @Override
    protected void defineEnemigo() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Main.PPM);
        fdef.filter.categoryBits = Main.ENEMY_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT | Main.COIN_BIT | Main.BRICK_BIT | Main.ENEMY_BIT | Main.OBJECT_BIT | Main.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Definir fixture para la cabeza
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1 / Main.PPM);
        vertices[1] = new Vector2(5, 8).scl(1 / Main.PPM);
        vertices[2] = new Vector2(-3, 3).scl(1 / Main.PPM);
        vertices[3] = new Vector2(3, 3).scl(1 / Main.PPM);
        head.set(vertices);

        fdef.shape = head;
        fdef.restitution = 1.5f;
        fdef.filter.categoryBits = Main.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void onEnemyHit(Enemigo enemigo) {
        reverseVelocity(true, false);
    }

    @Override
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
            setFlip(velocity.x < 0, false); // Invierte la textura según la dirección
        }
    }

    @Override
    public void hitOnHead(Mario mario) {
        setToDestroy = true;
        stateTime = 0;
        Main.manager.get("audio/sounds/stomp.wav", Sound.class).play();

        // Deshabilita la colisión
        Filter filter = new Filter();
        filter.maskBits = Main.NOTHING_BIT;
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        // Mario rebota al pisar
        mario.b2body.applyLinearImpulse(new Vector2(0, 4.8f), mario.b2body.getWorldCenter(), true);
    }
}
