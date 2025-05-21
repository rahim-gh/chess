package com.kern;

import com.kern.GUI.GamePanel;

import javax.swing.JFrame;

public class Main {

    public static GamePanel gp = new GamePanel();
    
    public static void main(String[] args) {
        init();
        
        gp.launchGame();
    }

    private static void init() {
        JFrame window = new JFrame("Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }
}
