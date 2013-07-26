package net.kaleidos.universidadvirtual;

import android.content.Intent;
import android.graphics.Typeface;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;

import java.io.IOException;
import java.io.InputStream;

public class MainMenuActivity extends SimpleBaseGameActivity implements IOnMenuItemClickListener {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;

    protected static final int MENU_QUIT = 0;
    protected static final int MENU_START = 1;


    // ===========================================================
    // Fields
    // ===========================================================

    protected Camera mCamera;

    protected Scene mMainScene;

    private BitmapTextureAtlas mBitmapTextureAtlas;

    protected MenuScene mMenuScene;
    protected Scene mBackgroundScene;

    private Font mDroidFont;
    private Font mFontSmall;

    private ITextureRegion mFaceTextureRegion;
    private ITextureRegion mBackgroundTextureRegion;


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

        FontFactory.setAssetBasePath("font/");


        final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        this.mDroidFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "Droid.ttf", 48, true, android.graphics.Color.WHITE);
        this.mDroidFont.load();

        this.mFontSmall = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
        this.mFontSmall.load();


        BitmapTexture backgroundTexture = null;
        try {
            backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getResources().openRawResource(R.drawable.universidadvirtual_bg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();


    }

    @Override
    public Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.createMenuScene();


        this.mMainScene = new Scene();
        this.mMainScene.setBackgroundEnabled(false);
        this.mMainScene.attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));

        this.mMainScene.setChildScene(this.mMenuScene, false, true, true);

        return this.mMainScene;
    }


    @Override
    public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case MENU_QUIT:
                /* End Activity. */
                this.finish();
                System.exit(0);
                return true;
            case MENU_START:
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, PlayerSelectionActivity.class));
                MainMenuActivity.this.finish();
                return true;
            default:
                return false;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void createMenuScene() {
        this.mMenuScene = new MenuScene(this.mCamera);


        Color pSelectedColor = Color.RED;
        Color pUnselectedColor = Color.CYAN;
        Color pQuitColor = Color.CYAN;

        final IMenuItem headerMenu =
                new ColorMenuItemDecorator(
                        new TextMenuItem(100, mDroidFont, "UNIVERSIDAD VIRTUAL", this.getVertexBufferObjectManager()), Color.BLUE, Color.BLUE);

        this.mMenuScene.addMenuItem(headerMenu);

        final IMenuItem subHeaderMenu1 =
                new ColorMenuItemDecorator(
                        new TextMenuItem(101, mFontSmall, "Elige un avatar, un aula donde aprender", this.getVertexBufferObjectManager()), Color.BLUE, Color.BLUE);

        this.mMenuScene.addMenuItem(subHeaderMenu1);

        final IMenuItem subHeaderMenu2 =
                new ColorMenuItemDecorator(
                        new TextMenuItem(102, mFontSmall, "y ¡comparte conocimiento!", this.getVertexBufferObjectManager()), Color.BLUE, Color.BLUE);

        this.mMenuScene.addMenuItem(subHeaderMenu2);

        final IMenuItem onePMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_START, mDroidFont, "COMENZAR", this.getVertexBufferObjectManager()), pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(onePMenuItem);


        final IMenuItem quitMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_QUIT, mDroidFont, "SALIR", this.getVertexBufferObjectManager()), pSelectedColor, pQuitColor);

        this.mMenuScene.addMenuItem(quitMenuItem);


        this.mMenuScene.buildAnimations();

        this.mMenuScene.setBackgroundEnabled(false);

        this.mMenuScene.setOnMenuItemClickListener(this);
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
