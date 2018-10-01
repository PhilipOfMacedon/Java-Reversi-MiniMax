/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import game.config.AISettings;
import game.ai.ComputerPlayer;
import game.core.ReversiGame;
import game.core.ReversiGameAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import util.Coordinate2D;
import static util.Paths.*;
import static game.core.ReversiGameAction.*;
import game.core.ReversiGameEvent;
import java.awt.Frame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import util.RunningTools;

/**
 *
 * @author filipe
 */
public class GameWindow extends javax.swing.JFrame {

    private class AuxiliarGUIEnhancements implements Runnable {
        
        private boolean isAITime;
        private boolean running = true;
        
        public void stop() {
            running = false;
        }

        public void start() {
            running = true;
        }
        
        @Override
        public void run() {
            int counter = 0;
            while(!gameEngine.isFinished() && running) {
                RunningTools.holdOn(200);
                if (isAITime) {
                    String reticences = "";
                    for (int i = 0; i < counter; i++) {
                        reticences += '.';
                    }
                    labelAIThinking.setText(reticences + "AI is thinking, wait" + reticences);
                    if (counter <= 3) counter++; else counter = 0;
                } else {
                    counter = 1;
                }
            }
        }
        
    }
    
    private class BoardCell extends JPanel {

        private Coordinate2D position;
        private int side;
        private JLabel imageLabel;
        private Image currentImage;

        public BoardCell(Coordinate2D position, int side) {
            this.position = position;
            this.side = side;
            initComponent();
        }

