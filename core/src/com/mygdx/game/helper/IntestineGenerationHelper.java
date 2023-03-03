package com.mygdx.game.helper;

import static com.mygdx.game.actors.Intestine.Direction.DIRECTION_TO_FLOOR;
import static com.mygdx.game.actors.Intestine.Direction.DIRECTION_TO_LEFT;
import static com.mygdx.game.actors.Intestine.Direction.DIRECTION_TO_RIGHT;
import static com.mygdx.game.actors.Intestine.ORIENTATION_CORNER_LEFT_TO_FLOR;
import static com.mygdx.game.actors.Intestine.ORIENTATION_CORNER_RIGHT_TO_FLOR;
import static com.mygdx.game.actors.Intestine.ORIENTATION_CORNER_TOP_TO_LEFT;
import static com.mygdx.game.actors.Intestine.ORIENTATION_CORNER_TOP_TO_RIGHT;
import static com.mygdx.game.actors.Intestine.ORIENTATION_STREET_HORIZONTAL;
import static com.mygdx.game.actors.Intestine.ORIENTATION_STREET_VERTICAL;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.actors.Corner;
import com.mygdx.game.actors.Intestine;
import com.mygdx.game.actors.Street;
import com.mygdx.game.extra.MyAssetManager;

/**
 * Clase que nos ayuda a gestionar la generacion de camino. El camino se genera de forma aleatoria.
 *
 * Seguiremos unas normas de generacion sencillas para que el juego sea jugable:
 *
 * Si el camino va hacia abajo puede seguir hacia abajo, girar a la izquierda o girar a la derecha.
 * Si el camino va hacia la derecha puede seguir hacia la derecha o girar hacia abajo
 * Si el camino va hacia la izquierda puede seguir hacia la izquierda o girar hacia abajo
 */
public class IntestineGenerationHelper{

    public IntestineGenerationHelper() {}

    /**
     * Funcion que llama a otras funciones para decidir cual es el tipo de Corner que toca crear
     * para despues crearlo y devolverlo
     *
     * @param intestine
     * @param assetMan
     * @return new Corner(...)
     */
    //la siguiente funcion se ha hecho con un switch por limpieza de codigo y para evitar que yo pierda la cabeza
    public Intestine createCorner(Intestine intestine, MyAssetManager assetMan){
        float[] variationArray= new float[3];
        Intestine.Direction[] directionArray= new Intestine.Direction[2];

        switch (whichCorner(intestine)){
            case "StreetFloorRight":
                createCornerStreetFloorRight(intestine,variationArray,directionArray);
                break;
            case "CornerFloorRight":
                createCornerCornerFloorRight(intestine,variationArray,directionArray);
                break;
            case "StreetFloorLeft":
                createCornerStreetFloorLeft(intestine,variationArray,directionArray);
                break;
            case "CornerFloorLeft":
                createCornerCornerFloorLeft(intestine,variationArray,directionArray);
                break;
            case "StreetRightFloor":
                createCornerStreetRightFloor(intestine,variationArray,directionArray);
                break;
            case "CornerRightFloor":
                createCornerCornerRightFloor(intestine,variationArray,directionArray);
                break;
            case "StreetLeftFloor":
                createCornerStreetLeftFloor(intestine,variationArray,directionArray);
                break;
            case "CornerLeftFloor":
                createCornerCornerLeftFloor(intestine,variationArray,directionArray);
                break;
        }

        return new Corner(assetMan.getCorner(),intestine.getWorld(),
                new Vector2(variationArray[0],variationArray[1]),
                variationArray[2],
                directionArray[0],directionArray[1]);
    }

