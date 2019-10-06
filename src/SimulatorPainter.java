import Actors.Actor;
import Visuals.VisualShape;

import javax.swing.*;
import java.awt.*;

public class SimulatorPainter extends JPanel {
    public String viewState = "TitleScreen";
    public int levelNumber;
    private MainFrame mainFrame;
    public SimulatorPainter(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    public void paint(Graphics g) {
        g.setFont(new Font("Comic sans",Font.BOLD,30));
        switch (this.viewState) {
            case "TitleScreen" :
                this.titleScreen(g);
                break;
            case "LevelTitleScreen" :
                this.levelTitleScreen(g);
                break;
            case "LevelScreen" :
                this.levelScreen(g);
                break;
        }
    }
    private void titleScreen(Graphics g) {
        int width = g.getFontMetrics().stringWidth("Welcome to ML Turrets");
        int height = g.getFontMetrics().getHeight();
        System.out.println(width);
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        System.out.println("Panel width " + panelWidth + " String width " + width + " Center " + (panelWidth - width));
        g.drawString("Welcome to ML Turrets",(panelWidth - width)/2,(panelHeight - height)/2);
    }
    private void levelTitleScreen(Graphics g) {
        int width = g.getFontMetrics().stringWidth("Level "+this.levelNumber);
        int height = g.getFontMetrics().getHeight();
        g.drawString("Level " + this.levelNumber,(this.getWidth()-width)/2,(this.getHeight()-height)/2);
    }

    /**
     * In each level the base is always at the center, the level number is the number^2 amount of turrets in the game
     * @param g
     */
    private void levelScreen(Graphics g) {
//        System.out.println("Printing level screen");
        SimulationLevel currentLevel = this.mainFrame.trainer.currentLevel;
        //Print base first
        this.paintActor(currentLevel.base,g);
        //Print turrets
        for (int i = 0; i < currentLevel.turrets.size(); i++) {
            this.paintActor(currentLevel.turrets.get(i),g);
        }
        //Print Enemies
        for (int i = 0; i < currentLevel.enemies.size(); i++) {
            this.paintActor(currentLevel.enemies.get(i),g);
        }
        //Print text data
    }

    private void paintActor(Actor actor,Graphics g) {
        g.setColor(actor.component.color);
        if(actor.component.shape == VisualShape.Square) {
            g.fillRect(actor.component.xPos,actor.component.yPos,actor.component.width,actor.component.width);
        } else if(actor.component.shape == VisualShape.Circle) {
            g.fillOval(actor.component.xPos,actor.component.yPos,actor.component.width,actor.component.width);
        }
    }
}
