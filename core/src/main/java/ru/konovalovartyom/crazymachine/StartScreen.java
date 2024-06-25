package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen implements Screen {
    private final MainGame game;
    private Viewport viewport;
    private Stage stage;
    private  boolean startButtonPressed;

    private FirstScreen createLevelScreen;
    private ChooseLevelScreen chooseLevelScreen;
    private ChooseLevelScreen localLevelScreen;
    private InfoScreen infoScreen;

    public StartScreen(MainGame game) {
        this.game = game;
        createLevelScreen = new FirstScreen(this, game, true);
        chooseLevelScreen = new ChooseLevelScreen(game, this, false);
        localLevelScreen = new ChooseLevelScreen(game, this, true);
        infoScreen = new InfoScreen(game, this);
    }

    @Override
    public void show() {
        startButtonPressed = false;
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        BackgroundActor backgroundActor = new BackgroundActor(new Texture("Textures/StartBackground.jpeg"));
        stage.addActor(backgroundActor);

        LogoActor logo = new LogoActor();
        stage.addActor(logo);
        logo.setPosition((MainGame.SCREEN_WIDTH-logo.getWidth())/2, MainGame.SCREEN_HEIGHT-logo.getHeight());

        Texture ButtonUp = new Texture("Textures/button_up.png");
        Texture ButtonDown = new Texture("Textures/button_down.png");
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        FileHandle fontFile = Gdx.files.internal("Textures/vag-world-bold.fnt");
        BitmapFont bitmapfont = new BitmapFont(fontFile);
        buttonStyle.up = new TextureRegionDrawable(ButtonUp);
        buttonStyle.down = new TextureRegionDrawable(ButtonDown);
        buttonStyle.font = bitmapfont;

        TextButton startButton = new TextButton("Играть", buttonStyle);
        stage.addActor(startButton);
        startButton.setPosition(MainGame.SCREEN_WIDTH/2F - startButton.getWidth()/2F, 250);

        TextButton internalLevelButton = new TextButton("Выбрать уровень", buttonStyle);
        stage.addActor(internalLevelButton);
        internalLevelButton.setPosition(startButton.getX()+startButton.getWidth() + 10, startButton.getY() + startButton.getHeight()/2 + 5);
        internalLevelButton.setVisible(false);
        internalLevelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(chooseLevelScreen);
            }
        });

        TextButton localLevelButton = new TextButton("Свои уровни", buttonStyle);
        stage.addActor(localLevelButton);
        localLevelButton.setPosition(startButton.getX()+startButton.getWidth() + 10, startButton.getY() + startButton.getHeight()/2 - localLevelButton.getHeight() - 5);
        localLevelButton.setVisible(false);
        localLevelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(localLevelScreen);
            }
        });

        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                startButtonPressed = !startButtonPressed;
                internalLevelButton.setVisible(startButtonPressed);
                localLevelButton.setVisible(startButtonPressed);
            }
        });

        TextButton createLevelButton = new TextButton("Создать уровень", buttonStyle);
        stage.addActor(createLevelButton);
        createLevelButton.setPosition(MainGame.SCREEN_WIDTH/2F - startButton.getWidth()/2, 150);
        createLevelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(createLevelScreen);
            }
        });

        TextButton infoButton = new TextButton("Как играть?", buttonStyle);
        stage.addActor(infoButton);
        infoButton.setPosition(MainGame.SCREEN_WIDTH/2F - startButton.getWidth()/2F, 50);
        infoButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(infoScreen);
            }
        });

    }


    @Override
    public void render(float delta) {
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
}
