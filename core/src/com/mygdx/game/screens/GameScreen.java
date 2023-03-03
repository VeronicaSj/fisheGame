package com.mygdx.game.screens;

import static com.mygdx.game.actors.Intestine.ORIENTATION_STREET_VERTICAL;
import static com.mygdx.game.actors.Intestine.incrementSpeed;
import static com.mygdx.game.actors.Intestine.restartSpeed;
import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.extra.Utils.SCREEN_WIDTH;
import static com.mygdx.game.extra.Utils.USER_FISH;
import static com.mygdx.game.extra.Utils.USER_INTESTINE;
import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MainGame;
import com.mygdx.game.actors.Fish;
import com.mygdx.game.actors.Intestine;
import com.mygdx.game.actors.Street;

public class GameScreen extends BaseScreen  implements ContactListener {

    //Recursos "esteticos"
    private Music musicbg;
    private Sound hitSound;

    //Actores
    private Fish fish;
    private Array<Intestine> path;

    public GameScreen(MainGame mainGame) {
        super(mainGame);
        this.world.setContactListener(this);//nuestro mundo va a responder a colisiones

        //inicializamos la musica
        this.musicbg = this.mainGame.assetMan.getMusicBG();
        this.hitSound= this.mainGame.assetMan.getHitSound();

        //inicializamos el array vacío
        this.path=new Array<>();
    }

    //funcion para añadir el pez al escenario
    public void addFish(){
        Animation<TextureRegion> fishSprite = mainGame.assetMan.getFishAnimation();
        this.fish = new Fish(world, fishSprite, new Vector2(4.5f,9f));
        this.stage.addActor(this.fish);
    }

    //funcion que ngenera el camino inicial.
    // El camino inicial siempre va a ser camino recto hacia abajo
    public void addStarterIntestine(){
        TextureRegion trStreet = mainGame.assetMan.getStreet();

        for(int i =0; i<5;i++){//necesitaremos 5 tramos para cubrir toda la pantalla
            Intestine street = new Street(trStreet,this.world,
                    new Vector2(5f,(WORLD_HEIGHT-2)-(4*i)),
                    ORIENTATION_STREET_VERTICAL, Intestine.Direction.DIRECTION_TO_FLOOR);
            this.stage.addActor(street);
            path.add(street);
        }
    }

    //funcion para añadir camino
    private void addIntestine(){
        Intestine lastPath=path.get(path.size-1);

        //comprobamos si se necesita camino
        if(lastPath!=null && Fish.getState().equals(Fish.State.ALIVE) && lastPath.isNeededNew()){

            //el ultimo tramo de camino es el que se encarga de generar nuevo camino
            Intestine intestine= lastPath.getNextIntestine(mainGame.assetMan);//le pasamos el
            // asetmanager porque dentro de este metodo necesitamos obtener dos
            // tipos de textura

            path.add(intestine);
            this.stage.addActor(intestine);
        }
    }

    //Funcion para quitar el camino cuando se encuentre fuera de vista
    private void removeIntestine(){
        for (Intestine intestine : this.path) {
            if(!world.isLocked() && intestine.isOutOfScreen()) {
                intestine.detach();
                intestine.remove();
                path.removeValue(intestine,false);
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        //el camino se añadirá en el fotograma siguiente del que es llamado
        if(Fish.getState().equals(Fish.State.ALIVE)){
            addIntestine();//añadimos el camino necesario
            fish.toFront();//necesitamos que el pez se quede siempre encima aunque hayan actores que se
            // han añadido despues
            removeIntestine();//eliminamos los intestinos que no se van a volver a ver
            incrementSpeed(0.005f);//aumentamos la dificultad
        }

        write(""+(int)(Intestine.getSpeed()-8));
    }

    @Override
    public void show() {
        addBackground(new Image(mainGame.assetMan.getBackground()));
        addStarterIntestine();
        addFish();

        this.musicbg.setLooping(true);
        this.musicbg.play();
    }

    @Override
    public void hide() {
        this.fish.detach();
        this.fish.remove();
        for (Intestine inte : path) {
            inte.detach();
            inte.remove();
        }
        path.clear();
        this.musicbg.stop();
    }

    //***************************************************************
    // COLISIONES
    //***************************************************************

    //Funcion para saber si dos actores pasados por parametro han colisionado
    private boolean areColider(Contact contact, Object objA, Object objB) {
        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }

    //funcion para responder ante las colisiones
    @Override
    public void beginContact(Contact contact) {
        if (areColider(contact, USER_FISH, USER_INTESTINE)) {//comprobamos si el pez ha chocado con las paredes
            fish.hurt();//cambiamos el estado del pez

            //paramos todos los intestinos
            for (Intestine inte : path) {
                inte.stopIntestines();
            }

            //paramos la musuca y reproducimos el sonido de choque
            this.musicbg.stop();
            this.hitSound.play();

            //camviamos de pantalla y restauramos la velocidad
            this.stage.addAction(Actions.sequence(
                    Actions.delay(0.7f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            mainGame.setScreen(mainGame.gameOverScreen);
                            restartSpeed();
                        }
                    })
            ));
        }
    }

    // FUNCIONES NO NECESARIAS
    @Override
    public void endContact(Contact contact) {}
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}