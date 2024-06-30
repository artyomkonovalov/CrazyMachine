package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen, RotateListener{
    private StartScreen startScreen;
    private final MainGame game;
    public Stage stage;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Map<ThingTypeEnum, TextureRegion> textureMap;
    private Map<String, ThingTypeEnum> stringToThingTypeEnumMap;
    private InventoryActor inventoryActor;

//    private List<DragAndDropActor> elements = new ArrayList<>();

    private LinkedHashSet<DragAndDropActor> elements = new LinkedHashSet<>();
    private Viewport viewport;
    public final boolean isCreateLevelScreen;
    private FileHandle file;

    public DragAndDropActor isSelected;
    private Group buttons;
    private CheckBox needToWin;

    private CheckBox toInventory;
    private TextArea task;
    private String textTask;
    private FileHandle fontFile;
    private BitmapFont font;

    //Констркутор для экрана создания уровня
    public FirstScreen(StartScreen startScreen, MainGame game, boolean isCreateLevelScreen){
        this.startScreen = startScreen;
        this.game = game;
        this.isCreateLevelScreen = isCreateLevelScreen;
        fontFile = Gdx.files.internal("Textures/vag-world-bold.fnt");
        font = new BitmapFont(fontFile);
        font.setColor(0, 0, 0, 1);
    }
    //Конструктор для игрового экрана
    public FirstScreen(StartScreen startScreen, MainGame game, boolean isCreateLevelScreen, FileHandle file){
        this.startScreen = startScreen;
        this.game = game;
        this.isCreateLevelScreen = isCreateLevelScreen;
        this.file = file;
        fontFile = Gdx.files.internal("Textures/vag-world-bold.fnt");
        font = new BitmapFont(fontFile);
        font.setColor(0, 0, 0, 1);
    }


    @Override
    public void show() {
        stringToThingTypeEnumMap = stringToThingTypeEnum();
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        elements.clear();

        textureMap = createTextureMap();

        BackgroundActor backgroundActor = new BackgroundActor(new Texture("Textures/background_gamearea.png"));
        stage.addActor(backgroundActor);

        inventoryActor = new InventoryActor(1040, 0, 240, 720, 180, 200);
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
                PlayScreen playScreen = new PlayScreen(elements, inventoryActor, FirstScreen.this, startScreen, game);
                game.setScreen(playScreen);
                dispose();
            }
        });

        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        Texture backButtonNormal = new Texture("Textures/backbutton_normal(wood).png");
        Texture backButtonActive = new Texture("Textures/backbutton_active(wood).png");
        backButtonStyle.up = new TextureRegionDrawable(backButtonNormal);
        backButtonStyle.down = new TextureRegionDrawable(backButtonActive);
        ImageButton backButton = new ImageButton(backButtonStyle);
        backButton.setPosition(0, MainGame.SCREEN_HEIGHT-backButton.getHeight());
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(startScreen);
            }

        });
        stage.addActor(backButton);
        if(isCreateLevelScreen){
            //Интерфейс для редактирования уровня

            TextureRegionDrawable checkboxOff = new TextureRegionDrawable(new Texture("Textures/checkbox_up.png"));
            TextureRegionDrawable checkboxOn = new TextureRegionDrawable(new Texture("Textures/checkbox_down.png"));
            CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(checkboxOff, checkboxOn, font, new Color(0, 0, 0, 1));
            //new Skin(Gdx.files.internal("ui/uiskin.json"))
            needToWin = new CheckBox("Need to win?", checkBoxStyle);
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
            //new Skin(Gdx.files.internal("ui/uiskin.json"))
            toInventory = new CheckBox("To inventory?", checkBoxStyle);
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

            ImageButton.ImageButtonStyle showTextAreaStyle = new ImageButton.ImageButtonStyle();
            showTextAreaStyle.up = new TextureRegionDrawable(new Texture("Textures/showTextButton_normal.png"));
            showTextAreaStyle.down = new TextureRegionDrawable(new Texture("Textures/showTextButton_active.png"));
            ImageButton showTextArea = new ImageButton(showTextAreaStyle);
            showTextArea.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    task.setVisible(!task.isVisible());
                }
            });
            showTextArea.setPosition(backButton.getX(), backButton.getY()-showTextArea.getHeight()-5);
            stage.addActor(showTextArea);

            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            Texture backgroundField = new Texture("Textures/textBox.png");
            textFieldStyle.background = new TextureRegionDrawable(backgroundField);
            textFieldStyle.font = font;
            textFieldStyle.fontColor = new Color(0, 0, 0, 1);

            task = new TextArea("Задание уровня", textFieldStyle);
            task.setWidth(backgroundField.getWidth());
            task.setHeight(backgroundField.getHeight());
            task.setPosition(showTextArea.getX() + showTextArea.getWidth() + 5, MainGame.SCREEN_HEIGHT-task.getHeight());
            stage.addActor(task);
            task.setVisible(false);

            for(ThingTypeEnum elementType:ThingTypeEnum.values()){
                if(elementType != ThingTypeEnum.FINISH_LINE && elementType != ThingTypeEnum.AIRBALL && elementType != ThingTypeEnum.TEXTFIELD) {
                    DragAndDropActor currentElement = new DragAndDropActor(textureMap, elementType, inventoryActor, elements, this);
                    inventoryActor.addItem(currentElement);
                    stage.addActor(currentElement);
                }
            }
        }

        if(!isCreateLevelScreen){
            String data = file.readString();
            JsonValue root = new JsonReader().parse(data);
            for(JsonValue entry : root){
                if(entry.getString("task") != null) {
                    textTask = new String(entry.getString("task").getBytes(), StandardCharsets.UTF_8) ;
                }
                else{
                    DragAndDropActor actor = new DragAndDropActor(textureMap, stringToThingTypeEnumMap.get(entry.getString("actorType")), inventoryActor, elements, this, entry.get("isNeedToWin").asBoolean(), entry.get("toInventory").asBoolean());
                    stage.addActor(actor);
                    actor.setPosition(entry.get("x").asInt(), entry.get("y").asInt());
                    if(actor.toInventory){
                        inventoryActor.addItem(actor);
                    }
                    else{
                        elements.add(actor);
                        actor.rotation = entry.get("angle").asInt();
                    }
                }
            }
            ImageButton.ImageButtonStyle showTextAreaStyle = new ImageButton.ImageButtonStyle();
            showTextAreaStyle.up = new TextureRegionDrawable(new Texture("Textures/showTextButton_normal.png"));
            showTextAreaStyle.down = new TextureRegionDrawable(new Texture("Textures/showTextButton_active.png"));
            ImageButton showTextArea = new ImageButton(showTextAreaStyle);
            showTextArea.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    task.setVisible(!task.isVisible());
                }
            });

            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            Texture backgroundField = new Texture("Textures/textBox.png");
            textFieldStyle.background = new TextureRegionDrawable(backgroundField);
            textFieldStyle.font = font;
            textFieldStyle.fontColor = new Color(0, 0, 0, 1);

            task = new TextArea(textTask, textFieldStyle);
            task.setWidth(backgroundField.getWidth());
            task.setHeight(backgroundField.getHeight());
            task.setPosition(showTextArea.getX() + showTextArea.getWidth() + 5, MainGame.SCREEN_HEIGHT-task.getHeight());
            stage.addActor(task);

            showTextArea.setPosition(backButton.getX(), backButton.getY()-showTextArea.getHeight()-5);
            stage.addActor(showTextArea);
        }
    }

    private Map<String, ThingTypeEnum> stringToThingTypeEnum(){
        HashMap<String, ThingTypeEnum> stringToThingTypeEnumMap = new HashMap<>();
        stringToThingTypeEnumMap.put("BALL", ThingTypeEnum.BALL);
        stringToThingTypeEnumMap.put("DESK", ThingTypeEnum.DESK);
        stringToThingTypeEnumMap.put("DOMINO", ThingTypeEnum.DOMINO);
        stringToThingTypeEnumMap.put("BALLOON", ThingTypeEnum.BALLOON);
        stringToThingTypeEnumMap.put("PUSHPIN", ThingTypeEnum.PUSHPIN);
        stringToThingTypeEnumMap.put("FAN", ThingTypeEnum.FAN);
        return stringToThingTypeEnumMap;
    }

    public void inventoryReorganization(){
        for(DragAndDropActor actor:inventoryActor.getItems()){
            actor.remove();
        }
        inventoryActor.clearItems();
        for(ThingTypeEnum elementType:ThingTypeEnum.values()){
            if(elementType != ThingTypeEnum.FINISH_LINE && elementType != ThingTypeEnum.AIRBALL && elementType != ThingTypeEnum.TEXTFIELD) {
                DragAndDropActor currentElement = new DragAndDropActor(textureMap, elementType, inventoryActor, elements, this);
                inventoryActor.addItem(currentElement);
                stage.addActor(currentElement);
            }
        }
    }

    public void saveLevel(){
        Json json = new Json();
        ArrayList<Object> actors = new ArrayList<>();
        for(DragAndDropActor element:elements){
            SaveActor saveActor = new SaveActor(element.thingTypeEnum, element.getX(), element.getY(), element.rotation, element.NeedToWin, element.toInventory);
            actors.add(saveActor);
        }
        FileHandle directory = Gdx.files.local("levels/");
//        FileHandle[] files = directory.list();
//        directory.emptyDirectory();
//        for(int i = 0; i < filesCount(); ++i){
//            String levelName = "level" + i + ".json";
//            directory.child(levelName).writeString(files[i].readString(), false);
//        }
        SaveActor taskSave = new SaveActor(ThingTypeEnum.TEXTFIELD, 0, 0, 0, false, false);
        String text = task.getText();
//        if(Gdx.app.getType() == Application.ApplicationType.Desktop){
//
//        }
        taskSave.setTask(text);
        actors.add(taskSave);
        Date date = new Date();
        String levelName = "level" + date.getTime() + ".json";
        String data = json.prettyPrint(actors);
        System.out.println(data);
        directory.child(levelName).writeString(data, false, "utf-8");
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
        textureMap.put(ThingTypeEnum.DOMINO, new TextureRegion(new Texture("Textures/domino.png")));
        textureMap.put(ThingTypeEnum.PUSHPIN, new TextureRegion(new Texture("Textures/pushpin.png")));
        textureMap.put(ThingTypeEnum.BALLOON, new TextureRegion(new Texture("Textures/balloon.png")));
        textureMap.put(ThingTypeEnum.FAN, new TextureRegion(new Texture("Textures/fan.png")));
        return textureMap;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        if(isSelected != null && (isSelected.thingTypeEnum == ThingTypeEnum.DESK || isSelected.thingTypeEnum == ThingTypeEnum.FAN)){
            buttons.setVisible(isSelected.isVisible());
            buttons.setPosition(isSelected.getX()-buttons.getWidth()-5, isSelected.getY());
        } else{
            buttons.setVisible(false);
        }
        if(isCreateLevelScreen){
            if(isSelected != null && (isSelected.thingTypeEnum == ThingTypeEnum.BALL || isSelected.thingTypeEnum == ThingTypeEnum.BALLOON || isSelected.thingTypeEnum == ThingTypeEnum.DOMINO)){
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
                toInventory.setVisible(false);
            }
        }
        stage.act();
        stage.draw();


//        DragAndDropActor prev = null;
//        for(DragAndDropActor element:elements){
//            if(element.thingTypeEnum == ThingTypeEnum.PUSHPIN){
//                if(prev != null){
//                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                    shapeRenderer.setColor(1, 0, 0, 1); // Red line
//                    shapeRenderer.rectLine(prev.getX(), prev.getY(), element.getX(), element.getY(), 10);
//                    shapeRenderer.end();
//                }
//                prev = element;
//            }
//        }
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
        isSelected = null;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void rotate(float degree) {
        if (isSelected != null){
            isSelected.rotation += degree;
//            isSelected.setRotation(isSelected.getRotation() + degree);
//            isSelected.rotateBy(degree);
//            isSelected.setHeight((float) (isSelected.getHeight() + isSelected.getWidth()*Math.sin(isSelected.getRotation())));
//            isSelected.setWidth((float) (isSelected.getWidth()*Math.cos(isSelected.getRotation())));
        }
    }
}

