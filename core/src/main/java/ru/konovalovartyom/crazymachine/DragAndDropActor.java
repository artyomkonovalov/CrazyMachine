package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.Map;
import java.util.Set;

public class DragAndDropActor extends Actor {

    private float grabOffsetX;
    private float grabOffsetY;
    public TextureRegion textureRegion;
    public FirstScreen screen;
    public ThingTypeEnum thingTypeEnum;
    public boolean NeedToWin;
    public boolean toInventory;
    public DragAndDropActor(Map<ThingTypeEnum, TextureRegion> textureRegionMap, ThingTypeEnum thingTypeEnum, InventoryActor inventory, Set<DragAndDropActor> elements, FirstScreen firstScreen) {
        this.screen = firstScreen;
        this.thingTypeEnum = thingTypeEnum;
        this.textureRegion = textureRegionMap.get(thingTypeEnum);
        setWidth(textureRegion.getRegionWidth());
        setHeight(textureRegion.getRegionHeight());
        setOrigin(getWidth()/2, getHeight()/2);
        if(screen.isCreateLevelScreen){
            addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        toFront();
                        //addAction(Actions.scaleTo(1.1f, 1.1f, 0.25f));
                        grabOffsetX = x;
                        grabOffsetY = y;
                        System.out.println(getWidth() + " " + getHeight());
                        System.out.println(getOriginX() + " " + getOriginY());
                        System.out.println(getX() + " " + getY());
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
                        screen.isSelected = DragAndDropActor.this;
                        if(firstScreen.isCreateLevelScreen){
                            if(getX() + getWidth()/2 >= inventory.getX()){
                                elements.remove(DragAndDropActor.this);
                                firstScreen.isSelected = null;
                                DragAndDropActor.this.remove();
                            }else {
                                elements.add(DragAndDropActor.this);
                            }
                            screen.inventoryReorganization();
                        }
                        else{
                            if(getX() + getWidth()/2 >= inventory.getX()){
                                firstScreen.isSelected.setRotation(0);
                                inventory.addItem(DragAndDropActor.this);
                                firstScreen.isSelected = null;
                                elements.remove(DragAndDropActor.this);
                            }else {
                                elements.add(DragAndDropActor.this);
                            }
                        }
                    }
                }
            );
        }

    }
    public DragAndDropActor(Map<ThingTypeEnum, TextureRegion> textureRegionMap, ThingTypeEnum thingTypeEnum, InventoryActor inventory, Set<DragAndDropActor> elements, FirstScreen firstScreen, boolean NeedToWin, boolean toInventory){
        this.screen = firstScreen;
        this.thingTypeEnum = thingTypeEnum;
        this.textureRegion = textureRegionMap.get(thingTypeEnum);
        this.toInventory = toInventory;
        this.NeedToWin = NeedToWin;
        setWidth(textureRegion.getRegionWidth());
        setHeight(textureRegion.getRegionHeight());
        setOrigin(getWidth()/2, getHeight()/2);
        if(screen.isCreateLevelScreen || this.toInventory){
            addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        toFront();
                        //addAction(Actions.scaleTo(1.1f, 1.1f, 0.25f));
                        grabOffsetX = x;
                        grabOffsetY = y;
                        System.out.println(getWidth() + " " + getHeight());
                        System.out.println(getOriginX() + " " + getOriginY());
                        System.out.println(getX() + " " + getY());
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
                        screen.isSelected = DragAndDropActor.this;
                        if(firstScreen.isCreateLevelScreen){
                            if(getX() + getWidth()/2 >= inventory.getX()){
                                firstScreen.isSelected = null;
                                elements.remove(DragAndDropActor.this);
                            }else {
                                elements.add(DragAndDropActor.this);
                                screen.inventoryReorganization();
                            }
                        }
                        else{
                            if(getX() + getWidth()/2 >= inventory.getX()){
                                firstScreen.isSelected.setRotation(0);
                                firstScreen.isSelected = null;
                                inventory.addItem(DragAndDropActor.this);
                                elements.remove(DragAndDropActor.this);
                            }else {
                                elements.add(DragAndDropActor.this);
                            }
                        }
                    }
                }
            );
        }
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
