package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.List;
import java.util.Map;

public class DragAndDropActor extends Actor {

    private float grabOffsetX;
    private float grabOffsetY;
    public TextureRegion textureRegion;
    public FirstScreen screen;
    public ThingTypeEnum thingTypeEnum;
    public DragAndDropActor(Map<ThingTypeEnum, TextureRegion> textureRegionMap, ThingTypeEnum thingTypeEnum, InventoryActor inventory, List<DragAndDropActor> elements, FirstScreen firstScreen) {
        this.screen = firstScreen;
        this.thingTypeEnum = thingTypeEnum;
        this.textureRegion = textureRegionMap.get(thingTypeEnum);
        setWidth(textureRegion.getRegionWidth());
        setHeight(textureRegion.getRegionHeight());
        setOrigin(getWidth()/2, getHeight()/2);
        // setPosition(100, 100);

        addListener(
            new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    toFront();
                    //addAction(Actions.scaleTo(1.1f, 1.1f, 0.25f));
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
                    super.touchUp(event, x, y, pointer, button);
                    if(getX() >= inventory.getX()){
                        inventory.addItem(DragAndDropActor.this);
                        elements.remove(DragAndDropActor.this);
                    }else {
                        elements.add(DragAndDropActor.this);
                    }
                    screen.isSelected = DragAndDropActor.this;
                    if(thingTypeEnum == ThingTypeEnum.PUSHPIN){
                        screen.drawFinishLine();
                    }
                }
            }
        );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(
            textureRegion,
            getX(),
            getY(),
            getOriginX(),
            getOriginY(),
            getWidth(),
            getHeight(),
            getScaleX(),
            getScaleY(),
            getRotation()
        );
    }
}
