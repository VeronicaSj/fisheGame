package com.mygdx.game.actors;

import static com.mygdx.game.actors.Corner.INTESTINE_HEIGHT;
import static com.mygdx.game.actors.Corner.INTESTINE_WIDTH;
import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.extra.Utils.USER_INTESTINE;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.extra.MyAssetManager;
import com.mygdx.game.helper.IntestineGenerationHelper;

public class Intestine extends Actor {

    //Clase enum para asegurarnos de que las direcciones solo tomen una de los tres valores posibles
    public enum Direction{
        DIRECTION_TO_FLOOR, DIRECTION_TO_LEFT, DIRECTION_TO_RIGHT;
    }

    //ATRIBUTOS "AUXILIARES" PARA LA GENERACION DE NUEVO CAMINO

    //no pongo esto en un enum porque los valore que tienen estas contastes son importantes
    public static final float ORIENTATION_STREET_VERTICAL=0;
    public static final float ORIENTATION_STREET_HORIZONTAL=90;
    public static final float ORIENTATION_CORNER_TOP_TO_RIGHT=90;
    public static final float ORIENTATION_CORNER_TOP_TO_LEFT=180;
    public static final float ORIENTATION_CORNER_LEFT_TO_FLOR=270;
    public static final float ORIENTATION_CORNER_RIGHT_TO_FLOR=0;

    IntestineGenerationHelper intestineHelper; //fibra?

    //ATRIBUTOS PROPIOS DE LA CLASE

    protected TextureRegion trIntestine;
    protected Body body;
    protected Fixture fixture1;
    protected Fixture fixture2;
    protected Fixture fixture3;
    protected World world;
    protected Direction direction;

    private static float speed = 8f;

    //super constructor
    public Intestine(TextureRegion trIntestine, World world, Vector2 position, Direction direction) {
        this.trIntestine = trIntestine;
        this.world = world;
        createBody(position);
        this.direction = direction;
        intestineHelper=new IntestineGenerationHelper();
    }

    //GETTERS
    public Body getBody() {return body;}
    public World getWorld() {return world;}
    public Direction getDirection() {return direction;}

    public static float getSpeed() {return speed;}

    //creamos el cuerpos
    private void createBody(Vector2 position) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(def);
        body.setUserData(USER_INTESTINE);
        body.setLinearVelocity(0, speed);

    }

    //Metodo detach para liberar recursos
    public void detach(){
        body.destroyFixture(fixture1);
        body.destroyFixture(fixture2);
        body.destroyFixture(fixture3);
        world.destroyBody(body);
    }

    //Funcion que nos ayuda a gestionar el camino. Dice si el intestino esta fuera de vista
    public boolean isOutOfScreen(){
        return body.getPosition().y >= SCREEN_HEIGHT+(INTESTINE_HEIGHT/2);
    }

    //Funcion que nos ayuda a gestionar el camino. Dice si el intestino esta a punto de mostrarse
    public boolean isNeededNew(){
        //tenemos que generar camino un poco antes de que sea necesario mostrarlo
        return this.body.getPosition().y>=-(INTESTINE_HEIGHT/2) ||
                (this.body.getPosition().x>=-(INTESTINE_WIDTH/2) &&
                        this.body.getPosition().x<=(WORLD_WIDTH+(INTESTINE_WIDTH/2)));
    }

    //En esta funcion vamos a capturar los eventos del usuario
    //el evento que estamos capturando es el deslizamiento del dedo
    //puede que el usuario sienta que está invertido, esto depende de la lógica de cada persona
    //la respuesta del intestino va a ser moverse en la direccion que le indiques
    @Override
    public void act(float delta) {
        if(Fish.getState().equals(Fish.State.ALIVE)) {
            boolean slide = Gdx.input.isTouched();
            int deltaX = Gdx.input.getDeltaX();
            int deltaY = Gdx.input.getDeltaY();
            if (slide) {
                incrementSpeed(0.001f);//hay una pequeña penalizacion por tocar la pantalla
                    //con esta penalizacion pretendo evitar que el usuario avance de lado a lado
                    // para conseguir una puntuacion altisima sin esfuerzo
                    //esta penalizacion tambien se podra usar para ajustar la dificultad al usuario
                    //si un usuario quiere mas velocidad solo tendra que tocar la pantalla
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (deltaX < 0) {
                        body.setLinearVelocity(-speed, 0);
                    } else {
                        body.setLinearVelocity(speed, 0);
                    }
                } else if (deltaY < 0) {
                    body.setLinearVelocity(0, speed);
                }
            }
        }
    }

    //funcion para parar los intestinos cuando chocamos
    public void stopIntestines() {
        body.setLinearVelocity(0, 0);
    }

    //funcion que incrementa la velocidad ergo la dificultad
    public static void incrementSpeed(float increment) {
        Intestine.speed = speed +increment;
    }

    //funcion que reinicia la velocidad
    public static void restartSpeed() {
        Intestine.speed = 8f;
    }

    /*la generacion del camino es de forma procedural
    el camino se genera de forma aleatoria, pero necesitamos unas normas para que tenga sentido
    y el juego sea jugable*/
    public Intestine getNextIntestine(MyAssetManager assetMan){
        Intestine intestine=this;

        //decidimos si el siguiente es esquina o calle
        //y decidimos dependiendo del intestino anterior como va a ser el nuevo

        int rdm = (int)(Math.random()*(2));//decidimos que tipo de camino creamos
        switch (rdm){
            case 0:
                intestine = intestineHelper.createCorner(this,assetMan);
                break;
            default:
                intestine = intestineHelper.createStreet(this,assetMan);
        }
        return intestine;
    }
}
