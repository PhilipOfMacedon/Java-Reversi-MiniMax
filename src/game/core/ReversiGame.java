/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core;

import static game.core.ReversiGameAction.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import util.Coordinate2D;
import static util.Paths.DEFAULT_CONFIG_DIR;
import static util.Paths.SAVED_GAME_DIR;

/**
 *
 * @author filipe
 */
public class ReversiGame {

    private char[][] field;
    private ReversiGameListener[][] boardCells;
    private List<ReversiGameListener> otherListeners;
    private List<Coordinate2D> lastTurnHints;
    private char currentTurnPlayer;

    public ReversiGame(boolean loadFromSavedGame) {
        if (!loadFromSavedGame) {
            initializeElements(DEFAULT_CONFIG_DIR);
        } else {
            initializeElements(SAVED_GAME_DIR);
        }
    }
    
    public ReversiGame(String customOrganization, char firstPlayer) {
        field = initializeField(customOrganization);
        currentTurnPlayer = (firstPlayer != BLACK) ? (WHITE) : (BLACK);
    }
    
    public void resetGame() {
        createFields(DEFAULT_CONFIG_DIR);
        syncGameChanges();
    }
    
    private void initializeElements(String savedConfigDir) {
        boardCells = new ReversiGameListener[8][8];
        otherListeners = new ArrayList<>();
        createFields(savedConfigDir);
    }
    
    private void createFields(String savedConfigDir) {
        lastTurnHints = new ArrayList<>();
        loadFile(new File(savedConfigDir));
    }

    public void addReversiGameListener(ReversiGameListener listener, Coordinate2D position) {
        boardCells[position.x][position.y] = listener;
    }
    
    public void addReversiGameListener(ReversiGameListener listener) {
        otherListeners.add(listener);
    }

    private void fireReversiGameMovements(List<ReversiGameMovement> movements) {
        for (ReversiGameMovement movement : movements) {
            boardCells[movement.getPosition().x][movement.getPosition().y]
                    .reversiGameBoardChanged(movement.getEvent());
        }
    }
    
    private void fireReversiBoardStatus(ReversiGameEvent evt) {
            for (ReversiGameListener listener : otherListeners) {
            listener.reversiGameTurnChanged(evt);
        }
    }