        private void initComponent() {
            setBackground(new Color(204, 0, 0));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //console.append("LOL OLHA ISSO: " + panel.getPosition() + "\n");
                    gameEngine.put(position.x, position.y, humanColor);
                }
            });
            setLayout(new GridBagLayout());
            imageLabel = new JLabel();
            updateLabelImage(null);
            add(imageLabel);
            gameEngine.addReversiGameListener(new ReversiGameAdapter() {
                @Override
                public void reversiGameBoardChanged(char evt) {
                    updateLabelFromEvent(evt);
                }
            }, position);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (cellBackground != null) {
                g.drawImage(cellBackground, 0, 0, this.side, this.side, this);
            }
        }

        public void setPosition(Coordinate2D pos) {
            position = pos;
        }

        public Coordinate2D getPosition() {
            return position;
        }

        public void setSideSize(int newSize) {
            side = newSize;
        }

        public int getSideSize() {
            return side;
        }

        private void updateLabelFromEvent(char event) {
            switch (event) {
                case NOTHING:
                    updateLabelImage(null);
                    break;
                case BLACK:
                    updateLabelImage(black);
                    break;
                case WHITE:
                    updateLabelImage(white);
                    break;
                case HINT:
                    updateLabelImage(hint);
            }
        }

        public void updateLabelImage(Image newImage) {
            currentImage = newImage;
            updateLabelSize();
        }

        public void updateLabelSize() {
            if (currentImage != null) {
                imageLabel.setIcon(getImageIconFromImage(scaleImage(currentImage, this.side)));
            } else {
                imageLabel.setIcon(null);
            }
        }

    }

    private HashMap<Coordinate2D, BoardCell> posicoes;
    private Image cellBackground;
    private Image black;
    private Image white;
    private Image hint;
    private ReversiGame gameEngine;
    private ComputerPlayer machine;
    private char humanColor;
    private boolean showHumanHints;
    private boolean showMachineHints;
    private AuxiliarGUIEnhancements labelWatchdog;

    /**
     * Creates new form GameWindow
     */
    public GameWindow() {
        initComponents();
        initGameStructures();
        createBoard();
        startGame();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        boardContainer = new javax.swing.JPanel();
        board = new javax.swing.JPanel();
        sidePanel = new javax.swing.JPanel();
        btnRestart = new javax.swing.JButton();
        btnUndo = new javax.swing.JButton();
        btnSetLevel = new javax.swing.JButton();
        btnInfo = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        progressWhite = new javax.swing.JProgressBar();
        labelBlackPiece = new javax.swing.JLabel();
        progressBlack = new javax.swing.JProgressBar();
        labelWhiteScore = new javax.swing.JLabel();
        labelBlackScore = new javax.swing.JLabel();
        labelWhitePiece = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnPassTurn = new javax.swing.JButton();
        labelAIThinking = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Red & Black Reversi");
        setBackground(new java.awt.Color(0, 0, 0));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        boardContainer.setBackground(new java.awt.Color(0, 0, 0));
        boardContainer.setForeground(new java.awt.Color(238, 238, 238));
        boardContainer.setLayout(new java.awt.BorderLayout());

        board.setBackground(new java.awt.Color(51, 51, 51));
        board.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        board.setForeground(new java.awt.Color(238, 238, 238));
        java.awt.GridBagLayout boardLayout = new java.awt.GridBagLayout();
        boardLayout.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        boardLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        board.setLayout(boardLayout);
        boardContainer.add(board, java.awt.BorderLayout.CENTER);

        getContentPane().add(boardContainer, java.awt.BorderLayout.CENTER);

        sidePanel.setBackground(new java.awt.Color(204, 0, 0));
        sidePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sidePanel.setPreferredSize(new java.awt.Dimension(324, 727));

        btnRestart.setBackground(new java.awt.Color(153, 0, 0));
        btnRestart.setForeground(new java.awt.Color(204, 204, 204));
        btnRestart.setToolTipText("Restart match");
        btnRestart.setFocusable(false);
        btnRestart.setIcon(getImageIconFromFile("restart.png", ICONS_DIR, 28, 28));
        btnRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestartActionPerformed(evt);
            }
        });

        btnUndo.setBackground(new java.awt.Color(153, 0, 0));
        btnUndo.setForeground(new java.awt.Color(204, 204, 204));
        btnUndo.setToolTipText("Undo move");
        btnUndo.setFocusable(false);
        btnUndo.setIcon(getImageIconFromFile("undo.png", ICONS_DIR, 28, 28));
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });

        btnSetLevel.setBackground(new java.awt.Color(153, 0, 0));
        btnSetLevel.setForeground(new java.awt.Color(204, 204, 204));
        btnSetLevel.setToolTipText("Set AI level");
        btnSetLevel.setFocusable(false);
        btnSetLevel.setIcon(getImageIconFromFile("level.png", ICONS_DIR, 28, 28));
        btnSetLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetLevelActionPerformed(evt);
            }
        });

        btnInfo.setBackground(new java.awt.Color(153, 0, 0));
        btnInfo.setForeground(new java.awt.Color(204, 204, 204));
        btnInfo.setToolTipText("About");
        btnInfo.setFocusable(false);
        btnInfo.setIcon(getImageIconFromFile("info.png", ICONS_DIR, 28, 28));
        btnInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(238, 238, 238));

        progressWhite.setMaximum(64);

        progressBlack.setMaximum(64);

        labelWhiteScore.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        labelWhiteScore.setForeground(new java.awt.Color(238, 238, 238));

        labelBlackScore.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        labelBlackScore.setForeground(new java.awt.Color(238, 238, 238));
        labelBlackScore.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 238, 238));
        jLabel3.setText("X");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 238, 238));
        jLabel4.setText("X");

        btnPassTurn.setBackground(new java.awt.Color(64, 0, 0));
        btnPassTurn.setForeground(new java.awt.Color(238, 238, 238));
        btnPassTurn.setToolTipText("Click here to skip your turn!");
        btnPassTurn.setFocusable(false);
        btnPassTurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPassTurnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPassTurn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressWhite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBlack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(labelWhitePiece, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelWhiteScore, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(labelBlackScore, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelBlackPiece, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressWhite, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(labelWhiteScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                    .addComponent(labelWhitePiece, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnPassTurn, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(labelBlackScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                    .addComponent(labelBlackPiece, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBlack, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        labelAIThinking.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        labelAIThinking.setForeground(new java.awt.Color(238, 238, 238));
        labelAIThinking.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAIThinking.setText("AI is thinking");

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addComponent(btnInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRestart, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSetLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelAIThinking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRestart, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSetLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(labelAIThinking, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        getContentPane().add(sidePanel, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startGame() {
        gameEngine.syncGameChanges();
        startAI();
    }
    
    private void startAI() {
        Thread machineThread = new Thread(machine);
        machineThread.start();
        Thread guiAuxThread = new Thread(labelWatchdog);
        guiAuxThread.start();
    }

    private void loadImages() {
        try {
            black = ImageIO.read(new File(IMAGES_DIR + "black.png"));
            white = ImageIO.read(new File(IMAGES_DIR + "white.png"));
            hint = ImageIO.read(new File(IMAGES_DIR + "hint.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "I couldn't load your images :(", "Aw snap!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private ImageIcon getImageIconFromFile(String imageName, String imagePath, int width, int height) {
        try {
            return new ImageIcon(new ImageIcon(ImageIO.read(new File(imagePath + imageName))).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "I couldn't load your image named \"" + imageName + "\", sorry :(", "Aw snap!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private Image scaleImage(Image image, int side) {
        return image.getScaledInstance(side, side, Image.SCALE_SMOOTH);
    }

    private ImageIcon getImageIconFromImage(Image image) {
        return new ImageIcon(image);
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        adjustBoardSize();
    }//GEN-LAST:event_formComponentResized

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged

    }//GEN-LAST:event_formWindowStateChanged

    private void btnRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestartActionPerformed
        gameEngine.resetGame();
        machine.stop();
        machine.start();
        labelWatchdog.stop();
        labelWatchdog.start();
        startGame();
    }//GEN-LAST:event_btnRestartActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        JOptionPane.showMessageDialog(this, "I can't do that right now, I'm still learning somethings, you know? ;)", "Whoa hold on pal!", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnSetLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetLevelActionPerformed
        AISettings newSettings = GameOptions.loadSettings(this, machine.getSettings());
        if (newSettings != null) {
            machine.updateSettings(newSettings);
        }
    }//GEN-LAST:event_btnSetLevelActionPerformed

    private void btnInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoActionPerformed
        new HelpDialog(this, true);
    }//GEN-LAST:event_btnInfoActionPerformed

    private void btnPassTurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPassTurnActionPerformed
        gameEngine.skip(humanColor);
    }//GEN-LAST:event_btnPassTurnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel board;
    private javax.swing.JPanel boardContainer;
    private javax.swing.JButton btnInfo;
    private javax.swing.JButton btnPassTurn;
    private javax.swing.JButton btnRestart;
    private javax.swing.JButton btnSetLevel;
    private javax.swing.JButton btnUndo;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelAIThinking;
    private javax.swing.JLabel labelBlackPiece;
    private javax.swing.JLabel labelBlackScore;
    private javax.swing.JLabel labelWhitePiece;
    private javax.swing.JLabel labelWhiteScore;
    private javax.swing.JProgressBar progressBlack;
    private javax.swing.JProgressBar progressWhite;
    private javax.swing.JPanel sidePanel;
    // End of variables declaration//GEN-END:variables

    private void initGameStructures() {
        humanColor = BLACK;
        showHumanHints = true;
        showMachineHints = false;
        gameEngine = new ReversiGame(false, showHumanHints, showMachineHints);
        AISettings settings = new AISettings();
        settings.loadDefaultPreset("normal");
        machine = new ComputerPlayer(WHITE, gameEngine, settings);
        labelWatchdog = new AuxiliarGUIEnhancements();
        loadImages();
    }

    private void loadDefaultCellBackground(int side) {
        try {
            cellBackground = ImageIO.read(new File(IMAGES_DIR + "empty_red.png"));
        } catch (IOException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getSmallestContainerDimension() {
        int sizeX = boardContainer.getSize().width;
        int sizeY = boardContainer.getSize().height;
        int side = (sizeX < sizeY) ? (sizeX) : (sizeY);

        if (side < 8) side = 8;
        
        return side;
    }

    private void adjustBoardSize() {
        int side = getSmallestContainerDimension();
        loadDefaultCellBackground(side);
        side += 8;
        board.setSize(new Dimension(side, side));
        board.setPreferredSize(new Dimension(side, side));
        board.setMinimumSize(new Dimension(side, side));
        board.setMaximumSize(new Dimension(side, side));
        side -= 8;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate2D position = new Coordinate2D(i, j);
                BoardCell currentCell = posicoes.get(position);
                currentCell.setSize(new Dimension(side / 8, side / 8));
                currentCell.setSideSize(side / 8);
                currentCell.setPreferredSize(new Dimension(side / 8, side / 8));
                currentCell.setMinimumSize(new Dimension(side / 8, side / 8));
                currentCell.setMaximumSize(new Dimension(side / 8, side / 8));
                currentCell.updateLabelSize();
            }
        }
        revalidate();
        repaint();
    }

    private void createBoard() {
        posicoes = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate2D position = new Coordinate2D(i, j);
                BoardCell panel = new BoardCell(position, getSmallestContainerDimension() / 8);
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = j;
                constraints.gridy = i;
                board.add(panel, constraints);
                posicoes.put(position, panel);
            }
        }
        labelBlackPiece.setIcon(getImageIconFromImage(scaleImage(black, 48)));
        labelWhitePiece.setIcon(getImageIconFromImage(scaleImage(white, 48)));
        adjustBoardSize();
        gameEngine.addReversiGameListener(new ReversiGameAdapter() {
            @Override
            public void reversiGameTurnChanged(ReversiGameEvent evt) {
                updateGUI(evt);
            }
        });
        revalidate();
        repaint();
    }

    private void updateGUI(ReversiGameEvent evt) {
        String player = (evt.getCurrentActivePlayer() == BLACK) ? ("BLACK") : ("WHITE");
        if (evt.getHints().isEmpty()) {
            btnPassTurn.setText(player + " IS OUT OF MOVES");
        } else {
            btnPassTurn.setText("IT'S " + player + "'s TURN");
        }
        if (evt.getCurrentActivePlayer() == BLACK) {
            labelWatchdog.isAITime = false;
            labelAIThinking.setText("");
        } else {
            labelWatchdog.isAITime = true;
            labelAIThinking.setText("AI is thinking, wait");
            machine.giveTurn(evt);
        }
        if (!evt.isGamePlayable()) {
            btnPassTurn.setText("GAME FINISHED!!!");
            if (evt.getBlackScore() != evt.getWhiteScore()) {
                String winner = (evt.getBlackScore() > evt.getWhiteScore()) ? ("BLACK") : ("WHITE");
                labelAIThinking.setText(winner + " is the WINNER!!!");
            } else {
                labelAIThinking.setText("DRAW GAME, no winner!!!");
            }        
        }
        labelBlackScore.setText(evt.getBlackScore() + "");
        labelWhiteScore.setText(evt.getWhiteScore() + "");
        progressBlack.setValue(evt.getBlackScore());
        progressWhite.setValue(evt.getWhiteScore());
        revalidate();
        repaint();
    }
}
