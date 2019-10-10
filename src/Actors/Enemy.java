package Actors;

import Visuals.VisualComponent;
import Visuals.VisualShape;

import java.awt.*;

public class Enemy extends Actor{
    public int posXFromBase,posYFromBase;
    public int movementPerLoop = 1;
    public int health = 100;
    public Enemy(){
        this.size = 50;
        this.component = new VisualComponent();
        this.component.shape = VisualShape.Circle;
        this.component.width = this.size;
        this.component.height = this.size;
        this.component.color = Color.RED;
    }
    public void takeDamage(int damage) {
        this.health = this.health - damage;
    }
}
