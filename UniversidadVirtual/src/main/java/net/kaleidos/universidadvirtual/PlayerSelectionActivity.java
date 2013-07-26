package net.kaleidos.universidadvirtual;


import android.content.Intent;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

public class PlayerSelectionActivity extends SimpleBaseGameActivity {
    // ===========================================================
    // Constants
    // ===========================================================


    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    // ===========================================================
    // Fields
    // ===========================================================

    protected Camera mCamera;

    protected Scene mMainScene;


    protected MenuScene mMenuScene;
    protected Scene mBackgroundScene;

    private BitmapTextureAtlas mWoman1Texture;
    private ITextureRegion mWoman1TextureRegion;

    private BitmapTextureAtlas mWoman2Texture;
    private ITextureRegion mWoman2TextureRegion;

    private BitmapTextureAtlas mMan1Texture;
    private ITextureRegion mMan1TextureRegion;


    private BitmapTextureAtlas mMan2Texture;
    private ITextureRegion mMan2TextureRegion;


    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
    }

    @Override
    public void onCreateResources() {


        BitmapTextureAtlas textureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mWoman1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas1, this, "gpx/woman01_detail.png", 0, 0);
        textureAtlas1.load();

        BitmapTextureAtlas textureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mWoman2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas2, this, "gpx/woman02_detail.png", 0, 0);
        textureAtlas2.load();

        BitmapTextureAtlas textureAtlas3 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mMan1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas3, this, "gpx/man01_detail.png", 0, 0);
        textureAtlas3.load();

        BitmapTextureAtlas textureAtlas4 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mMan2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas4, this, "gpx/man02_detail.png", 0, 0);
        textureAtlas4.load();

    }

    @Override
    public Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.mMainScene = new Scene();
        this.mMainScene.setBackground(new Background(Color.WHITE));


        /* Create the button and add it to the scene. */
        final Sprite buttonWoman1 = new ButtonSprite(0, 0, this.mWoman1TextureRegion, this.mWoman1TextureRegion, this.mWoman1TextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(0);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonWoman1);
        this.mMainScene.attachChild(buttonWoman1);

        /* Create the button and add it to the scene. */
        final Sprite buttonWoman2 = new ButtonSprite(400, 0, this.mWoman2TextureRegion, this.mWoman2TextureRegion, this.mWoman2TextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(1);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonWoman2);
        this.mMainScene.attachChild(buttonWoman2);

        /* Create the button and add it to the scene. */
        final Sprite buttonMan1 = new ButtonSprite(0, 240, this.mMan1TextureRegion, this.mMan1TextureRegion, this.mMan1TextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(2);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonMan1);
        this.mMainScene.attachChild(buttonMan1);

        /* Create the button and add it to the scene. */
        final Sprite buttonMan2 = new ButtonSprite(400, 240, this.mMan2TextureRegion, this.mMan2TextureRegion, this.mMan2TextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(3);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonMan2);
        this.mMainScene.attachChild(buttonMan2);


        this.mMainScene.setTouchAreaBindingOnActionDownEnabled(true);


        return this.mMainScene;
    }


    public boolean selectPlayer(int numPlayer) {


        if (numPlayer == 0) {
            GameOneActivity.SelectedPlayerSprite = Player.SPRITE_WOMAN01;
        }

        if (numPlayer == 1) {
            GameOneActivity.SelectedPlayerSprite = Player.SPRITE_WOMAN02;
        }

        if (numPlayer == 2) {
            GameOneActivity.SelectedPlayerSprite = Player.SPRITE_MAN01;
        }

        if (numPlayer == 3) {
            GameOneActivity.SelectedPlayerSprite = Player.SPRITE_MAN02;
        }

        PlayerSelectionActivity.this.startActivity(new Intent(PlayerSelectionActivity.this, GameOneActivity.class));


        PlayerSelectionActivity.this.finish();
        return true;
    }


}
