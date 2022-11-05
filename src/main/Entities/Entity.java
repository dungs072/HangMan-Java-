package main.Entities;

import java.awt.image.BufferedImage;

public abstract class Entity
{
    protected Vector2 currentPosition;
    protected Vector2 currentSize;

    protected BufferedImage displayImage;
    
    public Entity(int x, int y,int width,int height,BufferedImage displayImage)
    {
        currentPosition = new Vector2(x, y);
        currentSize = new Vector2(width,height);
        this.displayImage = displayImage;
    }

}
