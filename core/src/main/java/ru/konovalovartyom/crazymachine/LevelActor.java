package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class LevelActor extends Actor {
    private Texture texture;
    private FileHandle levelFile;

    public LevelActor(FileHandle levelFile) {
        this.levelFile = levelFile;
        texture = new Texture("Textures/level.png");
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
    }

    public FileHandle getLevelFile() {
        return levelFile;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