    /**
     * Funcion que llama a otras funciones para decidir cual es el tipo de Street que toca crear
     * para despues crearlo y devolverlo
     *
     * @param intestine
     * @param assetMan
     * @return new Street(...)
     */
    //la siguiente funcion se ha hecho con un switch por limpieza de codigo y para evitar que yo pierda la cabeza
    public Intestine createStreet(Intestine intestine, MyAssetManager assetMan){
        float[] variationList= new float[3];
        Intestine.Direction dir= Intestine.Direction.DIRECTION_TO_FLOOR;

        switch (whichStreet(intestine)){
            case "StreetFloor":
                dir=createStreetStreetFloor(intestine,variationList);
                break;
            case "CornerFloor":
                dir=createStreetCornerFloor(intestine,variationList);
                break;
            case "StreetLeft":
                dir=createStreetStreetLeft(intestine,variationList);
                break;
            case "CornerLeft":
                dir=createStreetCornerLeft(intestine,variationList);
                break;
            case "StreetRight":
                dir=createStreetStreetRight(intestine,variationList);
                break;
            case "CornerRight":
                dir=createStreetCornerRight(intestine,variationList);
                break;
        }

        TextureRegion texture=assetMan.getStreet();
        return new Street(texture,intestine.getWorld(),
                new Vector2(variationList[0],variationList[1]),
                variationList[2],dir);
    }

    /**
     * Funcion que escoge el tipo de Corner que necesitamos segun:
     *      la forma del intestino que hay justo antes
     *      la direccion del intestino que hay justo antes
     *      la direccion final del intestino que vamos a crear
     *
     * @param intestine
     * @return
     */
    private String whichCorner(Intestine intestine){
        //el tipo de Corner esta marcado por tres variables:
        //la forma anterior, la direccion inicial y la direccion final
        String lastShape="";
        String initDirection="";
        String finalDirection="";

        //obtenemos la forma anterior
        if(intestine instanceof Street){
            lastShape="Street";
        }else{
            lastShape="Corner";
        }

        //obtenemos la direccion inicial
        switch(intestine.getDirection()){
            case DIRECTION_TO_FLOOR:
                initDirection="Floor";
                finalDirection=getRandomDirection();
                break;
            case DIRECTION_TO_LEFT:
                initDirection="Left";
                finalDirection="Floor";
                break;
            case DIRECTION_TO_RIGHT:
                initDirection="Right";
                finalDirection="Floor";
                break;
        }
        return lastShape+initDirection+finalDirection;
    }

    /**
     * Funcion auxiliar. Cuando la direccion inicial es hacia abajo, la nueva esquina podrá tener la
     * direccion final hacia la derecha o hacia la izquierda de manara aleatoria
     *
     * @return
     */
    private String getRandomDirection(){
        if((int)(Math.random()*2)==0){
            return "Left";
        }else{
            return "Right";
        }
    }

    /**
     * Funcion que escoge el tipo de Street que necesitamos segun:
     *      la forma del intestino que hay justo antes
     *      la direccion del intestino que hay justo antes
     *
     * @param intestine
     * @return
     */
    private String whichStreet(Intestine intestine){
        //el tipo de Street esta marcado por dos variables:
        //la forma anterior y la direccion
        String lastShape="";
        String direction="";
        if(intestine instanceof Street){
            lastShape="Street";
        }else{
            lastShape="Corner";
        }

        //obtenemos la direccion inicial
        switch(intestine.getDirection()){
            case DIRECTION_TO_FLOOR:
                direction="Floor";
                break;
            case DIRECTION_TO_LEFT:
                direction="Left";
                break;
            case DIRECTION_TO_RIGHT:
                direction="Right";
                break;
        }

        return lastShape+direction;
    }


    /*
    **********************************************************************************
    * FUNCIONES AUXILIARES QUE DAN LOS VALORES DE CREACION  ADECUADOS A LOS CORNER
    *
    * Los valores decimales que hay en las posiciones x o y son un pequeño ajuste
    * ha habido que ir probando un poco
    *
    * Los valores no decimales son calculos hachos a partir del tamaño de los intestinos segun
    * corresponde en cada caso
    * ********************************************************************************
     */

