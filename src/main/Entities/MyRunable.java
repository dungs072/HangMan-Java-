package main.Entities;
import java.awt.Graphics2D;
public interface MyRunable {
    public void update(long timeDeltaTime);
    public void paint(Graphics2D g2);
}
