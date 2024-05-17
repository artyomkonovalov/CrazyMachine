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

    FirstScreen createLevelScreen;
    ChooseLevelScreen chooseLevelScreen;

    public StartScreen(MainGame game) {
        this.game = game;
        createLevelScreen = new FirstScreen(game, true);
        chooseLevelScreen = new ChooseLevelScreen(game);
    }

    @Override
    public void show() {
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
        TextButton startButton = new TextButton("Start", buttonStyle);
        stage.addActor(startButton);
        startButton.setPosition(MainGame.SCREEN_WIDTH/2F - startButton.getWidth()/2F, 250);
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.setScreen(chooseLevelScreen);
            }
        });

        TextButton createLevelButton = new TextButton("Create Level", buttonStyle);
        stage.addActor(createLevelButton);
        createLevelButton.setPosition(MainGame.SCREEN_WIDTH/2 - startButton.getWidth()/2, 150);
        createLevelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.setScreen(createLevelScreen);
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
