package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Map;

public class DragAndDropActor extends Actor{

    private float grabOffsetX;
    private float grabOffsetY;
    private TextureRegion textureRegion;

    private ThingTypeEnum thingTypeEnum;
    public DragAndDropActor(Map<ThingTypeEnum, TextureRegion> textureRegionMap, ThingTypeEnum thingTypeEnum, InventoryActor inventory) {
        this.thingTypeEnum = thingTypeEnum;
        this.textureRegion = textureRegionMap.get(thingTypeEnum);
        setWidth(textureRegion.getRegionWidth());
        setHeight(textureRegion.getRegionHeight());
        setPosition(100, 100);

        addListener(
            new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    toFront();
                    addAction(Actions.scaleTo(1.1f, 1.1f, 0.25f));
                    grabOffsetX = x;
                    grabOffsetY = y;
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    float deltaX = x - grabOffsetX;
                    float deltaY = y - grabOffsetY;
                    moveBy(deltaX, deltaY);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    addAction(Actions.scaleTo(1.00f, 1.00f, 0.25f));
                    super.touchUp(event, x, y, pointer, button);
                    if(getX() > inventory.getX()){
                        inventory.addItem(DragAndDropActor.this);
                    }
                }
            }
        );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY());
    }
}

