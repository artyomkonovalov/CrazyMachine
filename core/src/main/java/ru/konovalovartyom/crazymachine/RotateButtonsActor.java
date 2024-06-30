package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.List;
import java.util.Map;

public class RotateButtonsActor extends Group {

    private RotateListener rotateListener;
    public RotateButtonsActor(RotateListener rotateListener){
        this.rotateListener = rotateListener;

        ImageButton.ImageButtonStyle clockwiseButtonStyle = new ImageButton.ImageButtonStyle();
        Texture clockwiseNormal = new Texture("Textures/rotatebutton_clockwise_normal(wood).png");
        Texture clockwiseActive = new Texture("Textures/rotatebutton_clockwise_active(wood).png");
        clockwiseButtonStyle.up = new TextureRegionDrawable(clockwiseNormal);
        clockwiseButtonStyle.down = new TextureRegionDrawable(clockwiseActive);
        ImageButton clockwiseButton = new ImageButton(clockwiseButtonStyle);
        clockwiseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rotateListener.rotate(-5);
            }

        });

        ImageButton.ImageButtonStyle anticlockwiseButtonStyle = new ImageButton.ImageButtonStyle();
        Texture anticlockwiseNormal = new Texture("Textures/rotatebutton_anticlockwise_normal(wood).png");
        Texture anticlockwiseActive = new Texture("Textures/rotatebutton_anticlockwise_active(wood).png");
        anticlockwiseButtonStyle.up = new TextureRegionDrawable(anticlockwiseNormal);
        anticlockwiseButtonStyle.down = new TextureRegionDrawable(anticlockwiseActive);
        ImageButton anticlockwiseButton = new ImageButton(anticlockwiseButtonStyle);
        anticlockwiseButton.setPosition(0, clockwiseButton.getHeight() + 5);
        anticlockwiseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rotateListener.rotate(5);
            }
        });
        addActor(clockwiseButton);
        addActor(anticlockwiseButton);
        setWidth(clockwiseNormal.getWidth());
    }
}
