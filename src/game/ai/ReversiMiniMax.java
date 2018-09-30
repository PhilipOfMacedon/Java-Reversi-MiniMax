/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.ai;

import game.core.ReversiGame;
import java.util.List;
import util.Coordinate2D;

/**
 *
 * @author filipe
 */
public class ReversiMiniMax {

    private static final int POSITIVE_INFINITY = 1000000;
    private static final int NEGATIVE_INFINITY = -1000000;

    private class Node {

        private ReversiGame gameState;
        private int value;
        private int alpha;
        private int beta;
        private boolean isMaximizer;
        private Coordinate2D bestMove;

        public Node(ReversiGame gameState, int alpha, int beta, boolean isMaximizer) {
            this.gameState = gameState;
            this.alpha = alpha;
            this.beta = beta;
            this.isMaximizer = isMaximizer;
            this.value = (isMaximizer) ? (NEGATIVE_INFINITY) : (POSITIVE_INFINITY);
            bestMove = null;
        }

    }

    private Node root;
    private AISettings settings;
    private char perspective;

    public ReversiMiniMax(ReversiGame gameState, AISettings settings, char perspective) {
        this.settings = settings;
        root = new Node(gameState, NEGATIVE_INFINITY, POSITIVE_INFINITY, true);
        this.perspective = perspective;
    }

    public Coordinate2D getBestMovement() {
        System.out.println("Best move score: " + getBestScore(0, root));
        return root.bestMove;
    }

    private int getBestScore(int currentLevel, Node currentNode) {
//        for (int i = 0; i < currentLevel; i++) {
//            System.err.print("\t");
//        }
//        System.err.println("Tree lvl: " + currentLevel + " ### ");
        if (currentLevel < settings.getMaxTreeLevel()) {
            List<Coordinate2D> possibleMoves = currentNode.gameState.getHints();
            if (possibleMoves.isEmpty()) return currentNode.gameState.getRelativeScoring(perspective);
            for (Coordinate2D possibleMove : possibleMoves) {
                
                if (currentNode.isMaximizer) {
                    if (currentNode.value > currentNode.beta) {
                        return currentNode.value;
                    }
                    ReversiGame childGameState = new ReversiGame(currentNode.gameState.getMatrixCopy(), currentNode.gameState.getCurrentPlayer());
                    childGameState.put(possibleMove.x, possibleMove.y, currentNode.gameState.getCurrentPlayer());
                    int moveScore = getBestScore(currentLevel + 1, new Node(childGameState, currentNode.value, currentNode.beta, false))
                            + settings.getBiasFromPosition(possibleMove);
                    if (moveScore > currentNode.value) { 
                        currentNode.value = moveScore;
                        currentNode.bestMove = possibleMove;
                    }
                } else {
                    if (currentNode.value < currentNode.alpha) {
                        return currentNode.value;
                    }
                    ReversiGame childGameState = new ReversiGame(currentNode.gameState.getMatrixCopy(), currentNode.gameState.getCurrentPlayer());
                    childGameState.put(possibleMove.x, possibleMove.y, currentNode.gameState.getCurrentPlayer());
                    int moveScore = getBestScore(currentLevel + 1, new Node(childGameState, currentNode.alpha, currentNode.value, true))
                            - settings.getBiasFromPosition(possibleMove);
                    if (moveScore < currentNode.value) { 
                        currentNode.value = moveScore;
                        currentNode.bestMove = possibleMove;
                    }
                }
            }
            return currentNode.value;
        } else {
            return currentNode.gameState.getRelativeScoring(perspective);
        }
    }
}
