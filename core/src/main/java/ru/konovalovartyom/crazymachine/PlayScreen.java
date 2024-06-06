package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Set;

public class PlayScreen implements Screen {
    private MainGame game;
    private StartScreen startScreen;
    private FirstScreen firstScreen;
    private static final float PPM = 100;
    private int winCount = 0;
    private int finishedCount = 0;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private final Set<DragAndDropActor> elements;
    private Array<Body> bodies = new Array<>();
    private Stage stage;
    private Viewport viewport;
    private WinActor winActor;
    public static final float nextWidth = MainGame.SCREEN_WIDTH/(2*PPM);
    public static final float nextHeight = MainGame.SCREEN_HEIGHT/(2*PPM);

    public PlayScreen(Set<DragAndDropActor> elements, FirstScreen firstScreen, StartScreen startScreen, MainGame game) {
        this.elements = elements;
        this.startScreen = startScreen;
        this.game = game;
        this.firstScreen = firstScreen;
    }

    @Override
    public void show() {
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Box2D.init();
        world = new World(new Vector2(0, -9.8F), true);
        world.setContactListener(new MyContactListener());
//        debugRenderer = new Box2DDebugRenderer();
//        debugRenderer.setDrawVelocities(true);
//
//        camera = new OrthographicCamera(MainGame.SCREEN_WIDTH / PPM, MainGame.SCREEN_HEIGHT / PPM);
//        camera.position.set( MainGame.SCREEN_WIDTH / (2 * PPM), MainGame.SCREEN_HEIGHT / (2 * PPM), 0);
//        camera.update();

        BackgroundActor backgroundActor = new BackgroundActor(new Texture("Textures/background_gamearea.jpg"));
        stage.addActor(backgroundActor);

        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        Texture clockwiseNormal = new Texture("Textures/backbutton_normal(wood).png");
        Texture clockwiseActive = new Texture("Textures/backbutton_active(wood).png");
        backButtonStyle.up = new TextureRegionDrawable(clockwiseNormal);
        backButtonStyle.down = new TextureRegionDrawable(clockwiseActive);
        ImageButton backButton = new ImageButton(backButtonStyle);
        backButton.setPosition(0, MainGame.SCREEN_HEIGHT-backButton.getHeight());
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(firstScreen);
            }

        });
        stage.addActor(backButton);

        for(DragAndDropActor element: elements){
            createBodies(element);
            if(element.NeedToWin) ++winCount;
        }
        createFinishLine();
        winActor = new WinActor(startScreen, game);
        stage.addActor(winActor);
        winActor.setVisible(false);
    }

    private void createBodies(DragAndDropActor element){
        TextureActor textureActor = new TextureActor(element);
        stage.addActor(textureActor);
        switch (element.thingTypeEnum){
            case BALL -> {
                createBall(element, textureActor);
                break;
            }
            case DESK -> {
                createDesk(element, textureActor);
                break;
            }
            case BALLOON -> {
                createBalloon(element, textureActor);
                break;
            }
            case DOMINO -> {
                createDomino(element, textureActor);
                break;
            }
            case FAN -> {
                createFan(element, textureActor);
            }
            default -> {
                break;
            }
//            case PUSHPIN -> {
//                createFinishLine();
//                break;
//            }
        }
    }

    private void createBall(DragAndDropActor element, TextureActor textureActor){
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
        fixtureDef.restitution = 0.5F;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin, textureActor);
        body.setUserData(data);
        bodies.add(body);

        Fixture fixture = body.createFixture(fixtureDef);
        circleShape.dispose();
    }


    private void createDesk(DragAndDropActor element, TextureActor textureActor){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(element.getWidth()/(2*PPM), element.getHeight()/(2*PPM));

        body.setTransform((element.getX() + element.getWidth()/2)/PPM, (element.getY() + element.getHeight()/2)/PPM, element.getRotation()*3.14F/180);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.6F;
        fixtureDef.restitution = 0f;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin, textureActor);
        body.setUserData(data);
        bodies.add(body);

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    private void createBalloon(DragAndDropActor element, TextureActor textureActor){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);
        body.setTransform(element.getX()/PPM + element.getWidth()/(2*PPM), element.getY()/PPM + element.getHeight()/(2*PPM), 0F);
        body.setGravityScale(-0.5F);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(element.getWidth()/(2*PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.005F;
        fixtureDef.friction = 1F;
        fixtureDef.restitution = 0.1F;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin, textureActor);
        body.setUserData(data);
        bodies.add(body);

        Fixture fixture = body.createFixture(fixtureDef);
        circleShape.dispose();
    }

    private void createDomino(DragAndDropActor element, TextureActor textureActor){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(element.getWidth()/(2*PPM), element.getHeight()/(2*PPM));

        body.setTransform((element.getX() + element.getWidth()/2)/PPM, (element.getY() + element.getHeight()/2)/PPM, element.getRotation()*3.14F/180);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.6F;
        fixtureDef.restitution = 0f;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin, textureActor);
        body.setUserData(data);
        bodies.add(body);

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    private void createFan(DragAndDropActor element, TextureActor textureActor){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

//        Vector2[] vertices = new Vector2[]{
//            new Vector2(83/PPM, 19/PPM),
//            new Vector2(94/PPM, 100/PPM),
//            new Vector2(25/PPM, 103/PPM),
//            new Vector2(6/PPM, 95/PPM),
//            new Vector2(0/PPM, 123/PPM),
//            new Vector2(5/PPM, 147/PPM),
//            new Vector2(24/PPM, 139/PPM),
//            new Vector2(116/PPM, 148/PPM),
//            new Vector2(144/PPM, 148/PPM),
//            new Vector2(150/PPM, 128/PPM),
//            new Vector2(143/PPM, 100/PPM),
//            new Vector2(117/PPM, 90/PPM),
//            new Vector2(107/PPM, 26/PPM),
//            new Vector2(83/PPM, 19/PPM),
//        };
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(element.getWidth()/(4*PPM), element.getHeight()/(2.5F*PPM));
        body.setTransform((element.getX() + element.getWidth()/2)/PPM, (element.getY() + element.getHeight()/2)/PPM, element.getRotation()*3.14F/180);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.6F;
        fixtureDef.restitution = 0f;

        BodyData data = new BodyData(element.thingTypeEnum, element.NeedToWin, textureActor);
        body.setUserData(data);
        bodies.add(body);

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
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

            BodyData data = new BodyData(ThingTypeEnum.FINISH_LINE, false, null);
            body.setUserData(data);

            Fixture fixture = body.createFixture(fixtureDef);

            shape.dispose();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        debugRenderer.render(world, camera.combined);
        world.step(1/60F, 6, 2);
        for(Body body:bodies){
            BodyData bodyData = (BodyData) body.getUserData();
            TextureActor textureActor = bodyData.getTextureActor();
            if(bodyData.getTypeEnum() != ThingTypeEnum.BALLOON){
                textureActor.setRotation((float) Math.toDegrees(body.getAngle()));
            }
            textureActor.setPosition(body.getPosition().x*PPM-textureActor.getWidth()/2, body.getPosition().y*PPM-textureActor.getHeight()/2);
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
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
            if(finishedCount == winCount && winCount > 0){
                System.out.println("WIN");
                winActor.setVisible(true);
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