    private void createCornerStreetFloorRight(Intestine intestine,
                                    float[] variationList, Intestine.Direction[] directionArray){
        variationList[0]=intestine.getBody().getPosition().x;//position x
        variationList[1]=intestine.getBody().getPosition().y-6;//position y
        variationList[2]=ORIENTATION_CORNER_TOP_TO_RIGHT;//orientacion
        directionArray[0]= DIRECTION_TO_FLOOR;
        directionArray[1]=DIRECTION_TO_RIGHT;
    }
    private void createCornerCornerFloorRight(Intestine intestine,
                                    float[] variationList, Intestine.Direction[] directionArray){
        //necesitamos saber la  direccion inicial del intestino anterior
        //si el anterior iva hacia la derecha, sumar 0.7 a x
        //si el anterior era hacia la izquierda, no simar nada a x
        if(((Corner)intestine).getInitialDirection().equals(DIRECTION_TO_RIGHT)){
            variationList[0]=intestine.getBody().getPosition().x+0.7f;//position x
        }else{
            variationList[0]=intestine.getBody().getPosition().x;//position x
        }

        variationList[1]=intestine.getBody().getPosition().y-8;//position y
        variationList[2]=ORIENTATION_CORNER_TOP_TO_RIGHT;//orientacion

        directionArray[0]= DIRECTION_TO_FLOOR;
        directionArray[1]=DIRECTION_TO_RIGHT;
    }
    private void createCornerStreetFloorLeft(Intestine intestine,
                                       float[] variationList, Intestine.Direction[] directionArray){
        variationList[0]=intestine.getBody().getPosition().x;//position x
        variationList[1]=intestine.getBody().getPosition().y-6;//position y
        variationList[2]=ORIENTATION_CORNER_TOP_TO_LEFT;//orientacion
        directionArray[0]= DIRECTION_TO_FLOOR;
        directionArray[1]=DIRECTION_TO_LEFT;
    }
    private void createCornerCornerFloorLeft(Intestine intestine,
                                       float[] variationList, Intestine.Direction[] directionArray){
        //necesitamos saber la  direccion inicial del intestino anterior
        //si el anterior iva hacia la derecha, sumar 0.7 a x
        //si el anterior era hacia la izquierda, no simar nada a x
        if(((Corner)intestine).getInitialDirection().equals(DIRECTION_TO_RIGHT)){
            variationList[0]=intestine.getBody().getPosition().x+0.7f;//position x
        }else{
            variationList[0]=intestine.getBody().getPosition().x;//position x
        }

        variationList[1]=intestine.getBody().getPosition().y-8;//position y
        variationList[2]=ORIENTATION_CORNER_TOP_TO_LEFT;//orientacion
        directionArray[0]= DIRECTION_TO_FLOOR;
        directionArray[1]=DIRECTION_TO_LEFT;
    }
    private void createCornerStreetRightFloor(Intestine intestine,
                                        float[] variationList, Intestine.Direction[] directionArray){
        variationList[0]=intestine.getBody().getPosition().x+6;//position x
        variationList[1]=intestine.getBody().getPosition().y;//position y
        variationList[2]=ORIENTATION_CORNER_LEFT_TO_FLOR;//orientacion
        directionArray[0]= DIRECTION_TO_RIGHT;
        directionArray[1]=DIRECTION_TO_FLOOR;
    }
    private void createCornerCornerRightFloor(Intestine intestine,
                                    float[] variationList, Intestine.Direction[] directionArray){
        variationList[0]=intestine.getBody().getPosition().x+8;//position x
        variationList[1]=intestine.getBody().getPosition().y-0.70f;//position y
        variationList[2]=ORIENTATION_CORNER_LEFT_TO_FLOR;//orientacion
        directionArray[0]= DIRECTION_TO_RIGHT;
        directionArray[1]=DIRECTION_TO_FLOOR;
    }
    private void createCornerStreetLeftFloor(Intestine intestine,
                                       float[] variationList, Intestine.Direction[] directionArray){
        variationList[0]=intestine.getBody().getPosition().x-6;//position x
        variationList[1]=intestine.getBody().getPosition().y-0.1f;//position y
        variationList[2]=ORIENTATION_CORNER_RIGHT_TO_FLOR;//orientacion
        directionArray[0]= DIRECTION_TO_LEFT;
        directionArray[1]=DIRECTION_TO_FLOOR;
    }
    private void createCornerCornerLeftFloor(Intestine intestine,
                                       float[] variationList, Intestine.Direction[] directionArray){
        variationList[0]=intestine.getBody().getPosition().x-8;//position x
        variationList[1]=intestine.getBody().getPosition().y-0.70f;//position y
        variationList[2]=ORIENTATION_CORNER_RIGHT_TO_FLOR;//orientacion
        directionArray[0]= DIRECTION_TO_LEFT;
        directionArray[1]=DIRECTION_TO_FLOOR;
    }


