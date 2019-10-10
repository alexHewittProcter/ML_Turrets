import Actors.Base;
import Actors.Enemy;
import Actors.Turret;

import java.util.ArrayList;

public class SimulationLevel {
    private int enemyGenerationDistance;
    private int level;
    private TrainingSimulator trainingSimulator;
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Turret> turrets = new ArrayList<>();
    public Base base;
    private boolean levelActive = false;
    private int enemiesToCreate;
    private int enemyGenerationRate;
    //enemyCreateBuffer is the amount of frames between generating enemies
    private int enemyCreateBuffer;
    private int currentEnemyCreateBuffer = 0;
    private int enemyDelay = 5 * 24;
    private int panelWidth = 0;
    private int panelHeight = 0;
    private int turretDistance;

    public SimulationLevel(TrainingSimulator trainingSimulator) {
        this.trainingSimulator = trainingSimulator;
        this.level = this.trainingSimulator.levelNumber;
        //Create base
        this.base = new Base();
        //Create turrets
        for (int i = 0; i < Math.pow(2,(this.level - 1)); i++) {
            this.turrets.add(new Turret());
        }
        System.out.println("Turrets size " + this.turrets.size());
        this.positionTurrets();
        //Add a enemy amount
        this.enemiesToCreate = (int) (200 + (100 * Math.pow((double)2,(double)this.level-1)));
        //Add a generation rate based on the level
        this.enemyGenerationRate = (int) (Math.pow((double)2,(double)this.level) * 5);
        //Create an enemy create buffer
        this.enemyCreateBuffer = 48;
        //Create a enemy distance from base
        this.enemyGenerationDistance = (int) (Math.pow(175,Math.pow(1.25,this.level-1))) + 100;
        //Create a turret distance from base
        this.turretDistance = (int) (100*Math.pow(1.1,this.level));
    }

    public void runLoop() {
        //First alter the current positions based on the panel size
        this.replaceActors();
        if(this.enemyDelay == 0) {
//            System.out.println("Running loop");
            //Move all enemies
            for (int i = 0; i < this.enemies.size(); i++) {
                Enemy currentEnemy = this.enemies.get(i);
                //Calculate new x
                if(currentEnemy.posXFromBase < 0) {
                    currentEnemy.posXFromBase = currentEnemy.posXFromBase + currentEnemy.movementPerLoop;
                } else if (currentEnemy.posXFromBase > 0) {
                    currentEnemy.posXFromBase = currentEnemy.posXFromBase - currentEnemy.movementPerLoop;
                }
                currentEnemy.component.xPos = this.base.component.xPos + currentEnemy.posXFromBase;
                //Calculate new y
                if(currentEnemy.posYFromBase < 0) {
                    currentEnemy.posYFromBase = currentEnemy.posYFromBase + currentEnemy.movementPerLoop;
                } else if(currentEnemy.posYFromBase > 0) {
                    currentEnemy.posYFromBase = currentEnemy.posYFromBase - currentEnemy.movementPerLoop;
                }
                currentEnemy.component.yPos = this.base.component.yPos + currentEnemy.posYFromBase;
            }
            //Generate new enemies
            if(this.enemyCreateBuffer == this.currentEnemyCreateBuffer && this.enemiesToCreate > 0) {
                this.currentEnemyCreateBuffer = 0;
//                System.out.println("Generate enemies " + this.enemiesToCreate);
                Enemy newEnemy;
                double genDegree;
                double finalGenDegree;
                int newX;
                int newY;
                int finalX;
                int finalY;
                for (int i = 0; i < this.enemyGenerationRate; i++) {
                    newEnemy = new Enemy();
                    //Randomise the area they're generated
                    genDegree = Math.random() * 360;
                    if(genDegree >= 270) {
                        finalGenDegree = genDegree - 270;
                    } else if (genDegree >= 180) {
                        finalGenDegree = genDegree - 180;
                    } else if(genDegree >= 90) {
                        finalGenDegree = genDegree - 90;
                    } else {
                        finalGenDegree = genDegree;
                    }
                    newY = (int)(Math.sin(finalGenDegree) * this.enemyGenerationDistance);
                    newX = (int)(Math.cos(finalGenDegree) * this.enemyGenerationDistance);
                    if(genDegree >= 270) {
                        finalY = - newY;
                        finalX = - newX;
                    } else if (genDegree >= 180) {
                        finalY = newY;
                        finalX = - newX;
                    } else if (genDegree >= 90) {
                        finalY = newY;
                        finalX = newX;
                    } else {
                        finalY = -newY;
                        finalX = newX;
                    }
                    newEnemy.posYFromBase = finalY;
                    newEnemy.posXFromBase = finalX;
                    newEnemy.component.yPos = this.base.component.yPos + finalY;
                    newEnemy.component.xPos = this.base.component.xPos + finalX;
                    this.enemies.add(newEnemy);
                }
                this.enemiesToCreate = this.enemiesToCreate - this.enemyGenerationRate;
            } else {
                this.currentEnemyCreateBuffer++;
            }
            //Get all actions from
            Enemy currentEnemy;
            int currentDistance;
            for (int i = 0; i < this.turrets.size(); i++) {
                Turret currentTurret = this.turrets.get(i);
                int closestDistance = currentTurret.range + 1;
                Enemy closestEnemy = null;
                for (int j = 0; j < this.enemies.size(); j++) {
                    currentEnemy = this.enemies.get(j);
                    if(currentEnemy.health > 0) {
                        currentDistance = this.getDistanceBetween(currentEnemy.component.xPos, currentEnemy.component.yPos, currentTurret.component.xPos, currentTurret.component.yPos);
                        if (closestDistance > currentDistance) {
                            closestEnemy = currentEnemy;
                            closestDistance = currentDistance;
                        }
                    }
                }
                if(closestEnemy != null) {
                    closestEnemy.takeDamage(currentTurret.damage);
//                    System.out.println("Enemy " + closestEnemy.health);
                    if(closestEnemy.health <= 0) {
                        this.enemies.remove(closestEnemy);
                    }
                }
            }
        } else {
//            System.out.println("Enemy delays");
            this.enemyDelay--;
        }
    }

