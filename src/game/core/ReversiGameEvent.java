/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core;

import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import util.Coordinate2D;

/**
 *
 * @author filipe
 */
public class ReversiGameEvent extends EventObject {
    
    private List<Coordinate2D> hints;
    private int scoreBlack;
    private int scoreWhite;
    private char currentActivePlayer;
    private boolean isFinished;

    public ReversiGameEvent(Object source, List<Coordinate2D> hints, int scoreBlack, int scoreWhite, char currentActivePlayer, boolean isFinished) {
        super(source);
        this.hints = hints;
        this.scoreBlack = scoreBlack;
        this.scoreWhite = scoreWhite;
        this.currentActivePlayer = currentActivePlayer;
        this.isFinished = isFinished;
    }
    
    public List<Coordinate2D> getHints() {
        return Collections.unmodifiableList(hints);
    }
    
    public int getBlackScore() {
        return scoreBlack;
    }
    
    public int getWhiteScore() {
        return scoreWhite;
    }
    
    public char getCurrentActivePlayer() {
        return currentActivePlayer;
    }
    
    public boolean isGamePlayable() {
        return !isFinished;
    }
    
    public boolean isTurnPlayable() {
        return hints.size() > 0;
    }
}
