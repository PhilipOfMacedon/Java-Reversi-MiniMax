/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core;

import java.util.EventObject;
import util.Coordinate2D;

/**
 *
 * @author filipe
 */
public class ReversiGameMovement {
    
    private Coordinate2D position;
    private char action;
    
    public ReversiGameMovement(Coordinate2D position, char action) {
        this.position = position;
        this.action = action;
    }
    
    public Coordinate2D getPosition() {
        return position;
    }
    
    public char getEvent() {
        return action;
    }
}