    private void replaceActors() {
        boolean widthChange = false;
        boolean heightChange = false;
        int currentPanelWidth = this.trainingSimulator.mainFrame.painter.getWidth();
        int currentPanelHeight = this.trainingSimulator.mainFrame.painter.getHeight();
        if(this.panelWidth != currentPanelWidth) {
            widthChange = true;
            this.panelWidth = currentPanelWidth;
        }
        if(this.panelHeight != currentPanelHeight) {
            heightChange = true;
            this.panelHeight = currentPanelHeight;
        }
        //Replace Base
        if(widthChange) {
            this.base.component.xPos = (currentPanelWidth - this.base.component.width)/2;
        }
        if(heightChange) {
            this.base.component.yPos = (currentPanelHeight - this.base.component.height) / 2;
        }
        //Replace Turrets
        if(widthChange || heightChange) {
            this.positionTurrets();
        }
        //Replace Enemies
        for (int i = 0; i < this.enemies.size(); i++) {
            Enemy enemy = this.enemies.get(i);
            if(widthChange) {
                enemy.component.xPos = this.base.component.xPos + enemy.posXFromBase;
            }
            if(heightChange) {
                enemy.component.yPos = this.base.component.yPos + enemy.posYFromBase;
            }
        }
    }

    public void positionTurrets() {
//        System.out.println(Math.sin(Math.toRadians(30)));
//        System.out.println("Base x " + this.base.component.xPos + " y " + this.base.component.yPos);
//        System.out.println("Turrent distance " + this.turretDistance);
        int currentPanelWidth = this.trainingSimulator.mainFrame.painter.getWidth();
        int currentPanelHeight = this.trainingSimulator.mainFrame.painter.getHeight();
        if(this.turrets.size() == 1) {
            Turret turret = this.turrets.get(0);
            turret.component.xPos = (currentPanelWidth - turret.component.width)/2;
            turret.component.yPos = (currentPanelHeight - turret.component.height) / 2;
        } else {
            double angleIncrement = 360.00 / this.turrets.size();
//            System.out.println("Angle increment " + angleIncrement);
            double currentAngle,finalAngle;
            int opp,adj,finalX,finalY;
            for (int i = 0; i < this.turrets.size(); i++) {
                Turret currentTurret = this.turrets.get(i);
                currentAngle = angleIncrement * i;
                System.out.println("");
//                System.out.println("Current angle " + currentAngle);
                if(currentAngle >= 270) {
                    finalAngle = currentAngle - 270;
                } else if(currentAngle >= 180) {
                    finalAngle = currentAngle - 180;
                } else if(currentAngle >= 90) {
                    finalAngle = currentAngle - 90;
                } else {
                    finalAngle = currentAngle;
                }
//                System.out.println("sin " + Math.sin(Math.toRadians(finalAngle)) + " finalAngle" + finalAngle);
//                System.out.println("cos " + Math.cos(Math.toRadians(finalAngle)) + " finalAngle" + finalAngle);
                opp = (int)(Math.sin(Math.toRadians(finalAngle)) * this.turretDistance);
                adj = (int)(Math.cos(Math.toRadians(finalAngle)) * this.turretDistance);
//                System.out.println("Opp" + opp + " adj " + adj);
                if(currentAngle >= 270) {
                    finalY = -opp;
                    finalX = -adj;
                } else if (currentAngle >= 180) {
                    finalY = adj;
                    finalX = -opp;
                } else if (currentAngle >= 90) {
                    finalY = opp;
                    finalX = adj;
                } else {
                    finalY = -adj;
                    finalX = opp;
                }
//                System.out.println("Final x " + finalX + " final y" + finalY);
                currentTurret.component.yPos = (currentPanelHeight - this.base.component.height)/2 + finalY;
                currentTurret.component.xPos = (currentPanelWidth  - this.base.component.width)/2 + finalX;
//                System.out.println("Xpos " + currentTurret.component.xPos + " Ypos " + currentTurret.component.yPos);
            }
        }
    }

    private int getDistanceBetween(int x1,int y1, int x2, int y2) {
        int netX = x1 - x2;
        int netY = y1 - y2;
        return (int) Math.ceil(Math.sqrt(Math.pow(netX,2)+Math.pow(netY,2)));
    }

    public boolean levelFinished() {
        return this.levelActive;
    }
}
