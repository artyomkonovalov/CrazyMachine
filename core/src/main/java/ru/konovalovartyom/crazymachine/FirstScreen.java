package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    private MainGame game = new MainGame();
    private Stage stage;

    private Map<ThingTypeEnum, TextureRegion> textureMap;

    private ArrayList<DragAndDropActor> elements = new ArrayList<>();

    private Viewport viewport;
    @Override
    public void show() {
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        textureMap = createTextureMap();

        BackgroundActor backgroundActor = new BackgroundActor();
        stage.addActor(backgroundActor);

        InventoryActor inventoryActor = new InventoryActor(1040, 0, 240, 720, 200, 200);
        stage.addActor(inventoryActor);


        /**GrabAndDropActor ball = new GrabAndDropActor(new TextureRegion(new Texture("Textures/football.png")));
         stage.addActor(ball); */

        ImageButton upButton = createButton("up");
        upButton.setHeight(upButton.getHeight()/1.5F);
        upButton.setPosition(inventoryActor.getX() + (inventoryActor.getWidth()-upButton.getWidth())/2, inventoryActor.getHeight()-upButton.getHeight());
        stage.addActor(upButton);
        upButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventoryActor.toUp();
            }
        });

        ImageButton downButton = createButton("down");
        downButton.setHeight(downButton.getHeight()/1.5F);
        downButton.setPosition(inventoryActor.getX() + (inventoryActor.getWidth()-downButton.getWidth())/2, 0);
        stage.addActor(downButton);
        downButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventoryActor.toDown();
            }
        });

        ImageButton startButton = createButton("start");
        startButton.setPosition(MainGame.SCREEN_WIDTH-startButton.getWidth(), 0);
        stage.addActor(startButton);
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen playScreen = new PlayScreen(elements);
                game.setScreen(playScreen);
                stage.dispose();
            }
        });

        List<DragAndDropActor> list = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            DragAndDropActor ball = new DragAndDropActor(textureMap, ThingTypeEnum.BALL, inventoryActor);
            stage.addActor(ball);
            list.add(ball);
            stage.addActor(ball);
        }
        inventoryActor.addItems(list);
    }

    private Map<ThingTypeEnum, TextureRegion> createTextureMap(){
        EnumMap<ThingTypeEnum, TextureRegion> textureMap = new EnumMap<>(ThingTypeEnum.class);
        //HashMap<ThingTypeEnum, TextureRegion> textureMap = new HashMap<>();
        textureMap.put(ThingTypeEnum.BALL, new TextureRegion(new Texture("Textures/football.png")));
        return textureMap;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act();
        stage.draw();
    }

    private ImageButton createButton(String typeButton){
        Texture normalTexture = null;
        Texture activeTexture = null;

        if(typeButton == "up"){
            normalTexture = new Texture("Textures/upbutton_normal.png");
            activeTexture = new Texture("Textures/upbutton_active.png");
        } else if(typeButton == "down"){
            normalTexture = new Texture("Textures/downbutton_normal.png");
            activeTexture = new Texture("Textures/downbutton_active.png");
        } else if(typeButton == "start"){
            normalTexture = new Texture("Textures/startButton_normal.png");
            activeTexture = new Texture("Textures/startButton_active.png");
        }

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(normalTexture);
        style.down = new TextureRegionDrawable(activeTexture);
        ImageButton button = new ImageButton(style);

        return button;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
