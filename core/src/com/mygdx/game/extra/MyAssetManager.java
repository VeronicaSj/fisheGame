package com.mygdx.game.extra;

import static com.mygdx.game.extra.Utils.ATLAS_MAP;
import static com.mygdx.game.extra.Utils.BACKGROUND_IMAGE;
import static com.mygdx.game.extra.Utils.CORNER;
import static com.mygdx.game.extra.Utils.FISH1;
import static com.mygdx.game.extra.Utils.FISH2;
import static com.mygdx.game.extra.Utils.FISH3;
import static com.mygdx.game.extra.Utils.FISH4;
import static com.mygdx.game.extra.Utils.FONT_FNT;
import static com.mygdx.game.extra.Utils.FONT_PNG;
import static com.mygdx.game.extra.Utils.GAMEOVER_SOUND;
import static com.mygdx.game.extra.Utils.GAME_OVER_IMAGE;
import static com.mygdx.game.extra.Utils.GET_READY_IMAGE;
import static com.mygdx.game.extra.Utils.HIT_SOUND;
import static com.mygdx.game.extra.Utils.MUSIC_BG;
import static com.mygdx.game.extra.Utils.STREET;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.actors.Corner;

public class MyAssetManager {

    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public MyAssetManager(){
        this.assetManager = new AssetManager();

        //cargamos la imagen
        assetManager.load(ATLAS_MAP, TextureAtlas.class);

        //cargamos los sonidos
        assetManager.load(GAMEOVER_SOUND, Sound.class);
        assetManager.load(HIT_SOUND, Sound.class);
        assetManager.load(MUSIC_BG, Music.class);

        //terminamos de cargar recursos
        assetManager.finishLoading();

        this.textureAtlas = assetManager.get(ATLAS_MAP);
    }

    //IMAGENES DE FONDO
    public TextureRegion getBackground(){
        return this.textureAtlas.findRegion(BACKGROUND_IMAGE);
    }
    public TextureRegion getGameOverBackground(){ return this.textureAtlas.findRegion(GAME_OVER_IMAGE);}
    public TextureRegion getGetReadyBackground(){ return this.textureAtlas.findRegion(GET_READY_IMAGE);}

    //ANIMACIÓN PEZ
    public Animation<TextureRegion> getFishAnimation(){
        return new Animation<TextureRegion>(0.25f,//en un segundo hay 4 keyFrame
                textureAtlas.findRegion(FISH1),
                textureAtlas.findRegion(FISH2),
                textureAtlas.findRegion(FISH3),
                textureAtlas.findRegion(FISH4));

    }

    //TEXTURAS DE LOS INTESTINOS
    public TextureRegion getStreet(){
        return this.textureAtlas.findRegion(STREET);
    }
    public TextureRegion getCorner(){
        return this.textureAtlas.findRegion(CORNER);
    }

    //SONIDOS
    public Sound getHitSound(){ return this.assetManager.get(HIT_SOUND); }
    public Sound getGameOverSound(){ return this.assetManager.get(GAMEOVER_SOUND);}

    //MUSICA
    public Music getMusicBG(){ return this.assetManager.get(MUSIC_BG);}

    //LETRAS
    public BitmapFont getFont(){
        return new BitmapFont(Gdx.files.internal(FONT_FNT),Gdx.files.internal(FONT_PNG), false);
    }
}
