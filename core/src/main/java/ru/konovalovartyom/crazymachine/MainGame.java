package ru.konovalovartyom.crazymachine;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends Game {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}
