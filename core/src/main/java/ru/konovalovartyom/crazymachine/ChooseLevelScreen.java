package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;


public class ChooseLevelScreen implements Screen {
    private StartScreen startScreen;
    private final MainGame game;
    private Viewport viewport;
    private Stage stage;
    private boolean isDeleteMod;
    private BitmapFont font;
    boolean fromLocalStorage;
    private static final int STARTX = 270;
    private static final int STARTY = 530;
    public static final int STEPX = 155;
    public static final int STEPY = 155;
    private ArrayList<LevelActor> levels;


    public ChooseLevelScreen(MainGame game, StartScreen startScreen, boolean fromLocalStorage) {
        this.startScreen = startScreen;
        this.game = game;
        this.fromLocalStorage = fromLocalStorage;
        isDeleteMod = false;
        FileHandle fontFile = Gdx.files.internal("Textures/vag-world-bold.fnt");
        font = new BitmapFont(fontFile);
    }

    @Override
    public void show() {

        isDeleteMod = false;
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        BackgroundActor backgroundActor = new BackgroundActor(new Texture("Textures/levelchoose_background.png"));
        stage.addActor(backgroundActor);


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

        ImageButton.ImageButtonStyle deleteButtonStyle = new ImageButton.ImageButtonStyle();
        Texture deleteNormal = new  Texture("Textures/deletebutton_normal(wood).png");
        Texture deleteActive = new  Texture("Textures/deletebutton_active(wood).png");
        deleteButtonStyle.up = new TextureRegionDrawable(deleteNormal);
        deleteButtonStyle.down = new TextureRegionDrawable(deleteActive);
        ImageButton deleteButton = new ImageButton(deleteButtonStyle);
        deleteButton.setPosition(MainGame.SCREEN_WIDTH-1.5F*deleteButton.getWidth(), deleteButton.getHeight()/4);
        deleteButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchDeleteMod();
            }
        });

        stage.addActor(backButton);
        stage.addActor(deleteButton);

        levelReorganization();
    }

    private void switchDeleteMod(){
        isDeleteMod = !isDeleteMod;
    }

    private void levelReorganization(){
        levels = new ArrayList<>();
        FileHandle[] files;
        if(fromLocalStorage){
            files = Gdx.files.local("levels/").list();
        } else{
            files = Gdx.files.internal("levels/").list();
        }
        for(FileHandle file: files) {
            LevelActor levelActor = new LevelActor(file);
            levels.add(levelActor);
        }
        for(int i = 0; i < levels.size(); ++i){
            stage.addActor(levels.get(i));
            levels.get(i).setPosition(STARTX + i%5*STEPX, STARTY - i/5*STEPY);
            final int a = i;
            levels.get(i).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(isDeleteMod){
                        levels.get(a).getLevelFile().delete();
                        levels.get(a).remove();
                        levelReorganization();
                        switchDeleteMod();
                    }
                    else{
                        FirstScreen gameScreen = new FirstScreen(startScreen, game, false, levels.get(a).getLevelFile());
                        game.setScreen(gameScreen);
                        dispose();
                    }
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
        if(isDeleteMod){
            stage.getBatch().begin();
            font.draw(stage.getBatch(), "Удалить уровень", 64, MainGame.SCREEN_HEIGHT-font.getXHeight());
            stage.getBatch().end();
        }
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
}
