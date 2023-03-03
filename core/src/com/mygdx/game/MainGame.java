package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.extra.MyAssetManager;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.GetReadyScreen;
/*
desde esta clase vamos a gestionar los cambios de pantalla del juego y los assets
 */
public class MainGame extends Game {
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;
    public GetReadyScreen getReadyScreen;

    public MyAssetManager assetMan;

    @Override
    public void create() {
        this.assetMan = new MyAssetManager();

        this.gameScreen = new GameScreen(this);
        this.gameOverScreen = new GameOverScreen(this);
        this.getReadyScreen = new GetReadyScreen(this);
        setScreen(this.getReadyScreen);
    }
}