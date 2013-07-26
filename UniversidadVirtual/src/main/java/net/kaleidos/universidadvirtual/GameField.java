package net.kaleidos.universidadvirtual;

/**
 * Created by palba on 17/07/13.
 */
public class GameField {

    private int limitDown = 430;
    private int limitUp = 0;
    private int limitLeft = 0;
    private int limitRight = 800;

    public GameField() {
    }

    public GameField(int limitDown, int limitUp, int limitLeft, int limitRight) {
        this.limitDown = limitDown;
        this.limitUp = limitUp;
        this.limitLeft = limitLeft;
        this.limitRight = limitRight;
    }

    public void setLimitDown(int limitDown) {
        this.limitDown = limitDown;
    }

    public void setLimitUp(int limitUp) {
        this.limitUp = limitUp;
    }

    public void setLimitLeft(int limitLeft) {
        this.limitLeft = limitLeft;
    }

    public void setLimitRight(int limitRight) {
        this.limitRight = limitRight;
    }

    public int getLimitDown() {
        return limitDown;
    }


    public int getLimitUp() {
        return limitUp;
    }


    public int getLimitLeft() {
        return limitLeft;
    }


    public int getLimitRight() {
        return limitRight;
    }


}
