package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class InfoScreen implements Screen {
    private StartScreen startScreen;
    private final MainGame game;
    private Viewport viewport;
    private Stage stage;
    private ArrayList<OnlyTextureActor> pages;
    ListIterator<OnlyTextureActor> currentPage;

    public InfoScreen(MainGame game, StartScreen startScreen) {
        this.game = game;
        this.startScreen = startScreen;
        pages = new ArrayList<>();
        pages.add(new OnlyTextureActor(new Texture("Textures/ManualPages/Title.png")));
        pages.add(new OnlyTextureActor(new Texture("Textures/ManualPages/page1.png")));
        pages.add(new OnlyTextureActor(new Texture("Textures/ManualPages/page2.png")));
        pages.add(new OnlyTextureActor(new Texture("Textures/ManualPages/page3.png")));
        pages.add(new OnlyTextureActor(new Texture("Textures/ManualPages/page4.png")));
        pages.add(new OnlyTextureActor(new Texture("Textures/ManualPages/page5.png")));
    }

    @Override
    public void show() {
        currentPage = pages.listIterator();
        viewport = new FitViewport(MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        BackgroundActor backgroundActor = new BackgroundActor(new Texture("Textures/StartBackground.jpeg"));
        stage.addActor(backgroundActor);

        ImageButton.ImageButtonStyle returnStyle = new ImageButton.ImageButtonStyle();
        Texture returnNormal = new Texture("Textures/backbutton_normal(wood).png");
        Texture returnActive = new Texture("Textures/backbutton_active(wood).png");
        returnStyle.up = new TextureRegionDrawable(returnNormal);
        returnStyle.down = new TextureRegionDrawable(returnActive);
        ImageButton returnButton = new ImageButton(returnStyle);
        returnButton.setPosition(0, MainGame.SCREEN_HEIGHT-returnButton.getHeight());
        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(startScreen);
            }

        });
        stage.addActor(returnButton);

        for(OnlyTextureActor actor:pages){
            actor.setPosition(MainGame.SCREEN_WIDTH/2F-actor.getWidth()/2F, (MainGame.SCREEN_HEIGHT-actor.getHeight())/2F);
            stage.addActor(actor);
            actor.setVisible(false);
        }
        currentPage.next().setVisible(true);
        ImageButton.ImageButtonStyle nextStyle = new ImageButton.ImageButtonStyle();
        nextStyle.up = new TextureRegionDrawable(new Texture("Textures/forwardbutton_normal(wood).png"));
        nextStyle.down = new TextureRegionDrawable(new Texture("Textures/forwardbutton_active(wood).png"));
        ImageButton nextButton = new ImageButton(nextStyle);
        nextButton.setPosition(MainGame.SCREEN_WIDTH-nextButton.getWidth(), 0);
        nextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentPage.hasNext()){
                    currentPage.next().setVisible(true);
                }
            }
        });
        stage.addActor(nextButton);

        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.up = new TextureRegionDrawable(new Texture("Textures/backbutton_normal(wood).png"));
        backStyle.down = new TextureRegionDrawable(new Texture("Textures/backbutton_active(wood).png"));
        ImageButton backButton = new ImageButton(backStyle);
        backButton.setPosition(0, 0);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentPage.hasPrevious()){
                    currentPage.previous().setVisible(false);
                }
            }
        });
        stage.addActor(backButton);
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
        stage.dispose();
    }
}
