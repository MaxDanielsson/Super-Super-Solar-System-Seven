import java.awt.*;

class Menu {
    //Hanterar den enkla meny-designen
    private final Rectangle quitBtn = new Rectangle(PhysicsCanvas.width/2 -100, PhysicsCanvas.height/2 , 200, 100);
    private final Rectangle play1Btn = new Rectangle(PhysicsCanvas.width/2 -100, PhysicsCanvas.height/4, 200, 100);

    public void render (Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.black);

        Font fnt1 = new Font("comic sans", Font.ITALIC, 25);
        g.setFont(fnt1);
        g.drawString("Start", play1Btn.x+65,play1Btn.y+55);
        g.drawString("Quit", quitBtn.x+65,quitBtn.y+55);


        g2d.draw(play1Btn);
        g2d.draw(quitBtn);

    }

}