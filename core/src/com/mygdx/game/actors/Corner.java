package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.USER_INTESTINE;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.extra.Utils;

public class Corner extends Intestine{
    //constantes de tamaño
    public static final float INTESTINE_WIDTH = 8f;
    public static final float INTESTINE_HEIGHT = 8f;

    //atributos para hacer los dibujos adecuado
    protected float rotation;//este es un atributo que proviene de Actor
    protected Direction initialDirection;

    //CONSTRUCTOR
    public Corner(TextureRegion trIntestine, World world, Vector2 position, float rotation,
                  Direction initialDirection, Direction direction) {
        super(trIntestine,world,position,direction);
        createFixtures();
        this.rotation=rotation;
        this.initialDirection=initialDirection;
        transformBodyRotation();
    }

    //getter de initialDirection
    public Direction getInitialDirection(){ return initialDirection;  }

    ////Funcion  que transformar la rotacion del body y el fixture, para que coincida con el dibujo
    private void transformBodyRotation(){
    switch ((int) this.rotation) {
        case 0:
            body.setTransform(body.getPosition(), 0f);
            break;
        case 90:
            body.setTransform(body.getPosition(), 33f);
            break;
        case 180:
            body.setTransform(body.getPosition().x - 0.66f, body.getPosition().y, 22f);
            //en este caso hemos necesitado un pequeño ajuste de la posicion
            break;
        case 270:
            body.setTransform(body.getPosition(), 11f);
            break;
        }
    }

    //lo dibujamos con la rotacion adecuada
    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(super.body.getPosition().x - (INTESTINE_WIDTH /2),
                super.body.getPosition().y - (INTESTINE_HEIGHT /2) );
        batch.draw(this.trIntestine,
                    getX(),getY(),
                    4f,4f,
                    INTESTINE_WIDTH,INTESTINE_HEIGHT,1,1,rotation);
    }

    //creamos el fixture
    protected void createFixtures() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(INTESTINE_WIDTH /2, 0.6f,new Vector2(0,(INTESTINE_HEIGHT-1.1f)/2),0);
        fixture1 = body.createFixture(shape, 8);
        fixture1.setUserData(USER_INTESTINE);

        shape.setAsBox(0.6f,INTESTINE_HEIGHT /2, new Vector2(-(INTESTINE_WIDTH-1.1f)/2,0),0);
        fixture2 = body.createFixture(shape, 8);
        fixture2.setUserData(USER_INTESTINE);

        shape.setAsBox(0.5f, 0.5f,new Vector2((INTESTINE_WIDTH-1.8f)/2,-(INTESTINE_HEIGHT-1.8f)/2),0);
        fixture3 = body.createFixture(shape, 8);
        fixture3.setUserData(USER_INTESTINE);

        shape.dispose();
    }
}
