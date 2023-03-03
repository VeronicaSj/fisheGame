package com.mygdx.game.screens;

import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.extra.Utils.SCREEN_WIDTH;
import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MainGame;

// Clase que reduce la repeticion de codigo al maximo posible.
// Como su nombre indica, es un esqueleto sobre el que hacer nuevas pantallas

public class BaseScreen implements Screen {

    protected MainGame mainGame;

    //recursos "esteticos"
    protected Image background;
    protected BitmapFont font;

    //mundo y escenario
    protected Stage stage;
    protected World world;

    //Camaras
    protected OrthographicCamera ortCamera;
    protected OrthographicCamera fontCamera;

    public BaseScreen(MainGame mainGame){
        this.mainGame = mainGame;
        //inicializamos  el mundo
        this.world = new World(new Vector2(0,0), true);//no hay gravedad en este mundo

        //ajustamos el tamaño del juego y creamos el escenario con esas medidas
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);

        //preparamos nuestras letras
        prepareFont();

        //obtenemos la camara
        this.ortCamera = (OrthographicCamera) this.stage.getCamera();
    }

    //funcion para poner el fondo a las pantallas
    protected void addBackground(Image img){
        this.background = img;
        this.background.setPosition(0,0);
        this.background.setSize(WORLD_WIDTH,WORLD_HEIGHT);
        this.stage.addActor(this.background);
    }

    //funcion que inicializa y prepara los recursos necesarios para mostrar letras
    protected void prepareFont(){
        this.font = this.mainGame.assetMan.getFont();
        this.font.getData().scale(0.5f);

        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH,SCREEN_HEIGHT);
        this.fontCamera.update();
    }


    //funcion para poner las letras en pantalla
    protected void write(String msg) {
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.font.draw(this.stage.getBatch(), msg, SCREEN_WIDTH / 14, 100);
        this.stage.getBatch().end();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.getBatch().setProjectionMatrix(ortCamera.combined);
        this.stage.act();
        this.world.step(delta,6,2);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.stage.dispose();
        this.world.dispose();
    }
}
