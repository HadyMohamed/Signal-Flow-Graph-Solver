import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Circle {
	Point position = new Point(0, 0);
	int radius;
	int number;
	public void draw(Graphics g) {
		int x=position.x - radius/2;
		int y=position.y  - radius/2;
		int r=radius;
        g.setColor(Color.black);
        g.fillOval(x,y,r,r);
        String s = "Node " + number;
        g.setColor(Color.black);
        g.drawString(s, x, y);
	}
	public boolean isAt(Point point){
		int x=position.x - radius/2;
		int y=position.y  - radius/2;
		return Math.pow((point.getX() - x), 2) +
				Math.pow((point.getY() - y), 2) < radius*radius;
    }
	
	
}
