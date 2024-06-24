package kernel.Controllers;

import kernel.Vindow.View;
import kernel.Vindow.ViewSize;

import java.util.HashMap;
import java.util.Objects;

public class Controller {
    public HashMap<String, Integer> data;
    public String complexity;
    private View view;

    public Controller() {
        data = new HashMap<>();
        new ViewSize(this);
    }
    public void restartGame(){
        view.setVisible(false);
        data = new HashMap<>();
        new ViewSize(this);
    }

    public void CreatGame() {
        this.view = new View();
        view.add(new GameController(this));
        view.setVisible(true);
    }

    public void setSizeWorld(String complexity) {
        data.put("BOARD_WIDTH", 29);
        data.put("BOARD_HEIGHT", 38);
        if (Objects.equals(complexity, "lite")) {
            data.put("fallingSpeed", 200);
            data.put("numberOfBlocks", 10);
        } else if (Objects.equals(complexity, "norm")) {
            data.put("fallingSpeed", 100);
            data.put("numberOfBlocks", 7);
        } else if (Objects.equals(complexity, "hard")) {
            data.put("fallingSpeed", 50);
            data.put("numberOfBlocks", 5);
        }
        this.complexity = complexity;
    }
}