    private char[][] initializeField(String fieldConfiguration) {
        char[][] handledField = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                handledField[i][j] = (fieldConfiguration.equals("")) ? (NOTHING) : (fieldConfiguration.charAt(8 * i + j));
            }
        }
        return handledField;
    }

    private void loadFile(File savedConfiguration) {
        try {
            FileReader fr = new FileReader(savedConfiguration);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("[Board]")) {
                    line = br.readLine();
                    field = initializeField(line);
                    line = br.readLine();
                    currentTurnPlayer = line.charAt(0);
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "I couldn't find your save file!", "Oh no!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "A problem occured while I was trying to read your file!", "Oh no!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void syncGameChanges() {
        List<ReversiGameMovement> movements = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                movements.add(new ReversiGameMovement(new Coordinate2D(i, j), field[i][j]));
            }
        }
        ReversiGameEvent evt = scanForHints(false);
        setHintFields(movements, lastTurnHints, false);
        setHintFields(movements, evt.getHints(), true);
        lastTurnHints = evt.getHints();
        fireReversiGameMovements(movements);
        fireReversiBoardStatus(evt);
    }
    
    public void skip() {
        List<ReversiGameMovement> movements = new ArrayList<>();
        ReversiGameEvent evt = scanForHints(true);
        setHintFields(movements, lastTurnHints, false);
        setHintFields(movements, evt.getHints(), true);
        lastTurnHints = evt.getHints();
        fireReversiGameMovements(movements);
        fireReversiBoardStatus(evt);
    }
    
    private void setHintFields(List<ReversiGameMovement> movements, List<Coordinate2D> hints, boolean add) {
        if (add) {
            for (Coordinate2D hint : hints) {
                movements.add(new ReversiGameMovement(hint, HINT));
            }
        } else {
            for (Coordinate2D hint : hints) {
                char cellValue = field[hint.x][hint.y];
                movements.add(new ReversiGameMovement(hint, (cellValue == HINT) ? (NOTHING) : (cellValue)));
            }
        }
    }
    
    private void switchPlayers() {
        if (currentTurnPlayer == BLACK) currentTurnPlayer = WHITE;
        else currentTurnPlayer = BLACK;
    }
    
    private ReversiGameEvent scanForHints(boolean willChangeTurn) {
        int scoreBlack = 0;
        int scoreWhite = 0;
        List<Coordinate2D> hints = new ArrayList<>();
        if (willChangeTurn) {
            switchPlayers();
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j] == BLACK) scoreBlack++;
                else if (field[i][j] == WHITE) scoreWhite++;
                if (isCellUsable(i, j)) {
                    hints.add(new Coordinate2D(i, j));
                }
            }
        }
        return new ReversiGameEvent(this, hints, scoreBlack, scoreWhite, currentTurnPlayer);
    }

    public void put(int x, int y) {
        if (isCellUsable(x, y)) {
            List<ReversiGameMovement> movements = scanCellNeighborhood(x, y, true);
            for (ReversiGameMovement movement : movements) {
                field[movement.getPosition().x][movement.getPosition().y] = movement.getEvent();
            }
            ReversiGameEvent evt = scanForHints(true);
            setHintFields(movements, lastTurnHints, false);
            setHintFields(movements, evt.getHints(), true);
            lastTurnHints = evt.getHints();
            
            fireReversiGameMovements(movements);
            fireReversiBoardStatus(evt);
        }
    }
    
    private boolean isCellUsable(int x, int y) {
        return (field[x][y] == '_' ) && scanCellNeighborhood(x, y, false) != null;
    }
    
    private List<ReversiGameMovement> scanCellNeighborhood(int x, int y, boolean makeMove) {
        if (!makeMove) {
            if (scanLine(x, y, makeMove) != null || scanColumn(x, y, makeMove) != null ||
                scanMainDiagonal(x, y, makeMove) != null || scanSecondaryDiagonal(x, y, makeMove) != null) {
                return new ArrayList<>();
            }
            return null;
        } else {
            List<ReversiGameMovement> movementsLine = scanLine(x, y, makeMove);
            List<ReversiGameMovement> movementsColunmn = scanColumn(x, y, makeMove);
            List<ReversiGameMovement> movementsDiagonal1 = scanMainDiagonal(x, y, makeMove);
            List<ReversiGameMovement> movementsDiagonal2 = scanSecondaryDiagonal(x, y, makeMove);
            List<ReversiGameMovement> movements = new ArrayList<>();
            if (movementsLine != null) {
                movements.addAll(movementsLine);
            }
            if (movementsColunmn != null) {
                movements.addAll(movementsColunmn);
            }
            if (movementsDiagonal1 != null) {
                movements.addAll(movementsDiagonal1);
            }
            if (movementsDiagonal2 != null) {
                movements.addAll(movementsDiagonal2);
            }
            if (!movements.isEmpty()) movements.add(new ReversiGameMovement(new Coordinate2D(x, y), currentTurnPlayer)); 
            return movements;
        }
    }
    
    private List<ReversiGameMovement> scanLine(int x, int y, boolean makeMove) {
        boolean foundOpposite = false;
        boolean foundDeadEnd = false;
        boolean foundChain = false;
        List<ReversiGameMovement> movements = new ArrayList<>();
        List<ReversiGameMovement> auxEvts = new ArrayList<>();
        int auxY = y;
        if (y > 0) {
            do {
                if (isPlayerChar(x, --auxY) && field[x][auxY] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(x, auxY), currentTurnPlayer));
                } else if (field[x][auxY] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxY > 0 && !foundDeadEnd && !foundChain);
        }
        foundOpposite = foundDeadEnd = false;
        if (foundChain) movements.addAll(auxEvts);
        auxEvts.clear();
        foundChain = false;
        auxY = y;
        if (y < 7) {
            do {
                if (isPlayerChar(x, ++auxY) && field[x][auxY] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(x, auxY), currentTurnPlayer));
                } else if (field[x][auxY] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxY < 7 && !foundDeadEnd && !foundChain);
        }
        if (foundChain) movements.addAll(auxEvts);
        if (movements.isEmpty()) return null;
        return movements;
    }
    
    private List<ReversiGameMovement> scanColumn(int x, int y, boolean makeMove) {
        boolean foundOpposite = false;
        boolean foundDeadEnd = false;
        boolean foundChain = false;
        List<ReversiGameMovement> movements = new ArrayList<>();
        List<ReversiGameMovement> auxEvts = new ArrayList<>();
        int auxX = x;
        if (x > 0) {
            do {
                if (isPlayerChar(--auxX, y) && field[auxX][y] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(auxX, y), currentTurnPlayer));
                } else if (field[auxX][y] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxX > 0 && !foundDeadEnd && !foundChain);
        }
        foundOpposite = foundDeadEnd = false;
        if (foundChain) movements.addAll(auxEvts);
        auxEvts.clear();
        foundChain = false;
        auxX = x;
        if (x < 7) {
            do {
                if (isPlayerChar(++auxX, y) && field[auxX][y] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(auxX, y), currentTurnPlayer));
                } else if (field[auxX][y] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxX < 7 && !foundDeadEnd && !foundChain);
        }
        if (foundChain) movements.addAll(auxEvts);
        if (movements.isEmpty()) return null;
        return movements;
    }
    
    private List<ReversiGameMovement> scanMainDiagonal(int x, int y, boolean makeMove) {
        boolean foundOpposite = false;
        boolean foundDeadEnd = false;
        boolean foundChain = false;
        List<ReversiGameMovement> movements = new ArrayList<>();
        List<ReversiGameMovement> auxEvts = new ArrayList<>();
        int auxX = x;
        int auxY = y;
        if (x > 0 && y > 0) {
            do {
                if (isPlayerChar(--auxX, --auxY) && field[auxX][auxY] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(auxX, auxY), currentTurnPlayer));
                } else if (field[auxX][auxY] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxX > 0 && auxY > 0 && !foundDeadEnd && !foundChain);
        }
        foundOpposite = foundDeadEnd = false;
        if (foundChain) movements.addAll(auxEvts);
        auxEvts.clear();
        foundChain = false;
        auxX = x;
        auxY = y;
        if (x < 7 && y < 7) {
            do {
                if (isPlayerChar(++auxX, ++auxY) && field[auxX][auxY] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(auxX, auxY), currentTurnPlayer));
                } else if (field[auxX][auxY] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxX < 7 && auxY < 7 && !foundDeadEnd && !foundChain);
        }
        if (foundChain) movements.addAll(auxEvts);
        if (movements.isEmpty()) return null;
        return movements;
    }
    
    private List<ReversiGameMovement> scanSecondaryDiagonal(int x, int y, boolean makeMove) {
        boolean foundOpposite = false;
        boolean foundDeadEnd = false;
        boolean foundChain = false;
        List<ReversiGameMovement> movements = new ArrayList<>();
        List<ReversiGameMovement> auxEvts = new ArrayList<>();
        int auxX = x;
        int auxY = y;
        if (x > 0 && y < 7) {
            do {
                if (isPlayerChar(--auxX, ++auxY) && field[auxX][auxY] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(auxX, auxY), currentTurnPlayer));
                } else if (field[auxX][auxY] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxX > 0 && auxY < 7 && !foundDeadEnd && !foundChain);
        }
        foundOpposite = foundDeadEnd = false;
        if (foundChain) movements.addAll(auxEvts);
        auxEvts.clear();
        foundChain = false;
        auxX = x;
        auxY = y;
        if (x < 7 && y > 0) {
            do {
                if (isPlayerChar(++auxX, --auxY) && field[auxX][auxY] != currentTurnPlayer) {
                    foundOpposite = true;
                    auxEvts.add(new ReversiGameMovement(new Coordinate2D(auxX, auxY), currentTurnPlayer));
                } else if (field[auxX][auxY] == currentTurnPlayer && foundOpposite) {
                    if (!makeMove) return movements;
                    else foundChain = true;
                } else {
                    foundDeadEnd = true;
                }
            } while (auxX < 7 && auxY > 0 && !foundDeadEnd && !foundChain);
        }
        if (foundChain) movements.addAll(auxEvts);
        if (movements.isEmpty()) return null;
        return movements;
    }
    
    private boolean isPlayerChar(int x, int y) {
        return field[x][y] == BLACK || field[x][y] == WHITE;
    }
    
    public void printField() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.err.print(field[i][j] + "\t");
            }
            System.err.println("");
        }
    }
    
    @Override
    public String toString() {
        String fieldRepresentation = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fieldRepresentation += field[i][j];
            }
        }
        return fieldRepresentation;
    }

}
