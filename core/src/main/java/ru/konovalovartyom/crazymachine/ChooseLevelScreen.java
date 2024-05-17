package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;


public class ChooseLevelScreen implements Screen {
    private final MainGame game;
    private Viewport viewport;
    private Stage stage;
    private static final int STARTX = 270;
    private static final int STARTY = 530;
    public static final int STEPX = 155;
    public static final int STEPY = 155;
    private ArrayList<LevelActor> levels = new ArrayList<>();


    public ChooseLevelScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        BackgroundActor backgroundActor = new BackgroundActor(new Texture("Textures/levelchoose_background.png"));
        stage.addActor(backgroundActor);

        FileHandle[] files = Gdx.files.local("levels/").list();
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
                    FirstScreen gameScreen = new FirstScreen(game, false, levels.get(a).getLevelFile());
                    game.setScreen(gameScreen);
                    dispose();
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
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
}
