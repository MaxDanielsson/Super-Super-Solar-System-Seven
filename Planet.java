import java.awt.*;


class Planet {
    private double vx;
    private double vy;
    private final double r;
    private final long m;
    private final Color color;
    private double y;
    private double x;
    private final Point[] coordinates = new Point[100];
    private int counter = 0;

    public Planet(double x, double y, double r, long m, Color color, Planet p) { //Vi vill egentligen inte använda VX heller. Om vi räknar rätt så kommer vi kunna räkna ut en starthastighet i konstuktor
        this.x = x;
        this.y = y;
        this.r = r;
        this.m = m;
        this.color = color;


        //Skapar en hastighet som blir en eliptisk bana runt solen
        double distX = x - p.getX();
        double distY = y - p.getY();
        double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        double gravConst = 6.67408 * Math.pow(10, -7);
        double vinkelVelocity = Math.sqrt(gravConst * p.getM() / Math.pow(dist, 3));
        double velocity = vinkelVelocity * dist;
        double angle = Math.atan((p.getY() - this.y) / (p.getX() - this.x)) + Math.PI / 2;
        double velocityX = Math.cos(angle) * velocity;
        double velocityY = Math.sin(angle) * velocity;

        this.vx = (int) Math.round(velocityX);
        this.vy = (int) Math.round(velocityY);


    }

    public Planet(double x, double y, double vx, double vy, double r, long m, Color color) { //Vi vill egentligen inte använda VX heller. Om vi räknar rätt så kommer vi kunna räkna ut en starthastighet i konstuktor
        this.x = x;
        this.y = y;
        this.r = r;
        this.m = m;
        this.color = color;
        this.vx = vx;
        this.vy = vy;

    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public long getM() {
        return m;
		}
		public double getR(){
				return r;
		}

    public void update(double Fx, double Fy) {

        vx += Fx / m;
        vy += Fy / m;
        x += vx;
        y += vy;


    }

    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int) Math.round(x - r), (int) Math.round(y - r), (int) Math.round(2 * r), (int) Math.round(2 * r));
        tail(g);
    }
    //Skapar en svans som visar de senaste positionerna som planeten har haft
    private void tail(Graphics2D g) {
        if (counter == 100) {
            counter = 0;
        }
        coordinates[counter] = new Point((int) x, (int) y);
        counter++;
        for (Point coord : coordinates) {
            if (coord != null) {
                g.fillOval(((int) coord.getX()), ((int) coord.getY()), 1, 1);

            }
        }
    }
}