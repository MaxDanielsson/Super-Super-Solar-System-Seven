import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class PhysicsCanvas extends Canvas implements Runnable {

    private boolean running;
    private List<Planet> planetList = new ArrayList<>();
    private double gravConst;
    private double score = 0;
    private Font myFont = new Font("Impact", 1, 50);
    public static int width = 1000;
    public static int height = 800;
    private Menu menu = new Menu();
    private Sun sun = new Sun(width / 2, height / 2, 0, 0, 20, 900000000, Color.BLUE); // Solens massa nedskalat

    public enum STATE {
        MENU, GAME
    }

    private static STATE state = STATE.MENU;

    public PhysicsCanvas() {
        Dimension d = new Dimension(width, height);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        gravConst = 6.67408 * Math.pow(10, -7);
        planetList.add(sun);
    }


    //Kodskelett
    @Override
    public void run() {
        addKeyListener(new KeyInput(this)); // Functionality for keyboard
        addMouseListener(new KeyInput(this)); // Functionality for clicks
        while (running) {
            update();
            render();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }

    public void start() {
        if (!running) {
            Thread t = new Thread(this);
            createBufferStrategy(3);
            running = true;
            t.start();
        }
    }

    private void render() {
        BufferStrategy strategy = getBufferStrategy();
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (state == STATE.MENU) {
            g.clearRect(0, 0, width, height);
            menu.render(g);
            score = 0;
        } else {
            for (int i = 0; i < planetList.size(); i++) {
                Planet p = planetList.get(i);
                p.render(g);
            }
            g.setColor(Color.BLACK);
            g.setFont(myFont);
            g.drawString("Score: " + String.valueOf((int) score), 400, 750);
        }

        strategy.show();
    }

    private void update() {
        if (state == STATE.GAME) {
            for (int i = 0; i < planetList.size(); i++) {
                Planet p = planetList.get(i);
                double[] fxy = calcF(p);
                if (!(p instanceof Sun)) {
                    score += p.getM() / 200000;
                }
                p.update(fxy[0], fxy[1]);
                detectCollision(p);
                outOfBounds(p);
            }
        }
    }
    //Vi undersöker om spelaren är out-of-bounds --> startar om spelet
    private void outOfBounds(Planet p) {
        int border = 100;
        if ((p.getX() < 0 || p.getY() < 0 || p.getX() > PhysicsCanvas.width || p.getY() > PhysicsCanvas.height)
               && !(p instanceof Sun)) {

            state = STATE.MENU;
            planetList.clear();
            planetList.add(sun);
        }else if (p instanceof Sun){

            if(p.getX() > width-border || p.getX() < border)p.setVx(p.getVx()*-1);
            if(p.getY() > height-border || p.getY() < border)p.setVy(p.getVy()*-1);
        }
    }
    //Vi räknar ut nettokraften mellan alla planeter i systemet (inklusive solen) och sparar den resulterande kraften.
    private double[] calcF(Planet p1) {
        double[] fxy = new double[2];
        fxy[0] = 0;
        fxy[1] = 0;

        for (int i = 0; i < planetList.size(); i++) {
            Planet p = planetList.get(i);
            if (p != p1) {
                double distX = p1.getX() - p.getX();
                double distY = p1.getY() - p.getY();
                double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
                double fNet = gravConst * p1.getM() * p.getM() / (Math.pow(dist, 2));// + 0.1;
                fxy[0] += -fNet * distX / dist;
                fxy[1] += -fNet * distY / dist;
            }
        }
        return fxy; // calc netforces Y for planetp
    }
    //Vi kollar om en planet är tillräckligt nära en annan planet för att en kollision ska ske
    private void detectCollision(Planet p) {
        for (int j = 0; j < planetList.size(); j++) {
            Planet t = planetList.get(j);
            if (p != t) {
                if ((p.getX() <= t.getX() + p.getR() && p.getY() <= t.getY() + p.getR())
                        && (p.getX() >= t.getX() - p.getR() && p.getY() >= t.getY() - p.getR())) {
                    if (p.getM() > t.getM() * 10) {
                        planetList.remove(t);
                    } else if (t.getM() > p.getM() * 10) {
                        planetList.remove(p);
                    }else {
                        Planet pNew = new Planet(p.getX(), p.getY(), (p.getVx() + t.getVx()) / 2,
                                (p.getVy() + t.getVy()) / 2, 5 + (p.getR() + t.getR())/3, p.getM() + t.getM(), Color.MAGENTA);
                        planetList.remove(p);
                        planetList.remove(t);
                        planetList.add(pNew);
                    }
                }
            }
        }
    }
    //Vi hanterar musklick så att nya planeter kan skapas
    void mousePressed(MouseEvent mouseEvent) { // Implements event mousePressed and "summons" a new planet.
        int mx = mouseEvent.getX();
        int my = mouseEvent.getY();

        if (PhysicsCanvas.state == STATE.MENU) { // Kollar om din muspekare är inom rätt område på skärmen
            if (mx >= PhysicsCanvas.width / 2 - 100 && mx <= PhysicsCanvas.width / 2 + 100) {
                if (my <= PhysicsCanvas.height / 2 + 100 && my >= PhysicsCanvas.height / 2) {
                    Object[] options = new Object[3];
                    options[0] = ("Ja");
                    options[1] = ("Surprise me!");
                    options[2] = ("Nej");
                    int a = JOptionPane.showOptionDialog(this, "Är du säker på att du vill avsluta?", "Avsluta", JOptionPane.INFORMATION_MESSAGE, 0, null, options, 0);
                    
                     if (a == 1) {
                        if(Math.random() < 0.5){
                            PhysicsCanvas.state = STATE.MENU; //gitPvP
                        }
                        else{
                            System.exit(0);
                        }
                     }
                     else if (a == 0){
                         System.exit(0);
                     }
                 } else if (my <= PhysicsCanvas.height/4 +100 && my >= PhysicsCanvas.height/4) {
                        PhysicsCanvas.state = STATE.GAME; //gitPvP
                 }
             }
         }
         else{
             Planet pNew = new Planet(mx, my, 5, 250000, Color.BLACK, sun);
             planetList.add(pNew);
         }
     }

    public static void main(String[] args) {
        System.setProperty("Sun.java2d.opengl", "True");
        JFrame myFrame = new JFrame("Superb Super Solar System Simulator Seven");
        PhysicsCanvas physics = new PhysicsCanvas();
        myFrame.add(physics);
        myFrame.pack();
        myFrame.setResizable(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        physics.start();
        myFrame.setLocationRelativeTo(null);

    }


}