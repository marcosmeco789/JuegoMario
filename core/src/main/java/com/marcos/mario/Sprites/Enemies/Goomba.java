package com.marcos.mario.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.marcos.mario.Main;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Mario;

public class Goomba extends Enemigo {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> hurtAnimation;

    public Goomba(PantallaJugar screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<>();

        // Idle animation
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), 15, 143 + i * 16, 16, 16));
        }
        idleAnimation = new Animation<>(0.1f, frames);
        frames.clear();

        // Walk animation
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 32, 16, 16));
        }
        walkAnimation = new Animation<>(0.1f, frames);
        frames.clear();

        // Hurt animation
        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 48, 16, 16));
        }
        hurtAnimation = new Animation<>(0.08f, frames);
        frames.clear();

        stateTime = 0;
        setBounds(getX(), getY(), 16 / Main.PPM, 16 / Main.PPM);
        setToDestroy = false;
        destroyed = false;
    }

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
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
        }
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        if (setToDestroy && !destroyed) {
            region = hurtAnimation.getKeyFrame(stateTime, false);
        } else if (b2body.getLinearVelocity().x != 0) {
            region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
        } else {
            region = idleAnimation.getKeyFrame(stateTime, true);
        }

        // Flip the texture based on the direction of the velocity
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

        // Goomba head
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / Main.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / Main.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / Main.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / Main.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Main.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }

    public void onEnemyHit(Enemigo enemigo) {
        if (enemigo instanceof Turtle && ((Turtle) enemigo).getCurrentState() == Turtle.State.MOVING_SHELL) {
            setToDestroy = true;
        } else {
            reverseVelocity(true, false);
        }
    }

    @Override
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
            setFlip(!isFlipX(), false); // Flip the texture horizontally
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }

    @Override
    public void hitOnHead(Mario mario) {
        setToDestroy = true;
        stateTime = 0; // Reset the state time to start the hurt animation from the beginning
        Main.manager.get("audio/sounds/stomp.wav", Sound.class).play();

        // Remove collision fixture
        Filter filter = new Filter();
        filter.maskBits = Main.NOTHING_BIT;
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        // Apply a small jump to Mario
        mario.b2body.applyLinearImpulse(new Vector2(0, 4.8f), mario.b2body.getWorldCenter(), true);
    }
}
