/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.ai;

import game.core.ReversiGame;
import game.core.ReversiGameEvent;
import java.util.Random;
import util.Coordinate2D;
import util.RunningTools;

/**
 *
 * @author filipe
 */
public class ComputerPlayer implements Runnable {
    
    private ReversiGame actualGame;
    private ReversiGameEvent currentEvent;
    private boolean myTurn;
    private boolean running;
    private char myColor;
    private AISettings mySettings;
    
    public ComputerPlayer(char myColor, ReversiGame actualGame, AISettings settings) {
        this.myColor = myColor;
        this.actualGame = actualGame;
        this.running = true;
        currentEvent = null;
        mySettings = settings;
    }
    
    private void makeMove() {
        if (currentEvent.isGamePlayable()) {
            if (currentEvent.isTurnPlayable()) {
                Coordinate2D cellToPlay = chooseMovement();
                if (cellToPlay != null) actualGame.put(cellToPlay.x, cellToPlay.y, myColor);
                else actualGame.skip(myColor);
            } else {
                actualGame.skip(myColor);
            }
        }
    }
    
    public void updateSettings(AISettings settings) {
        mySettings = settings;
        mySettings.calculateBiasMatrix();
    }
    
    public AISettings getSettings() {
        return mySettings;
    }
    
    private Coordinate2D chooseMovement() {
        if (mySettings.getMaxTreeLevel() == 0) {
            return getRandomMovement(currentEvent);
        } else {
            ReversiGame gameCopy = new ReversiGame(actualGame.getMatrixCopy(), myColor);
            return new ReversiMiniMax(gameCopy, mySettings, myColor).getBestMovement();
        }
    }
    
    private Coordinate2D getRandomMovement(ReversiGameEvent evt) {
        Random rnd = new Random();
        int element = rnd.nextInt(evt.getHints().size());
        return evt.getHints().get(element);
        
    }
    
    public void giveTurn(ReversiGameEvent evt) {
        myTurn = true;
        currentEvent = evt;
    }
    
    public void stop() {
        running = false;
    }
    
    public void start() {
        running = true;
    }

    @Override
    public void run() {
        System.err.println("AI STARTED!");
        while(!actualGame.isFinished() && running) {
            RunningTools.holdOn(50);
            if (myTurn) {
                RunningTools.holdOn(1000);
                makeMove();
                myTurn = false;
            }
        }
        System.err.println("AI FINISHED!");
    }
}
