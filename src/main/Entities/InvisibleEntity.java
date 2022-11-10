package main.Entities;

public class InvisibleEntity {
    protected Vector2 currentPosition;
    protected Vector2 currentSize;
    public InvisibleEntity(int x, int y,int width,int height)
    {
        currentPosition = new Vector2(x, y);
        currentSize = new Vector2(width,height);
    }
    public Vector2 getCurrentPosition()
    {
        return currentPosition.copy();
    }
    public Vector2 getCurrentSize()
    {
        return currentSize.copy();
    }
}
