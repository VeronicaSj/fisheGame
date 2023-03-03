package com.mygdx.game.screens;

import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.extra.Utils.SCREEN_WIDTH;
import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MainGame;

// Pantalla que aparece cuando pierdes

public class GameOverScreen extends BaseScreen{

    //recursos "esteticos"
    private Sound gameOverSound;

    public GameOverScreen(final MainGame mainGame) {
        super(mainGame);
        this.gameOverSound= this.mainGame.assetMan.getGameOverSound();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        write("PULSE");

        //input para cambiar la pantalla
        if(Gdx.input.justTouched()){
            gameOverSound.stop();
            mainGame.setScreen(mainGame.gameScreen);
        }
    }

    @Override
    public void show() {
        addBackground(new Image(mainGame.assetMan.getGameOverBackground()));
        this.gameOverSound.play();
    }
}
