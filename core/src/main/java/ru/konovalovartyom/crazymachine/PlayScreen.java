package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayScreen implements Screen {

    private static final float PPM = 100;
    private int winCount = 0;
    private int finishedCount = 0;
    //    float kx = MainGame.SCREEN_WIDTH / PPM;
//    float ky = MainGame.SCREEN_HEIGHT / PPM;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private final Set<DragAndDropActor> elements;

    public PlayScreen(Set<DragAndDropActor> elements) {
        this.elements = elements;
    }

    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0, -9.8F), true);
        world.setContactListener(new MyContactListener());
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);

        camera = new OrthographicCamera(MainGame.SCREEN_WIDTH / PPM, MainGame.SCREEN_HEIGHT / PPM);
        camera.position.set( MainGame.SCREEN_WIDTH / (2 * PPM), MainGame.SCREEN_HEIGHT / (2 * PPM), 0);
        camera.update();

        for(DragAndDropActor element: elements){
            createBodies(element);
            if(element.NeedToWin) ++winCount;
        }
        createFinishLine();
    }

    private void createBodies(DragAndDropActor element){
        switch (element.thingTypeEnum){
            case BALL -> {
                createBall(element);
                break;
            }
            case DESK -> {
                createDesk(element);
                break;
            }
            case BALLOON -> {
                createBalloon(element);
                break;
            }
//            case PUSHPIN -> {
//                createFinishLine();
//                break;
//            }
        }
    }

    private void createBall(DragAndDropActor element){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);
        body.setTransform(element.getX()/PPM + element.getWidth()/(2*PPM), element.getY()/PPM + element.getHeight()/(2*PPM), 0F);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(element.getWidth()/(2*PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.5F;
        fixtureDef.friction = 0.4F;
        fixtureDef.restitution = 1F;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin);
        body.setUserData(data);

        Fixture fixture = body.createFixture(fixtureDef);
        circleShape.dispose();
    }


    private void createDesk(DragAndDropActor element){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(element.getWidth()/(2*PPM), element.getHeight()/(2*PPM));

        body.setTransform((element.getX() + element.getWidth()/2)/PPM, (element.getY() + element.getHeight()/2)/PPM, element.getRotation()*3.14F/180);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4F;
        fixtureDef.restitution = 0f;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin);
        body.setUserData(data);

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    private void createBalloon(DragAndDropActor element){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);
        body.setTransform(element.getX()/PPM + element.getWidth()/(2*PPM), element.getY()/PPM + element.getHeight()/(2*PPM), 0F);
        body.applyForceToCenter(0, 10, true);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(element.getWidth()/(2*PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.05F;
        fixtureDef.friction = 0.5F;
        fixtureDef.restitution = 0.1F;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin);
        body.setUserData(data);

        Fixture fixture = body.createFixture(fixtureDef);
        circleShape.dispose();
    }

    private void createFinishLine(){
        int count = 0;
        for(DragAndDropActor element:elements){
            if(element.thingTypeEnum == ThingTypeEnum.PUSHPIN) ++count;
        }
        if(count > 1){
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            Body body = world.createBody(bodyDef);

            ChainShape shape = new ChainShape();

            ArrayList<Vector2> vert = new ArrayList<>();
            for(DragAndDropActor element:elements){
                if(element.thingTypeEnum == ThingTypeEnum.PUSHPIN){
                    vert.add(new Vector2(element.getX()/PPM, element.getY()/PPM));
                }
            }
            Vector2[] vertices = vert.toArray(new Vector2[0]);
            shape.createChain(vertices);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            body.setUserData(ThingTypeEnum.FINISH_LINE);

            BodyData data = new BodyData(ThingTypeEnum.FINISH_LINE, false);
            body.setUserData(data);

            Fixture fixture = body.createFixture(fixtureDef);

            shape.dispose();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, camera.combined);
        world.step(1/60F, 6, 2);
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
            BodyData bodyAType = (BodyData) contact.getFixtureA().getBody().getUserData();
            BodyData bodyBType = (BodyData) contact.getFixtureB().getBody().getUserData();


            if(bodyAType.getTypeEnum() == ThingTypeEnum.FINISH_LINE && bodyBType.isNeedToWin()){
                ++finishedCount;
            }
        }

        @Override
        public void endContact(Contact contact) {
            if(finishedCount == winCount){
                System.out.println("WIN");
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    }
}
