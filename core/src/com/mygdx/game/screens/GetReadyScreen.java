package com.mygdx.game.screens;

import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.extra.Utils.SCREEN_WIDTH;
import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MainGame;

// Pantalla que aparece cuando abres la app

public class GetReadyScreen extends BaseScreen{

    public GetReadyScreen(final MainGame mainGame) {
        super(mainGame);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        write("PULSE");

        //input para cambiar la pantalla
        if(Gdx.input.justTouched()){
            mainGame.setScreen(mainGame.gameScreen);
        }
    }

    @Override
    public void show() {
        addBackground(new Image(mainGame.assetMan.getGetReadyBackground()));
    }
}
