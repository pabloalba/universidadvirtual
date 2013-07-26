package net.kaleidos.universidadvirtual;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.GLES20;
import android.webkit.WebView;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameOneActivity extends BaseGameActivity {

    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;
    public static final int LAYER_BACKGROUND = 0;
    public static final int LAYER_PLAYER = 1;
    public static final int LAYER_TEXT = 3;
    public static final int LAYER_COUNT = 4;

    public static final int MOVE_NONE = 0;
    public static final int MOVE_UP = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_DOWN = 3;
    public static final int MOVE_LEFT = 4;


    public static final int STATUS_PLAYER1_MOVE = 1;
    public static final int STATUS_PLAYER1_TEACHER = 2;
    public static final int STATUS_PLAYER1_EXIT = 3;
    public static final int STATUS_PLAYER1_END_VIDEO = 4;

    public static String SelectedPlayerSprite = Player.SPRITE_WOMAN01;


    private TiledTextureRegion mPlayer1TextureRegion;
    private ITextureRegion mTeacherTextureRegion;
    private ITextureRegion[] mBackgroundTextureRegion = new ITextureRegion[6];


    private ITextureRegion mOnScreenControlBaseTextureRegion;
    private ITextureRegion mOnScreenControlKnobTextureRegion;

    int lastMove = MOVE_NONE;

    Font mFont;

    Scene scene;
    Camera camera;


    byte status = STATUS_PLAYER1_MOVE;

    Player player1;
    Text debugText;

    Teacher teacher;

    Sprite[] background = new Sprite[6];

    GameField gameField = new GameField();
    GameEngineOnePlayer gameEngine;

    ArrayList<Rectangle> obstacles = new ArrayList<Rectangle>();
    ArrayList<Exit> exits = new ArrayList<Exit>();

    Exit exit = null;

    WebView webView;


    public void setExit(Exit exit) {
        this.exit = exit;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ArrayList<Rectangle> getObstacles() {
        return obstacles;
    }

    public ArrayList<Exit> getExits() {
        return exits;
    }

    public GameField getGameField() {
        return this.gameField;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 384, TextureOptions.BILINEAR);
        this.mPlayer1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, this, SelectedPlayerSprite, 0, 0, 4, 4);
        textureAtlas.load();


        BitmapTextureAtlas teacherTexture = new BitmapTextureAtlas(this.getTextureManager(), 32, 48, TextureOptions.BILINEAR);
        this.mTeacherTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(teacherTexture, this, "gpx/teacher.png", 0, 0);
        teacherTexture.load();


        BitmapTextureAtlas onScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
        this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(onScreenControlTexture, this, "gpx/onscreen_control_base.png", 0, 0);
        this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(onScreenControlTexture, this, "gpx/onscreen_control_knob.png", 128, 0);
        onScreenControlTexture.load();

        this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, true, Color.WHITE);
        this.mFont.load();


        BitmapTexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.stadium);
            }
        });

        this.mBackgroundTextureRegion[0] = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();

        backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.aula);
            }
        });

        this.mBackgroundTextureRegion[1] = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();

        backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.aula2);
            }
        });

        this.mBackgroundTextureRegion[2] = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();

        backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.aula3);
            }
        });

        this.mBackgroundTextureRegion[3] = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();

        backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.aula4);
            }
        });

        this.mBackgroundTextureRegion[4] = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();

        backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.aula5);
            }
        });

        this.mBackgroundTextureRegion[5] = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();


        onCreateResourcesCallback.onCreateResourcesFinished();
    }


    public void mainLoop(float pSecondsElapsed) {
        status = gameEngine.mainLoop(this, pSecondsElapsed, status, lastMove);

        if (status == STATUS_PLAYER1_EXIT) {
            loadGameField(exit.getNextField(), exit.getNextX(), exit.getNextY());
            exit = null;
            status = STATUS_PLAYER1_MOVE;

        } else if (status == STATUS_PLAYER1_TEACHER) {
            openWebViewURL(teacher.getUrl());
            player1.setY(teacher.getY() + 50);
            status = STATUS_PLAYER1_END_VIDEO;

        }


    }


    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        scene = new Scene();

        scene.registerUpdateHandler(new IUpdateHandler() {
            public void reset() {
            }

            public void onUpdate(float pSecondsElapsed) {
                GameOneActivity.this.mainLoop(pSecondsElapsed);
            }
        });

        for (int i = 0; i < LAYER_COUNT; i++) {
            this.scene.attachChild(new Entity());
        }


        //load backgrounds
        for (int i = 0; i < 6; i++) {
            background[i] = new Sprite(0, 0, this.mBackgroundTextureRegion[i], this.getVertexBufferObjectManager());
        }





        /* No background color needed as we have a fullscreen background sprite. */
        scene.setBackgroundEnabled(false);
        scene.getChildByIndex(LAYER_BACKGROUND).attachChild(background[0]);


        final float centerY = (CAMERA_HEIGHT) / 2;


        player1 = new Player(0, centerY, this.mPlayer1TextureRegion, this.getVertexBufferObjectManager());
        teacher = new Teacher(200, 200, this.mTeacherTextureRegion, this.getVertexBufferObjectManager());

        scene.getChildByIndex(LAYER_PLAYER).attachChild(player1);


        debugText = new Text(0, 100, this.mFont, "                                                                                    ", new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_TEXT).attachChild(debugText);

        initOnScreenControls();


        this.gameEngine = new GameEngineOnePlayer();

        loadGameField(0, 390, 450);

        onCreateSceneCallback.onCreateSceneFinished(scene);
    }


    private void initOnScreenControls() {

        final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.camera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new BaseOnScreenControl.IOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
                GameOneActivity.this.lastMove = MOVE_NONE;


                if (pValueX > 0) { //RIGHT

                    GameOneActivity.this.lastMove += MOVE_RIGHT;
                } else if (pValueX < 0) { //LEFT

                    GameOneActivity.this.lastMove += MOVE_LEFT;
                }

                if (pValueY > 0) { //DOWN

                    GameOneActivity.this.lastMove += MOVE_DOWN;
                } else if (pValueY < 0) { //UP

                    GameOneActivity.this.lastMove += MOVE_UP;
                }
            }
        });

        digitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        digitalOnScreenControl.getControlBase().setAlpha(0.5f);
        digitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
        digitalOnScreenControl.getControlBase().setScale(1.25f);
        digitalOnScreenControl.getControlKnob().setScale(1.25f);
        digitalOnScreenControl.refreshControlKnobPosition();

        this.scene.setChildScene(digitalOnScreenControl);

    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) throws Exception {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    private void debug(Object text) {
        GameOneActivity.this.debugText.setText("" + text);
    }

    private void loadGameField(int numField, int x, int y) {

        obstacles = new ArrayList<Rectangle>();
        exits = new ArrayList<Exit>();

        switch (numField) {
            case 0:
                //change background

                scene.getChildByIndex(LAYER_BACKGROUND).detachChildren();
                scene.getChildByIndex(LAYER_BACKGROUND).attachChild(background[0]);

                scene.getChildByIndex(LAYER_PLAYER).detachChildren();
                scene.getChildByIndex(LAYER_PLAYER).attachChild(player1);
                player1.setPosition(x, y);
                teacher.setPosition(-100, -100);

                gameField.setLimitUp(0);
                gameField.setLimitDown(480);
                gameField.setLimitLeft(0);
                gameField.setLimitRight(800);

                //obstacles
                obstacles.add(new Rectangle(97, 32, 95, 80, this.getVertexBufferObjectManager()));
                obstacles.add(new Rectangle(610, 32, 95, 80, this.getVertexBufferObjectManager()));
                obstacles.add(new Rectangle(127, 289, 95, 62, this.getVertexBufferObjectManager()));
                obstacles.add(new Rectangle(544, 290, 94, 70, this.getVertexBufferObjectManager()));
                obstacles.add(new Rectangle(353, 0, 94, 104, this.getVertexBufferObjectManager()));


                //exits
                exits.add(new Exit(129, 91, 30, 30, this.getVertexBufferObjectManager(), 1, 390, 450));
                exits.add(new Exit(386, 73, 30, 35, this.getVertexBufferObjectManager(), 2, 390, 450));
                exits.add(new Exit(642, 89, 30, 30, this.getVertexBufferObjectManager(), 3, 390, 450));
                exits.add(new Exit(162, 330, 30, 30, this.getVertexBufferObjectManager(), 4, 390, 450));
                exits.add(new Exit(579, 331, 30, 35, this.getVertexBufferObjectManager(), 5, 390, 450));


                break;


            case 1:
                createRoom(numField, 129, 135, "https://www.youtube.com/watch?v=LV1equSyI8w");
                break;
            case 2:
                createRoom(numField, 386, 115, "https://www.youtube.com/watch?v=zeq1Q-XJACs");
                break;
            case 3:
                createRoom(numField, 682, 130, "https://www.youtube.com/watch?v=muWZDte5pL0");
                break;
            case 4:
                createRoom(numField, 162, 375, "https://www.youtube.com/watch?v=sLR7z-uZ5oY");
                break;
            case 5:
                createRoom(numField, 579, 375, "https://www.youtube.com/watch?v=sHWmvswuNKQ");
                break;

        }


    }

    private void createRoom(int num, int nextX, int nextY, String url) {
        scene.getChildByIndex(LAYER_BACKGROUND).detachChildren();
        scene.getChildByIndex(LAYER_BACKGROUND).attachChild(background[num]);

        scene.getChildByIndex(LAYER_PLAYER).detachChildren();
        scene.getChildByIndex(LAYER_PLAYER).attachChild(player1);
        player1.setPosition(390, 420);
        scene.getChildByIndex(LAYER_PLAYER).attachChild(teacher);
        teacher.setPosition(390, 20);

        teacher.setUrl(url);

        gameField.setLimitUp(0);
        gameField.setLimitDown(480);
        gameField.setLimitLeft(230);
        gameField.setLimitRight(570);


        //obstacles
        obstacles.add(new Rectangle(261, 77, 87, 51, this.getVertexBufferObjectManager()));
        obstacles.add(new Rectangle(261, 173, 87, 51, this.getVertexBufferObjectManager()));
        obstacles.add(new Rectangle(261, 269, 87, 51, this.getVertexBufferObjectManager()));
        obstacles.add(new Rectangle(261, 365, 87, 51, this.getVertexBufferObjectManager()));
        obstacles.add(new Rectangle(453, 77, 87, 51, this.getVertexBufferObjectManager()));
        obstacles.add(new Rectangle(453, 173, 87, 51, this.getVertexBufferObjectManager()));
        obstacles.add(new Rectangle(453, 269, 87, 51, this.getVertexBufferObjectManager()));
        obstacles.add(new Rectangle(453, 365, 87, 51, this.getVertexBufferObjectManager()));

        //exits
        exits.add(new Exit(383, 475, 32, 5, this.getVertexBufferObjectManager(), 0, nextX, nextY));

    }


    private void openWebViewURL(String url) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }


}
