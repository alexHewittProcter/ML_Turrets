import javax.sound.midi.SysexMessage;

/**
 * The TrainingSimulator object is for all the logic with the learning and interaction with between the turrets and the enemies
 */
public class TrainingSimulator {
    public final MainFrame mainFrame;
    public boolean levelRunning = false;
    public SimulationLevel currentLevel = null;
    public int levelNumber = 5;
    private int levelNumberScreenTime = 24 * 5;

    public TrainingSimulator(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void runLoop() {
//        System.out.println("Training simulator run loop");
        if(!levelRunning) {
//            System.out.println("Creating new level");
            //Create a new level
            this.currentLevel = new SimulationLevel(this);
            this.levelRunning = true;
        } else {
            //Run level
            this.currentLevel.runLoop();
            if(this.currentLevel.levelFinished()) {
                this.levelRunning = false;
                this.levelNumber++;
            }
        }
    }
}
