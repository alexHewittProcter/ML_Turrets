package Actors;

import Visuals.VisualComponent;
import Visuals.VisualShape;

import java.awt.*;

public class Base extends Actor{
    public double health = 100;
    public Base() {
        this.size = 50;
        this.component = new VisualComponent();
        this.component.shape = VisualShape.Circle;
        this.component.width = this.size;
        this.component.height = this.size;
        this.component.color = Color.cyan;
    }
}
