package kernel.GameInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;
public abstract class Definitions {
    public static String gameName = "Snake";
    // Перечисление для хранения типов тетромино (фигур)
    public enum Tetrominoe {NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape}
    // Размеры окна
    public static int WIDTH_WINDOW = 600;
    public static int HEIGHT_WINDOW = 800;
    public static final int SQUARE_SIZE = 10;
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

}