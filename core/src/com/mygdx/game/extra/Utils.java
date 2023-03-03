package com.mygdx.game.extra;

public class Utils {
    //UNIDADES DE LA PANTALLA EN PIXELES
    public static final int SCREEN_HEIGHT = 800;
    public static final int SCREEN_WIDTH = 450;

    //UNIDADES DEL MUNDO VIRTUAL
    public static final float WORLD_HEIGHT = 16f;
    public static final float WORLD_WIDTH = 9f;


    //IDENTIFICADORES DEL ATLAS DE IMAGENES
    public static final String ATLAS_MAP = "fisheAtlas";
    public static final String BACKGROUND_IMAGE = "screen";
    public static final String GET_READY_IMAGE = "startScreen";
    public static final String GAME_OVER_IMAGE = "finishScreen";
    public static final String CORNER = "corner";
    public static final String FISH1 = "pez1";
    public static final String FISH2 = "pez2";
    public static final String FISH3 = "pez3";
    public static final String FISH4 = "pez4";
    public static final String STREET = "street";

    //IDENTIFICADORES DE LAS FUENTES
    public static final String FONT_FNT = "mifont.fnt";
    public static final String FONT_PNG = "mifont.png";

    //IDENTIFICADORES DE SONIDOS Y MUSICAS
    public static final String GAMEOVER_SOUND = "wc.mp3";
    public static final String HIT_SOUND = "mumiScream.mp3";
    public static final String MUSIC_BG = "heyYaLowQuality.mp3";

    //IDENTIFICADORE DE CUERPOS PARA COLISIONES (yo solo tengo estos dos cuerpos)
    public static final String USER_FISH = "fish";
    public static final String USER_INTESTINE ="intestine";
}
