package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class OnlyTextureActor extends Actor {
    private Texture texture;

    public OnlyTextureActor(Texture texture) {
        this.texture = texture;
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
