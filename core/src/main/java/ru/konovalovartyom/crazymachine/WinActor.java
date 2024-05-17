package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class WinActor extends Group {
    private Texture texture;
    private ImageButton backButton;

    public WinActor(StartScreen screen, MainGame game) {
        texture = new Texture("Textures/level_complete.png");
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
        setPosition(MainGame.SCREEN_WIDTH/2-getWidth()/2, MainGame.SCREEN_HEIGHT/2-getHeight()/2);

        OnlyTextureActor background = new OnlyTextureActor(texture);
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        Texture clockwiseNormal = new Texture("Textures/backbutton_normal(wood).png");
        Texture clockwiseActive = new Texture("Textures/backbutton_active(wood).png");
        backButtonStyle.up = new TextureRegionDrawable(clockwiseNormal);
        backButtonStyle.down = new TextureRegionDrawable(clockwiseActive);
        backButton = new ImageButton(backButtonStyle);
        backButton.setPosition(getWidth()/2-backButton.getWidth()/2, 0);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(screen);
            }

        });
        addActor(background);
        addActor(backButton);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
