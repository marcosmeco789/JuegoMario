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

public class Abeja extends Enemigo {
    private float stateTime;
    private Mario mario;
    private Animation<TextureRegion> flyAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private boolean setToDestroy;
    private boolean destroyed;
    private float inicioY;
    private float tiempo; // Controla la oscilación en Y
    private float amplitud = 0.3f; // Altura del vuelo en Y

    public Abeja(PantallaJugar screen, float x, float y) {
        super(screen, x, y);

        Array<TextureRegion> frames = new Array<>();
        this.inicioY = y;

        // Animación de vuelo
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 288, 16, 16));
        }
        flyAnimation = new Animation<>(0.1f, frames);
        frames.clear();

        // Animación de daño
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 304, 16, 16));
        }
        hurtAnimation = new Animation<>(0.08f, frames);
        frames.clear();

        stateTime = 0;
        setBounds(getX(), getY(), 16 / Main.PPM, 16 / Main.PPM);
        setToDestroy = false;
        destroyed = false;

        velocity = new Vector2(0.4f, 0); // Se mueve en X, la Y la controla con el vuelo senoidal
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        tiempo += dt * 2f; // Controla la frecuencia del vuelo en Y

        if (setToDestroy && !destroyed) {
            setRegion(hurtAnimation.getKeyFrame(stateTime, false));
            if (hurtAnimation.isAnimationFinished(stateTime)) {
                world.destroyBody(b2body);
                destroyed = true;
                stateTime = 0;
            }
        } else if (!destroyed) {
            float nuevaY = (float) (inicioY + Math.sin(tiempo) * amplitud); // Mantiene el vuelo oscilante
            b2body.setLinearVelocity(velocity.x, 0); // No cambia la velocidad en Y
            b2body.setTransform(b2body.getPosition().x + velocity.x * dt, nuevaY, 0); // Se mantiene en la trayectoria

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

        // Define the head fixture for the bee
        Vector2[] vertice = new Vector2[4];
        PolygonShape head = new PolygonShape();
        vertice[0] = new Vector2(-5, 8).scl(1 / Main.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / Main.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / Main.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / Main.PPM);
        head.set(vertice);

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
        b2body.setLinearVelocity(velocity.x, 0); // Evita cambios en Y tras la colisión
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

        // Removemos la colisión
        Filter filter = new Filter();
        filter.maskBits = Main.NOTHING_BIT;
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        // Mario rebota al pisar
        mario.b2body.applyLinearImpulse(new Vector2(0, 4.8f), mario.b2body.getWorldCenter(), true);
    }
}
