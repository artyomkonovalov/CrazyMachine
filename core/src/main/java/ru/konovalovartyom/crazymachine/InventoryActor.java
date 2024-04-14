package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InventoryActor extends Actor {
    private List<DragAndDropActor> items = new ArrayList<>();

    private final Texture background;
    private final int space = 10;

    private final float childHeight;
    private final float childWidth;

    public InventoryActor(float x, float y, int width, int height, float childWidth, float childHeight) {
        setBounds(x, y, width, height);
        background = new Texture("Textures/Inventory.jpg");
        this.childHeight = childHeight;
        this.childWidth = childWidth;
    }

    public void addItems(List<DragAndDropActor> actors) {
        if(!actors.isEmpty()) {
            items = actors;
            reorganization();
        }
    }

    public void addItem(DragAndDropActor actor) {
        if (!items.contains(actor)){
            items.add(actor);
            reorganization();
        }
    }

    private void reorganization() {
        float positionY = getTop();
        float positionX = getX() + (getWidth() / 2F) - childWidth / 2;

        for (Actor actor : items) {
            positionY = positionY - actor.getHeight() - space;
            actor.setPosition(positionX, positionY);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Iterator<DragAndDropActor> iterator = items.iterator();
        while (iterator.hasNext()) {
            DragAndDropActor item = iterator.next();
            if (item.getRight() < getX()) {
                iterator.remove();
                reorganization();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background, getX(), getY(), getWidth(), getHeight());
    }

    public void toDown() {
        for (Actor actor : items) {
            if (getY() + childHeight > getBottom()) {
                actor.setY(actor.getY() + actor.getHeight() + space / 2F);
            }
        }
    }

    public void toUp() {
        if (!items.isEmpty()) {
            Actor firstActor = items.get(0);
            if (getTop() - childHeight < firstActor.getY()) {
                for (Actor actor : items) {
                    actor.setY(actor.getY() - actor.getHeight() - space / 2F);
                }
            }
        }
    }

    public float getAllChildHeight() {
        float height = 0F;
        for (Actor actor : items) {
            height += actor.getHeight() + space;
        }
        return height;
    }

    private float getBottom() {
        if (!items.isEmpty()) {
            Actor actor = items.get(items.size() - 1);
            return actor.getY();
        } else {
            return 0F;
        }
    }
}
