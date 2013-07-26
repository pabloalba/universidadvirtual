package net.kaleidos.universidadvirtual;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Exit extends Rectangle {
    private int nextField;
    private int nextX;
    private int nextY;

    public int getNextField() {
        return nextField;
    }

    public void setNextField(int nextField) {
        this.nextField = nextField;
    }

    public int getNextX() {
        return nextX;
    }

    public void setNextX(int nextX) {
        this.nextX = nextX;
    }

    public int getNextY() {
        return nextY;
    }

    public void setNextY(int nextY) {
        this.nextY = nextY;
    }

    public Exit(final float pX, final float pY, final float pW, final float pH, final VertexBufferObjectManager pVertexBufferObjectManager, int nextField, int nextX, int nextY) {
        super(pX, pY, pW, pH, pVertexBufferObjectManager);
        this.nextField = nextField;
        this.nextX = nextX;
        this.nextY = nextY;
    }

}