    /*
     **********************************************************************************
     * FUNCIONES AUXILIARES QUE DAN LOS VALORES DE CREACION  ADECUADOS A LOS STREET
     *
     * Los valores decimales que hay en las posiciones x o y son un pequeño ajuste
     * ha habido que ir probando un poco
     *
     * Los valores no decimales son calculos hachos a partir del tamaño de los intestinos segun
     * corresponde en cada caso
     * ********************************************************************************
     */

    private Intestine.Direction createStreetStreetFloor(Intestine intestine, float[] variationList){
        variationList[0]=intestine.getBody().getPosition().x;//position x
        variationList[1]=intestine.getBody().getPosition().y-4;//position y
        variationList[2]=ORIENTATION_STREET_VERTICAL;//orientacion
        return DIRECTION_TO_FLOOR;
    }
    private Intestine.Direction createStreetCornerFloor (Intestine intestine, float[] variationList){
        //necesitamos conocer la direccion inicial de la esquina anterior
        //si la esquina anterior iva hacia la derecha, sumar 0.7f a x
        //si la esquina anterior iva hacia la izquierda, sumar 0 a x
        if(((Corner)intestine).getInitialDirection().equals(DIRECTION_TO_RIGHT)){
            variationList[0]=intestine.getBody().getPosition().x+0.7f;//position x
        }else{
            variationList[0]=intestine.getBody().getPosition().x;//position x
        }

        variationList[1]=intestine.getBody().getPosition().y-6;//position y
        variationList[2]=ORIENTATION_STREET_VERTICAL;//orientacion
        return DIRECTION_TO_FLOOR;
    }
    private Intestine.Direction createStreetStreetLeft(Intestine intestine, float[] variationList){
        variationList[0]=intestine.getBody().getPosition().x-4;//position x
        variationList[1]=intestine.getBody().getPosition().y;//position y
        variationList[2]=ORIENTATION_STREET_HORIZONTAL;//orientacion
        return DIRECTION_TO_LEFT;
    }
    private Intestine.Direction createStreetCornerLeft(Intestine intestine, float[] variationList){
        variationList[0]=intestine.getBody().getPosition().x-6;//position x
        variationList[1]=intestine.getBody().getPosition().y-0.70f;//position y
        variationList[2]=ORIENTATION_STREET_HORIZONTAL;//orientacion
        return DIRECTION_TO_LEFT;
    }
    private Intestine.Direction createStreetStreetRight(Intestine intestine, float[] variationList){
        variationList[0]=intestine.getBody().getPosition().x+4;//position x
        variationList[1]=intestine.getBody().getPosition().y;//position y
        variationList[2]=ORIENTATION_STREET_HORIZONTAL;//orientacion
        return DIRECTION_TO_RIGHT;
    }
    private Intestine.Direction createStreetCornerRight(Intestine intestine, float[] variationList){
        variationList[0]=intestine.getBody().getPosition().x+6;//position x
        variationList[1]=intestine.getBody().getPosition().y-0.70f;//position y
        variationList[2]=ORIENTATION_STREET_HORIZONTAL;//orientacion
        return DIRECTION_TO_RIGHT;
    }
}
