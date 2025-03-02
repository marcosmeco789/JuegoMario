package com.marcos.mario.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.marcos.mario.Main;
import com.marcos.mario.Screens.PantallaJugar;
import com.marcos.mario.Sprites.Enemies.Enemigo;
import com.marcos.mario.Sprites.Enemies.Turtle;

public class Mario extends Sprite {
    public boolean isJumping;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private Animation marioRun;
    private Animation marioIdle;
    private Animation marioJumpAnim;
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;
    private boolean onLadder;
    private boolean nearLadder;
    private PantallaJugar screen;

    public Mario(PantallaJugar screen) {
        super(screen.getAtlas().findRegion("idle")); // Imagen inicial
        this.screen = screen;

        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();

        // Animación de Idle
        for (int i = 0; i < 12; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("idle"), i * 19, 0, 19, 34));
        }
        marioIdle = new Animation<>(0.1f, frames);
        frames.clear();

        // Animación de Correr
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("run"), i * 21, 0, 21, 33));
        }
        marioRun = new Animation<>(0.1f, frames);
        frames.clear();

        // Animación de Salto
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("full jump animation"), i * 20, 0, 20, 36));
        }
        marioJumpAnim = new Animation<>(0.1f, frames);
        frames.clear();

        // Definir otras texturas necesarias
        marioStand = new TextureRegion(screen.getAtlas().findRegion("idle"), 0, 0, 21, 33);
        marioJump = new TextureRegion(screen.getAtlas().findRegion("full jump animation"), 0, 0, 21, 33);
        marioDead = new TextureRegion(screen.getAtlas().findRegion("idle"), 0, 0, 21, 33); // Si hay una imagen de muerte, cámbiala

        defineMario();
        setBounds(0, 0, 21 / Main.PPM, 33 / Main.PPM);
        setRegion(marioStand);
    }


    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;
    }

    public boolean isOnLadder() {
        return onLadder;
    }

    public void setNearLadder(boolean nearLadder) {
        this.nearLadder = nearLadder;
    }

    public boolean isNearLadder() {
        return nearLadder;
    }


    public void update(float dt) {
        float textureOffsetY = 10 / Main.PPM; // Ajusta este valor según sea necesario

        if (marioIsBig) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / Main.PPM + textureOffsetY);
        } else {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + textureOffsetY);
        }

        setRegion(getFrame(dt));

        if (timeToDefineBigMario) {
            defineBigMario();
        }
        if (timeToRedefineMario) {
            redefineMario();
        }


        if (onLadder) {
            b2body.setLinearVelocity(new Vector2(0, 1)); // Adjust the speed as needed
        }
    }

    public boolean isBig() {
        return marioIsBig;
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {

            case DEAD:
                region = marioDead;
                break;

            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = (TextureRegion) marioJumpAnim.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = (TextureRegion) marioIdle.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (marioIsDead)
            return State.DEAD;
        else if (runGrowAnimation) {
            return State.GROWING;
        } else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void grow() {

        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        Main.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();

    }

    public boolean isDead() {
        return marioIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void hit(Enemigo enemigo) {
        // If the method is called with a null enemy (e.g., "Muerte" collision), kill Mario
        if (enemigo == null) {
            if (!marioIsDead) {
                Main.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
                marioIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = Main.NOTHING_BIT;
                for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }
            return;
        }

        // Existing turtle shell or enemy logic
        if (enemigo instanceof Turtle && ((Turtle) enemigo).getCurrentState() == Turtle.State.STANDING_SHELL) {
            ((Turtle) enemigo).kick(this.getX() <= enemigo.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        } else {
            if (marioIsBig) {
                marioIsBig = false;
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                Main.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                Main.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
                marioIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = Main.NOTHING_BIT;
                for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }
        }

    }


    public void redefineMario() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Main.PPM);
        fdef.filter.categoryBits = Main.MARIO_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT |
            Main.COIN_BIT |
            Main.BRICK_BIT |
            Main.ENEMY_BIT |
            Main.OBJECT_BIT |
            Main.ENEMY_HEAD_BIT |
            Main.ITEM_BIT |
            Main.ESCALERA_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-1.6f / Main.PPM, 6 / Main.PPM), new Vector2(1.6f / Main.PPM, 6 / Main.PPM));
        fdef.filter.categoryBits = Main.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToRedefineMario = false;
    }


    public void defineBigMario() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / Main.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Main.PPM);
        fdef.filter.categoryBits = Main.MARIO_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT |
            Main.COIN_BIT |
            Main.BRICK_BIT |
            Main.ENEMY_BIT |
            Main.OBJECT_BIT |
            Main.ENEMY_HEAD_BIT |
            Main.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / Main.PPM));
        b2body.createFixture(fdef).setUserData(this);


        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-1.6f / Main.PPM, 6 / Main.PPM), new Vector2(1.6f / Main.PPM, 6 / Main.PPM));
        fdef.filter.categoryBits = Main.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;

    }

    public boolean isPressingDown() {
        Vector2 direction = screen.getJoystick().getKnobPercentage();
        return direction.y < -0.5f; // Adjust the threshold as needed
    }


    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(172 / Main.PPM, 172 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Main.PPM);
        fdef.filter.categoryBits = Main.MARIO_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT |
            Main.COIN_BIT |
            Main.BRICK_BIT |
            Main.ENEMY_BIT |
            Main.OBJECT_BIT |
            Main.ENEMY_HEAD_BIT |
            Main.ITEM_BIT |
            Main.ESCALERA_BIT |
            Main.TECHO_ESCALERA_BIT |
            Main.MUERTE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-1.6f / Main.PPM, 6 / Main.PPM), new Vector2(1.6f / Main.PPM, 6 / Main.PPM));
        fdef.filter.categoryBits = Main.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }
}
