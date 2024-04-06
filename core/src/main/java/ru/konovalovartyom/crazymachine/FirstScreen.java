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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;


/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    private Stage stage;
    private Viewport viewport;
    @Override
    public void show() {
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        BackgroundActor backgroundActor = new BackgroundActor();
        stage.addActor(backgroundActor);

        InventoryActor inventoryActor = new InventoryActor(1040, 0, 240, 720, 200, 200);
        stage.addActor(inventoryActor);


        GrabAndDropActor ball = new GrabAndDropActor(new TextureRegion(new Texture("Textures/football.png")));
        stage.addActor(ball);

        ImageButton upButton = createButton(true);
        upButton.setPosition(0, 54);

        upButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventoryActor.toUp();
            }
        });

        List<GrabAndDropActor> list = new ArrayList<>();
        list.add(ball);
        inventoryActor.addItems(list);

        System.out.println(ball.getX());
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    private ImageButton createButton(boolean isUpButton){
        Texture normalTexture = null;
        Texture activeTexture = null;

        if(isUpButton){
            normalTexture = new Texture("Textures/upbutton_normal.png");
            activeTexture = new Texture("Textures/upbutton_active.png");
        } else {
            normalTexture = new Texture("Textures/downbutton_normal.png");
            activeTexture = new Texture("Textures/downbutton_active.png");
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
