/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.config;

import static game.config.AISettingsLoader.*;
import util.Coordinate2D;

/**
 *
 * @author filipe
 */
public final class AISettings {

    private boolean fromPreset;
    private int[] configurations;
    private int[][] precalculatedBiasedPositions;

    public AISettings(int[] configurations) {
        loadCustomConfiguration(configurations);
        precalculatedBiasedPositions = null;
    }

    public AISettings() {
        configurations = null;
        fromPreset = true;
        precalculatedBiasedPositions = null;
    }

    public int getBiasScore(int regionType) {
        return (configurations != null) ? (configurations[regionType]) : (0);
    }
    
    public int getBiasFromPosition(Coordinate2D position) {
        return precalculatedBiasedPositions[position.x][position.y];
    }

    public int getMaxTreeLevel() {
        return (configurations != null) ? (configurations[0]) : (0);
    }

    public void loadDefaultPreset(String preset) {
        configurations = AISettingsLoader.getInstance().getLevelSettings(preset);
        fromPreset = true;
        calculateBiasMatrix();
    }

    public void loadCustomConfiguration(int[] configurations) {
        this.configurations = configurations;
        fromPreset = false;
        calculateBiasMatrix();
    }

    public int getPresetOption() {
        if (fromPreset) {
            if (configurations == null) {
                return 0;
            } else {
                return configurations[0];
            }
        } else {
            return -1;
        }
    }

    public void calculateBiasMatrix() {
        if (configurations != null) {
            precalculatedBiasedPositions = new int[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boolean isRegion1 = (i == 0 && j == 0) || (i == 0 && j == 7) 
                            || (i == 7 && j == 0) || (i == 7 && j == 7);
                    boolean isRegion2 = ((i == 0 || i == 7) && (j > 1 && j < 6))
                            || ((j == 0 || j == 7) && (i > 1 && i < 6));
                    boolean isRegion3 = ((i == 1 || i == 6) && (j > 1 && j < 6))
                            || ((j == 1 || j == 6) && (i > 1 && i < 6));
                    boolean isRegion4 = i > 1 && i < 6 && j > 1 && j < 6;
                    if (isRegion1) {
                        precalculatedBiasedPositions[i][j] = configurations[REGION_1];
                    } else if (isRegion2) {
                        precalculatedBiasedPositions[i][j] = configurations[REGION_2];
                    } else if (isRegion3) {
                        precalculatedBiasedPositions[i][j] = configurations[REGION_3];
                    } else if (isRegion4) {
                        precalculatedBiasedPositions[i][j] = configurations[REGION_4];
                    } else {
                        precalculatedBiasedPositions[i][j] = configurations[REGION_5];
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        if (configurations != null) {
            return "Steps ahead:\t" + configurations[0] + "\n"
                    + "Region 1 bias:\t" + configurations[1] + "\n"
                    + "Region 2 bias:\t" + configurations[2] + "\n"
                    + "Region 3 bias:\t" + configurations[3] + "\n"
                    + "Region 4 bias:\t" + configurations[4] + "\n"
                    + "Region 5 bias:\t" + configurations[5];
        }
        return "AI selection: Random";
    }
}
