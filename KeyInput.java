import java.awt.event.KeyAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class KeyInput extends KeyAdapter implements MouseListener {

    private final PhysicsCanvas game;

    //Vi skriver över keyInput och MousePressed för att hantera i Physics canvas
    public KeyInput (PhysicsCanvas game){
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        game.mousePressed(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}