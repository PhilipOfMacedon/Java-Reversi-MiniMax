/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import static util.Paths.*;

/**
 *
 * @author filipe
 */
public class AISettingsLoader {
    
    public static final int STEPS_FORWARD = 0;
    public static final int REGION_1 = 1;
    public static final int REGION_2 = 2;
    public static final int REGION_3 = 3;
    public static final int REGION_4 = 4;
    public static final int REGION_5 = 5;
    
    private Map<String, Integer[]> aiSettings;
    
    private static AISettingsLoader singletonObject;
    
    private AISettingsLoader() {
        loadDefaultConfigFromFile();
    }
    
    public static AISettingsLoader getInstance() {
        if (singletonObject == null) {
            singletonObject = new AISettingsLoader();
        }
        return singletonObject;
    }
    
    private void loadDefaultConfigFromFile() {
        aiSettings = new HashMap<>();
        try {
            File defaults = new File(DEFAULT_CONFIG_DIR);
            FileReader fr = new FileReader(defaults);
            BufferedReader br = new BufferedReader(fr);
            String input;
            while ((input = br.readLine()) != null) {
                if (input.contains("[Bias]")) {
                    while (((input = br.readLine()) != null) && input.charAt(0) != '[') {
                        Integer[] settings = new Integer[6];
                        String[] stringSettings = input.split("=");
                        for (int i = 0; i < 6; i++) {
                            settings[i] = Integer.parseInt(stringSettings[1].split(" ")[i]);
                        }
                        aiSettings.put(stringSettings[0], settings);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "I couldn't find the configurations file :(", "Aw shit...", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "There's something wrong with this file... Or with your storage device...\n"
                    + "Or with yourself.", "Aw shit...", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public int[] getLevelSettings(String level) {
        if (aiSettings.get(level) != null) {
            return Arrays.stream(aiSettings.get(level)).mapToInt(Integer::intValue).toArray();
        }
        return null;
    }
}
