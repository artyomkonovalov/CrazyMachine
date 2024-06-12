package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.LinkedHashSet;

public class BodyData {
    private final ThingTypeEnum typeEnum;
    private final boolean needToWin;
    private TextureActor textureActor;
    private Vector2 startPosition = new Vector2();

    private Vector2 dir = new Vector2();


    public BodyData(ThingTypeEnum typeEnum, boolean needToWin, TextureActor textureActor) {
        this.typeEnum = typeEnum;
        this.needToWin = needToWin;
        this.textureActor = textureActor;
    }

    public ThingTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public boolean isNeedToWin() {
        return needToWin;
    }
    public TextureActor getTextureActor() {
        return textureActor;
    }

    public void setStartPosition(Vector2 startPosition) {
        this.startPosition = startPosition;
    }

    public void setDir(Vector2 dir) {
        this.dir = dir;
    }

    public Vector2 getDir() {
        return dir;
    }
    public Vector2 getStartPosition() {
        return startPosition;
    }
}
