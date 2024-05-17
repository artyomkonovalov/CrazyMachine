package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LogoActor extends Actor {
    private TextureRegion texture;
    public LogoActor() {
        this.texture = new TextureRegion(new Texture("Textures/logo.png"));
        setWidth(texture.getRegionWidth()/1.5F);
        setHeight(texture.getRegionHeight()/1.5F);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
