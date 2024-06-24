
package kernel.Vindow;

import kernel.GameInfo.Definitions;

import javax.swing.*;

public class View extends JFrame {

    public View() {
        setTitle(Definitions.gameName);
        setSize(Definitions.WIDTH_WINDOW, Definitions.HEIGHT_WINDOW);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}