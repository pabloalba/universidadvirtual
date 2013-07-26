package net.kaleidos.universidadvirtual;

import org.andengine.entity.primitive.Rectangle;

import java.util.ArrayList;

public class GameEngineOnePlayer {

    public byte mainLoop(GameOneActivity game, float secondsElapsed, byte status, int lastMove) {
        byte newStatus = status;

        GameField gameField = game.getGameField();
        Player player1 = game.getPlayer1();
        ArrayList<Rectangle> obstacles = game.getObstacles();
        ArrayList<Exit> exits = game.getExits();
        Teacher teacher = game.getTeacher();

        switch (status) {
            case GameOneActivity.STATUS_PLAYER1_MOVE:
                newStatus = movePlayer1(game, player1, obstacles, exits, gameField, lastMove, secondsElapsed, teacher);
                break;
            case GameOneActivity.STATUS_PLAYER1_END_VIDEO:
                if (lastMove == GameOneActivity.MOVE_DOWN) {
                    newStatus = GameOneActivity.STATUS_PLAYER1_MOVE;
                }


        }
        return newStatus;
    }


    private byte movePlayer1(GameOneActivity game, Player player1, ArrayList<Rectangle> obstacles, ArrayList<Exit> exits, GameField gameField, int lastMove, float secondsElapsed, Teacher teacher) {
        byte status = GameOneActivity.STATUS_PLAYER1_MOVE;
        //Move player1
        float x = player1.getX();
        float y = player1.getY();
        float vel = 200;

        float oldX = player1.getX();
        float oldY = player1.getY();


        if (lastMove == GameOneActivity.MOVE_RIGHT) {
            x += vel * secondsElapsed;
        } else if (lastMove == GameOneActivity.MOVE_LEFT) {
            x -= vel * secondsElapsed;
        } else if (lastMove == GameOneActivity.MOVE_UP) {
            y -= vel * secondsElapsed;
        } else if (lastMove == GameOneActivity.MOVE_DOWN) {
            y += vel * secondsElapsed;
        }

        player1.setPosition(x, y);

        for (int i = 0; i < obstacles.size(); i++) {
            if (player1.collidesWith(obstacles.get(i))) {
                player1.setPosition(oldX, oldY);
            }

        }

        for (int i = 0; i < exits.size(); i++) {
            if (player1.collidesWith(exits.get(i))) {
                status = GameOneActivity.STATUS_PLAYER1_EXIT;
                game.setExit(exits.get(i));
            }

        }

        if (player1.collidesWith(teacher)) {
            status = GameOneActivity.STATUS_PLAYER1_TEACHER;
            player1.setPosition(oldX, teacher.getY() + 50);
        }

        x = player1.getX();
        y = player1.getY();

        //Check limits player 1
        if (x + player1.getWidth() > gameField.getLimitRight()) {
            x = gameField.getLimitRight() - player1.getWidth();
        } else if (x < gameField.getLimitLeft()) {
            x = gameField.getLimitLeft();
        }

        if (y < gameField.getLimitUp()) {
            y = gameField.getLimitUp();
        } else if (y + player1.getHeight() > gameField.getLimitDown()) {
            y = gameField.getLimitDown() - player1.getHeight();
        }

        player1.setPosition(x, y);


        player1.animate(lastMove);

        return status;
    }


}
