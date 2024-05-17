package ru.konovalovartyom.crazymachine;


public class SaveActor {
    private ThingTypeEnum actorType;
    private float x;
    private float y;
    private float angle;
    private boolean isNeedToWin;
    private boolean toInventory;

    public SaveActor(ThingTypeEnum actorType, float x, float y, float angle, boolean isNeedToWin, boolean toInventory) {
        this.actorType = actorType;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.isNeedToWin = isNeedToWin;
        this.toInventory = toInventory;
    }

    public ThingTypeEnum getActorType() {
        return actorType;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public boolean isNeedToWin() {
        return isNeedToWin;
    }

    public boolean isToInventory() {
        return toInventory;
    }
}
