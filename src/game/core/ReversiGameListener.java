/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core;

import java.util.EventListener;

/**
 *
 * @author filipe
 */
public interface ReversiGameListener extends EventListener {
    public void reversiGameBoardChanged(char evt);
    public void reversiGameTurnChanged(ReversiGameEvent evt);
}
