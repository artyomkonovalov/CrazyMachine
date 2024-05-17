package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen, RotateListener{
    private final MainGame game;
    private Stage stage;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Map<ThingTypeEnum, TextureRegion> textureMap;
    private Map<String, ThingTypeEnum> stringToThingTypeEnumMap;

//    private List<DragAndDropActor> elements = new ArrayList<>();

    private LinkedHashSet<DragAndDropActor> elements = new LinkedHashSet<>();
    private Viewport viewport;
    public final boolean isCreateLevelScreen;
    private FileHandle file;

    public DragAndDropActor isSelected;
    private Group buttons;
    private CheckBox needToWin;

    private CheckBox toInventory;

    //Констркутор для экрана создания уровня
    public FirstScreen(MainGame game, boolean isCreateLevelScreen){
        this.game = game;
        this.isCreateLevelScreen = isCreateLevelScreen;
    }
    //Конструктор для игрового экрана
    public FirstScreen(MainGame game, boolean isCreateLevelScreen, FileHandle file){
        this.game = game;
        this.isCreateLevelScreen = isCreateLevelScreen;
        this.file = file;
    }


    @Override
    public void show() {
        stringToThingTypeEnumMap = stringToThingTypeEnum();
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        textureMap = createTextureMap();

        BackgroundActor backgroundActor = new BackgroundActor(new Texture("Textures/background_gamearea.jpg"));
        stage.addActor(backgroundActor);

        InventoryActor inventoryActor = new InventoryActor(1040, 0, 240, 720, 180, 200);
        stage.addActor(inventoryActor);

        buttons = new RotateButtonsActor(this);
        stage.addActor(buttons);
        buttons.setVisible(false);

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

        if(isCreateLevelScreen){
            //Интерфейс для редактирования уровня
            ImageButton.ImageButtonStyle saveStyle = new ImageButton.ImageButtonStyle();
            saveStyle.up = new TextureRegionDrawable(new Texture("Textures/savebutton_normal(wood).png"));
            saveStyle.down = new TextureRegionDrawable(new Texture("Textures/savebutton_active(wood).png"));
            ImageButton saveButton = new ImageButton(saveStyle);
            stage.addActor(saveButton);
            saveButton.setPosition(MainGame.SCREEN_WIDTH-startButton.getWidth(), startButton.getHeight() + 10);
            saveButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    saveLevel();
                }
            });

            needToWin = new CheckBox("Need to win?", new Skin(Gdx.files.internal("ui/uiskin.json")));
            needToWin.setChecked(false);
            stage.addActor(needToWin);
            needToWin.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(isSelected != null){
                        isSelected.NeedToWin = ! isSelected.NeedToWin;
                    }
                }
            });

            toInventory = new CheckBox("To inventory?", new Skin(Gdx.files.internal("ui/uiskin.json")));
            toInventory.setChecked(false);
            stage.addActor(toInventory);
            toInventory.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(isSelected != null){
                        isSelected.toInventory = !isSelected.toInventory;
                    }
                }
            });

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
        }

        if(!isCreateLevelScreen){
            String data = file.readString();
            JsonValue root = new JsonReader().parse(data);
            for(JsonValue entry : root){
                DragAndDropActor actor = new DragAndDropActor(textureMap, stringToThingTypeEnumMap.get(entry.getString("actorType")), inventoryActor, elements, this, entry.get("isNeedToWin").asBoolean(), entry.get("toInventory").asBoolean());
                stage.addActor(actor);
                actor.setPosition(entry.get("x").asInt(), entry.get("y").asInt());
                actor.setRotation(entry.get("angle").asInt());
                if(actor.toInventory){
                    inventoryActor.addItem(actor);
                }
            }
        }
    }

    private Map<String, ThingTypeEnum> stringToThingTypeEnum(){
        HashMap<String, ThingTypeEnum> stringToThingTypeEnumMap = new HashMap<>();
        stringToThingTypeEnumMap.put("BALL", ThingTypeEnum.BALL);
        stringToThingTypeEnumMap.put("DESK", ThingTypeEnum.DESK);
        stringToThingTypeEnumMap.put("DOMINO", ThingTypeEnum.DOMINO);
        stringToThingTypeEnumMap.put("BALLOON", ThingTypeEnum.BALLOON);
        stringToThingTypeEnumMap.put("PUSHPIN", ThingTypeEnum.PUSHPIN);
        return stringToThingTypeEnumMap;
    }
    private void saveLevel(){
        Json json = new Json();
        ArrayList<Object> actors = new ArrayList<>();
        for(DragAndDropActor element:elements){
            SaveActor saveActor = new SaveActor(element.thingTypeEnum, element.getX(), element.getY(), element.getRotation(), element.NeedToWin, element.toInventory);
            actors.add(saveActor);
        }
        FileHandle directory = Gdx.files.local("levels/");
        String levelName = "level" + filesCount() + ".json";
        String data = json.prettyPrint(actors);
        System.out.println(data);
        directory.child(levelName).writeString(data, false);
    }
    private int filesCount(){
        int count = 0;
        FileHandle[] files = Gdx.files.local("levels/").list();
        for(FileHandle file: files) {
            ++count;
        }
        return count;
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
        if(isCreateLevelScreen){
            if(isSelected != null && isSelected.thingTypeEnum == ThingTypeEnum.BALL){
                needToWin.setVisible(isSelected.isVisible());
                needToWin.setPosition(isSelected.getX()+isSelected.getWidth(), isSelected.getY()+isSelected.getHeight());
                needToWin.setChecked(isSelected.NeedToWin);
            } else{
                needToWin.setVisible(false);
            }

            if(isSelected != null && isSelected.thingTypeEnum != ThingTypeEnum.PUSHPIN){
                toInventory.setVisible(isSelected.isVisible());
                toInventory.setPosition(isSelected.getX()+isSelected.getWidth(), isSelected.getY());
                toInventory.setChecked(isSelected.toInventory);
            } else{
                needToWin.setVisible(false);
            }
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
            normalTexture = new Texture("Textures/upbutton_normal(wood).png");
            activeTexture = new Texture("Textures/upbutton_active(wood).png");
        } else if(typeButton == "down"){
            normalTexture = new Texture("Textures/downbutton_normal(wood).png");
            activeTexture = new Texture("Textures/downbutton_active(wood).png");
        } else if(typeButton == "start"){
            normalTexture = new Texture("Textures/startButton_normal(wood).png");
            activeTexture = new Texture("Textures/startButton_active(wood).png");
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
//            isSelected.setRotation(isSelected.getRotation() + degree);
            isSelected.rotateBy(degree);
//            isSelected.setHeight((float) (isSelected.getHeight() + isSelected.getWidth()*Math.sin(isSelected.getRotation())));
//            isSelected.setWidth((float) (isSelected.getWidth()*Math.cos(isSelected.getRotation())));
        }
    }
}

