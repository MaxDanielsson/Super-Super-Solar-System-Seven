import java.awt.*;


@SuppressWarnings("unused")
class Particle {
	
	private double vx;
	private double vy;
	private final double r;
	private final Color color;

	private final Point[] coordinates = new Point[100];
	private int counter = 0;
	
	private double x;

	public double getX() {
		return x;
	}

	private double y;
	public double getY() {
		return y;
	}

	public Particle(double x, double y, double r, Color color) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.color = color;
		this.vx = -1.0;
		this.vy = -0.5;
	}
	
	
	public void update() {
		x += vx;
		y += vy;
		
		if (x<r) vx *= -1;
		if (y<r) vy *= -1;

	}
	
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int)Math.round(x-r), (int)Math.round(y-r), (int)Math.round(2*r), (int)Math.round(2*r));
		tail(g);
	}

	private void tail(Graphics2D g){
		if (counter == coordinates.length){
			counter = 0;
		}
		coordinates[counter] = new Point((int)x,(int)y);
		counter++;
		for (Point coord: coordinates) {
			if (coord != null){
				g.fillOval(((int)coord.getX()),((int)coord.getY()), 1, 1);

			}
		}
	}
}
