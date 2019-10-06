import javax.swing.*;

/**
 * The MainFrame is used as the highest object in the hierachy, it creates and runs all the objects used to run the simulations
 */
public class MainFrame extends JFrame {
    public SimulatorPainter painter;
    public TrainingSimulator trainer;
    //Generates a TrainingSimulator and SimulatorPainter
    //Also creates a JFrame and JPanel
    public MainFrame() {
        super("ML Turrets");
        this.trainer = new TrainingSimulator(this);
        this.painter = new SimulatorPainter(this);
        this.add(this.painter);
        this.setSize(700,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);
        this.runSimulator();
    }
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
    }
    private void runSimulator() {
        try{
//            Thread.sleep(1000 * 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(true) {
            if(!this.trainer.levelRunning) {
                System.out.println("Change level");
                this.painter.viewState = "LevelTitleScreen";
                this.painter.levelNumber = this.trainer.levelNumber;
                this.repaint();
                try {
//                    Thread.sleep(1000*5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.painter.viewState = "LevelScreen";
            this.trainer.runLoop();
            this.repaint();
            try {
                Thread.sleep(1000 / 24);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
