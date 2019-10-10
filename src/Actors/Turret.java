package Actors;

import Visuals.VisualComponent;
import Visuals.VisualShape;

import java.awt.*;

public class Turret extends Actor{
    public int range = 200;
    public int damage = 20;
    public Turret() {
        this.component = new VisualComponent();
        this.size = 50;
        this.component.shape = VisualShape.Circle;
        this.component.width = this.size;
        this.component.height = this.size;
        this.component.color = Color.BLACK;
    }
}
