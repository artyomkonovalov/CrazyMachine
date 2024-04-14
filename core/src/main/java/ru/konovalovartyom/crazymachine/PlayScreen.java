package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    public PlayScreen(ArrayList<DragAndDropActor> elements) {

    }

    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0, -9.8F), true);
        world.setContactListener(new MyContactListener());
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera(240, 160);
        camera.position.set(0, 80, 0);

    }

    @Override
    public void render(float delta) {

        debugRenderer.render(world, camera.combined);
        world.step(1/60, 6, 2);
    }

    @Override
    public void resize(int width, int height) {

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

    }

    private class MyContactListener implements ContactListener {
        @Override
        public void beginContact(Contact contact) {

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
}
