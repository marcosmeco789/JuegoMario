package com.marcos.mario.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.marcos.mario.Main;
import com.marcos.mario.Sprites.Enemies.Enemigo;
import com.marcos.mario.Sprites.TileObjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }
        switch (cDef){
            case Main.ENEMY_HEAD_BIT | Main.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_HEAD_BIT)
                    ((Enemigo) fixA.getUserData()).hitOnHead();
                else if(fixB.getFilterData().categoryBits == Main.ENEMY_HEAD_BIT){

                    ((Enemigo) fixB.getUserData()).hitOnHead();
                }
                break;

            case Main.ENEMY_BIT | Main.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT)
                    ((Enemigo) fixA.getUserData()).reverseVelocity(true, false);
                else if(fixB.getFilterData().categoryBits == Main.ENEMY_BIT)
                    ((Enemigo) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case Main.MARIO_BIT | Main.ENEMY_BIT:

                break;

            case Main.ENEMY_BIT | Main.ENEMY_BIT:
                ((Enemigo) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemigo) fixB.getUserData()).reverseVelocity(true, false);

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
