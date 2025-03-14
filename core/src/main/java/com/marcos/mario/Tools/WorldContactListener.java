package com.marcos.mario.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.marcos.mario.Main;
import com.marcos.mario.Sprites.Enemies.Enemigo;
import com.marcos.mario.Sprites.Enemies.Goomba;
import com.marcos.mario.Sprites.Enemies.Pinchos;
import com.marcos.mario.Sprites.Items.Item;
import com.marcos.mario.Sprites.Mario;
import com.marcos.mario.Sprites.TileObjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        if (fixA.getUserData() instanceof Pinchos || fixB.getUserData() instanceof Pinchos) {
            Fixture pinchosFixture = fixA.getUserData() instanceof Pinchos ? fixA : fixB;
            Fixture otherFixture = pinchosFixture == fixA ? fixB : fixA;

            if (otherFixture.getUserData() instanceof Mario) {
                ((Mario) otherFixture.getUserData()).hit(null); // Call the hit method on Mario
            }
        }


        if ("final".equals(fixA.getUserData()) || "final".equals(fixB.getUserData())) {
            Fixture finalFixture = "final".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture otherFixture = finalFixture == fixA ? fixB : fixA;

            if (otherFixture.getUserData() instanceof Mario) {
                ((Mario) otherFixture.getUserData()).setHasReachedFinal(true);
            }
        }


        if ("escalera".equals(fixA.getUserData()) || "escalera".equals(fixB.getUserData())) {
            Fixture escaleraFixture = "escalera".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture otherFixture = (escaleraFixture == fixA) ? fixB : fixA;

            if (otherFixture.getUserData() instanceof Mario) {

                ((Mario) otherFixture.getUserData()).setNearLadder(true);
            }
        }

        if ("techoEscalera".equals(fixA.getUserData()) || "techoEscalera".equals(fixB.getUserData())) {
            Fixture techoEscaleraFixture = "techoEscalera".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture otherFixture = techoEscaleraFixture == fixA ? fixB : fixA;

            if (otherFixture.getUserData() instanceof Mario) {
                Gdx.app.log("Ceiling Ladder", "Mario hit the ceiling of the ladder");
                ((Mario) otherFixture.getUserData()).setOnLadder(false);
            }
        }

        if ("muerte".equals(fixA.getUserData()) || "muerte".equals(fixB.getUserData())) {
            Fixture muerteFixture = "muerte".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture otherFixture = (muerteFixture == fixA) ? fixB : fixA;

            if (otherFixture.getUserData() instanceof Mario) {
                ((Mario) otherFixture.getUserData()).hit(null); // Llama al método hit de Mario
            }
        }


        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case Main.MARIO_HEAD_BIT | Main.BRICK_BIT:
            case Main.MARIO_HEAD_BIT | Main.COIN_BIT:
                if(fixA.getFilterData().categoryBits == Main.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case Main.ENEMY_HEAD_BIT | Main.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_HEAD_BIT)
                    ((Enemigo) fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                else {
                    ((Enemigo) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                }
                break;

            case Main.ENEMY_BIT | Main.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT)
                    ((Enemigo) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemigo) fixB.getUserData()).reverseVelocity(true, false);
                break;

                case Main.MARIO_BIT | Main.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Main.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit((Enemigo) fixB.getUserData());
                else
                    ((Mario) fixB.getUserData()).hit((Enemigo) fixA.getUserData());
                break;

            case Main.ENEMY_BIT | Main.ENEMY_BIT:
                ((Enemigo) fixA.getUserData()).onEnemyHit((Enemigo) fixB.getUserData());
                ((Enemigo) fixB.getUserData()).onEnemyHit((Enemigo) fixA.getUserData());

                break;

            case Main.ITEM_BIT | Main.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Main.ITEM_BIT)
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case Main.ITEM_BIT | Main.MARIO_BIT:
                System.out.println("Collision");
                if(fixA.getFilterData().categoryBits == Main.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ("escalera".equals(fixA.getUserData()) || "escalera".equals(fixB.getUserData())) {
            Fixture escaleraFixture = "escalera".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture otherFixture = escaleraFixture == fixA ? fixB : fixA;

            if (otherFixture.getUserData() instanceof Mario) {
                ((Mario) otherFixture.getUserData()).setNearLadder(false);
                ((Mario) otherFixture.getUserData()).setOnLadder(false);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ("techoEscalera".equals(fixA.getUserData()) || "techoEscalera".equals(fixB.getUserData())) {
            Fixture escaleraFixture = "techoEscalera".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture otherFixture = (escaleraFixture == fixA) ? fixB : fixA;
            if (otherFixture.getUserData() instanceof Mario) {
                Mario mario = (Mario) otherFixture.getUserData();
                boolean marioBelow = mario.b2body.getPosition().y < escaleraFixture.getBody().getPosition().y;
                boolean pressingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN) || mario.isPressingDown(); // Check for joystick down input

                if (marioBelow && !pressingDown) {
                    contact.setEnabled(false);
                } else if (!marioBelow && pressingDown) {
                    contact.setEnabled(false);
                } else {
                    contact.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
