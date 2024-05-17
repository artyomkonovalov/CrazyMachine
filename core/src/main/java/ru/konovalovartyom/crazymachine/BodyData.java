package ru.konovalovartyom.crazymachine;

public class BodyData {
    private final ThingTypeEnum typeEnum;
    private final boolean needToWin;

    public BodyData(ThingTypeEnum typeEnum, boolean needToWin) {
        this.typeEnum = typeEnum;
        this.needToWin = needToWin;
    }

    public ThingTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public boolean isNeedToWin() {
        return needToWin;
    }
}
