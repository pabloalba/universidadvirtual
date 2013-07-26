package net.kaleidos.universidadvirtual;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends AnimatedSprite {

    public final static String SPRITE_WOMAN01 = "gpx/woman01.png";
    public final static String SPRITE_WOMAN02 = "gpx/woman02.png";
    public final static String SPRITE_MAN01 = "gpx/man01.png";
    public final static String SPRITE_MAN02 = "gpx/man02.png";


    private int lastMove = GameOneActivity.MOVE_RIGHT;
    private int numMoves = 0;

    public Player(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, (TiledTextureRegion) pTextureRegion, pVertexBufferObjectManager);
        this.setCurrentTileIndex(8);
    }


    public void animate(int direction) {
        boolean change = false;

        if (direction == lastMove) {
            numMoves++;
            if (numMoves == 5) {
                change = true;
                numMoves = 0;
            }
        } else {
            change = true;
            numMoves = 0;
        }

        if (change) {

            if (direction == GameOneActivity.MOVE_DOWN) {
                this.nextAnimation(0, 3);
            }

            if (direction == GameOneActivity.MOVE_UP) {
                this.nextAnimation(12, 15);
            }


            if (direction == GameOneActivity.MOVE_LEFT) {
                this.nextAnimation(4, 7);
            }

            if (direction == GameOneActivity.MOVE_RIGHT) {
                this.nextAnimation(8, 11);
            }

        }

        lastMove = direction;
    }


    private void nextAnimation(int min, int max) {
        int current = this.getCurrentTileIndex();
        if ((current >= min) && (current <= max)) {
            current++;
            if (current > max) {
                current = min;
            }
        } else {
            current = min;
        }
        this.setCurrentTileIndex(current);
    }
}
