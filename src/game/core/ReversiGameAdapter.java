/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core;

/**
 *
 * @author filipe
 */
public abstract class ReversiGameAdapter implements ReversiGameListener {

    @Override
    public void reversiGameBoardChanged(char evt) {
    }

    @Override
    public void reversiGameTurnChanged(ReversiGameEvent evt) {
    }
    
}
