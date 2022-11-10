package main.Entities;

public class Vector2 {
    private int x;
    private int y;
    public Vector2(int x,int y)
    {
        setPos(x, y);
    }
    public void setPos(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    public void setX(int x)
    {
        this.x = x;
    }
    public void setY(int y)
    {
        this.y = y;
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public Vector2 copy()
    {
        return new Vector2(x, y);
    }
}
