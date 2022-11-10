package main.Entities;

import java.awt.image.BufferedImage;

public class Entity extends InvisibleEntity
{
    protected BufferedImage displayImage;
    public Entity(int x, int y, int width, int height,BufferedImage displayImage) {
        super(x, y, width, height);
        this.displayImage = displayImage;
    }

}
