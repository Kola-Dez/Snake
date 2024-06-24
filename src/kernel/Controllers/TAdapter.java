package kernel.Controllers;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import kernel.GameInfo.Definitions.Direction;

public class TAdapter extends KeyAdapter {
    private final GameController gameController;

    public TAdapter(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_W:
                gameController.setDirection(String.valueOf(Direction.UP));
                break;
            case KeyEvent.VK_S:
                gameController.setDirection(String.valueOf(Direction.DOWN));
                break;
            case KeyEvent.VK_A:
                gameController.setDirection(String.valueOf(Direction.LEFT));
                break;
            case KeyEvent.VK_D:
                gameController.setDirection(String.valueOf(Direction.RIGHT));
                break;
            case KeyEvent.VK_SHIFT:
                gameController.togglePause();
                break;
        }
    }
}
