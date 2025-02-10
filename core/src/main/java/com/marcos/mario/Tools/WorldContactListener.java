package com.marcos.mario.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.marcos.mario.Main;
import com.marcos.mario.Sprites.Enemies.Enemigo;
import com.marcos.mario.Sprites.Items.Item;
import com.marcos.mario.Sprites.Mario;
import com.marcos.mario.Sprites.TileObjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

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
                    ((Enemigo) fixB.getUserData()).hitOnHead(Mario) fixA.getUserData());
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
                    ((Mario) fixA.getUserData()).hit();
                else
                    ((Mario) fixB.getUserData()).hit();
                break;

            case Main.ENEMY_BIT | Main.ENEMY_BIT:
                ((Enemigo) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemigo) fixB.getUserData()).reverseVelocity(true, false);
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

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
