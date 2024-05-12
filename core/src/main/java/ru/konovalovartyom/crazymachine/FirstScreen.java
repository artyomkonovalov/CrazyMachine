package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen, RotateListener{
    private final MainGame game;
    private Stage stage;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Map<ThingTypeEnum, TextureRegion> textureMap;

//    private List<DragAndDropActor> elements = new ArrayList<>();

    private LinkedHashSet<DragAndDropActor> elements = new LinkedHashSet<>();
    private Viewport viewport;

    public FirstScreen(MainGame game){
        this.game = game;
    }
    public DragAndDropActor isSelected;
    private Group buttons;
    private CheckBox checkBox;

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

        List<DragAndDropActor> list = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            DragAndDropActor ball = new DragAndDropActor(textureMap, ThingTypeEnum.BALL, inventoryActor, elements, this);
            stage.addActor(ball);
            list.add(ball);
        }
        for(int i = 0; i < 3; ++i){
            DragAndDropActor deskActor = new DragAndDropActor(textureMap, ThingTypeEnum.DESK, inventoryActor, elements, this);
            stage.addActor(deskActor);
            inventoryActor.addItem(deskActor);
        }

        DragAndDropActor balloon = new DragAndDropActor(textureMap, ThingTypeEnum.BALLOON, inventoryActor, elements, this);
        stage.addActor(balloon);
        inventoryActor.addItem(balloon);

        inventoryActor.addItems(list);
        for(int i = 0; i < 3; ++i){
            DragAndDropActor pushpin = new DragAndDropActor(textureMap, ThingTypeEnum.PUSHPIN, inventoryActor, elements, this);
            stage.addActor(pushpin);
            inventoryActor.addItem(pushpin);
        }

        buttons = new RotateButtonsActor(this);
        stage.addActor(buttons);
        buttons.setVisible(false);

        checkBox = new CheckBox("Need to win?", new Skin(Gdx.files.internal("ui/uiskin.json")));
        checkBox.setChecked(false);
        stage.addActor(checkBox);
        checkBox.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(isSelected != null){
                    isSelected.NeedToWin = ! isSelected.NeedToWin;
                }
            }
        });
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
                dispose();
            }
        });
    }

    private Map<ThingTypeEnum, TextureRegion> createTextureMap(){
        EnumMap<ThingTypeEnum, TextureRegion> textureMap = new EnumMap<>(ThingTypeEnum.class);
        //HashMap<ThingTypeEnum, TextureRegion> textureMap = new HashMap<>();
        textureMap.put(ThingTypeEnum.BALL, new TextureRegion(new Texture("Textures/football.png")));
        textureMap.put(ThingTypeEnum.DESK, new TextureRegion(new Texture("Textures/desk1.png")));
        textureMap.put(ThingTypeEnum.PUSHPIN, new TextureRegion(new Texture("Textures/pushpin.png")));
        textureMap.put(ThingTypeEnum.BALLOON, new TextureRegion(new Texture("Textures/balloon.png")));
        return textureMap;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        if(isSelected != null && isSelected.thingTypeEnum == ThingTypeEnum.DESK){
            buttons.setVisible(isSelected.isVisible());
            buttons.setPosition(isSelected.getX()-buttons.getWidth()-5, isSelected.getY());
        } else{
            buttons.setVisible(false);
        }
        if(isSelected != null && isSelected.thingTypeEnum == ThingTypeEnum.BALL){
            checkBox.setVisible(isSelected.isVisible());
            checkBox.setPosition(isSelected.getX()+isSelected.getWidth(), isSelected.getY()+isSelected.getHeight());
            checkBox.setChecked(isSelected.NeedToWin);
        } else{
            checkBox.setVisible(false);
        }
        stage.act();
        stage.draw();


        DragAndDropActor prev = null;
        for(DragAndDropActor element:elements){
            if(element.thingTypeEnum == ThingTypeEnum.PUSHPIN){
                if(prev != null){
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(1, 0, 0, 1); // Red line
                    shapeRenderer.rectLine(prev.getX(), prev.getY(), element.getX(), element.getY(), 10);
                    shapeRenderer.end();
                }
                prev = element;
            }
        }
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
        stage.dispose();
    }

    @Override
    public void rotate(float degree) {
        if (isSelected != null){
            isSelected.setRotation(isSelected.getRotation() + degree);
        }
    }
}

