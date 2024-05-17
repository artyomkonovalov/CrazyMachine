package ru.konovalovartyom.crazymachine;

public class BodyData {
    private final ThingTypeEnum typeEnum;
    private final boolean needToWin;
    private TextureActor textureActor;


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
}
