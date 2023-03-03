package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.USER_FISH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Fish extends Actor {

    //no pongo esto en un enum porque los valore que tienen estas costantes son importantes
    private static float FISH_DOWN=0;
    private static float FISH_LEFT=270;
    private static float FISH_RIGHT=90;

    //Clase enum que contienen los valores posibles del estado de vida del pez
    public enum State{
        ALIVE, DEAD;
    }

    private static State state;//informacion sobre el estado de vida del pez

    //atributos relacionados con la estetica del pez
    private Animation<TextureRegion> fishAnimation;
    private Vector2 position;
    private float stateTime;
    private float rotation;//este es un atributo que proviene de Actor

    //atributos relacionados con las fisicas del pez
    private World world;
    private Body body;
    private Fixture fixture;

    //CONSTUCTOR
    public Fish(World world, Animation<TextureRegion> animation, Vector2 position) {
        this.state=State.ALIVE;

        this.fishAnimation = animation;
        this.position = position;
        rotation=0;
        this.stateTime = 0f;

        this.world = world;
        createBody();
        createFixture();
    }

    //funcion para inicializar el body
    private void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(this.position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = this.world.createBody(bodyDef);
    }

    //funcion para inicializar el fixture
    private void createFixture(){
        CircleShape circle = new CircleShape();
        circle.setRadius(1.15f);
        this.fixture = this.body.createFixture(circle, 8);
        this.fixture.setUserData(USER_FISH);
        circle.dispose();
    }

    //funcion para cambiar el estado del pez
    public void hurt() {
        state = State.DEAD;
    }

    //getter del estado de vida del pez
    public static State getState() {return state;}

    //En esta funcion vamos a capturar los eventos del usuario
    //el evento que estamos capturando es el deslizamiento del dedo
    //puede que el usuario sienta que está invertido, esto depende de la lógica de cada persona
    //la respuesta del pez va a ser girarse un poco para parecer que es él el que se está moviendo
    @Override
    public void act(float delta) {
        if(state.equals(State.ALIVE)){
            boolean slide = Gdx.input.isTouched();
            int deltaX = Gdx.input.getDeltaX();
            int deltaY = Gdx.input.getDeltaY();
            if (slide) {
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (deltaX < 0) {
                        rotation=90;
                    } else {
                        rotation=270;
                    }
                } else if (deltaY < 0) {
                    rotation=0;
                }
            }

        }
    }

    //estafuncion se llama en el render, así que aprovechamos para actualizar la rotacion aquí
    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x-1.6f, body.getPosition().y-1.1f);
        batch.draw(this.fishAnimation.getKeyFrame(stateTime, true),
                getX(), getY(),
                1.6f,1.1f,
                3f,3f,
                1f,1f,
                rotation,true);

        stateTime += Gdx.graphics.getDeltaTime();
    }

    //funcion para liberar recursos
    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }
}