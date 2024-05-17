package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureActor extends Actor {
    public static final float PPM = 100;
    private TextureRegion texture;
    public TextureActor(DragAndDropActor actor) {
        this.texture = new TextureRegion(actor.textureRegion);
        setWidth(actor.getWidth());
        setHeight(actor.getHeight());
        setOrigin(actor.getOriginX(), actor.getOriginY());
        setPosition(actor.getX(), actor.getY());
        setRotation(actor.getRotation());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
