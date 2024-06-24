package kernel.Controllers;

import kernel.GameInfo.Definitions;
import kernel.Vindow.GameOverPanel;
import kernel.Vindow.PressNamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController extends JPanel {

    private Timer timer;
    private boolean isPaused = false;
    private int score = 0;
    private List<Point> snake;
    private Point food;
    private String namePlayer = null;
    private long startTime;
    private final Controller controller;
    public int BOARD_WIDTH;
    public int BOARD_HEIGHT;
    public String complexity;
    private static final int INITIAL_SNAKE_SIZE = 3;
    private static final int SQUARE_SIZE = 20;
    private Definitions.Direction currentDirection;
    private Definitions.Direction nextDirection;
    private final int fallingSpeed;
    private List<Rectangle> blueBlocks;
    private final int numberOfBlocks;

    public GameController(Controller controller) {
        this.controller = controller;
        this.BOARD_WIDTH = controller.data.get("BOARD_WIDTH");
        this.BOARD_HEIGHT = controller.data.get("BOARD_HEIGHT");
        this.fallingSpeed = controller.data.get("fallingSpeed");
        this.numberOfBlocks = controller.data.get("numberOfBlocks");
        this.complexity = controller.complexity;
        setFocusable(true);
        addKeyListener(new TAdapter(this));
        startGame();
    }

    /**
     * Запуск игры.
     */
    public void startGame() {
        snake = new ArrayList<>();
        for (int i = 0; i < INITIAL_SNAKE_SIZE; i++) {
            snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2 + i));
        }
        setDirection(String.valueOf(Definitions.Direction.RIGHT));
        spawnBlueBlocks();
        spawnFood();
        timer = new Timer(fallingSpeed, new GameCycle());
        timer.start();
        timerStart();
    }


    /**
     * Переключение паузы в игре.
     */
    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }
        repaint();
    }

    /**
     * Отрисовка игрового поля и текущей змейки.
     * @param g Графический контекст для отрисовки.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderGame(g);
    }

    /**
     * Отрисовка игрового поля и объектов на нем.
     * @param g Графический контекст для отрисовки.
     */
    private void renderGame(Graphics g) {
        Dimension size = getSize();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, size.width, size.height);

        // Отрисовка змейки
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * SQUARE_SIZE, point.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }

        // Отрисовка еды
        g.setColor(Color.RED);
        g.fillRect(food.x * SQUARE_SIZE, food.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

        // Отрисовка синих блоков
        g.setColor(Color.BLUE);
        for (Rectangle block : blueBlocks) {
            g.fillRect(block.x * SQUARE_SIZE, block.y * SQUARE_SIZE, block.width * SQUARE_SIZE, block.height * SQUARE_SIZE);
        }

        // Отображение счета
        g.setColor(Color.YELLOW);
        g.drawString("Apple: " + score, 10, 10);
    }

    /**
     * Обновление состояния игры на каждой итерации.
     */
    private void updateGame() {
        if (isPaused) {
            return;
        }
        moveSnake();
        checkCollision();
        checkFood();
    }

    /**
     * Перемещение змейки в текущем направлении.
     */
    private void moveSnake() {
        if (nextDirection != null) {
            currentDirection = nextDirection;
            nextDirection = null;
        }
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (currentDirection) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }
        snake.addFirst(newHead);
        snake.removeLast();
    }

    /**
     * Проверка столкновений змейки с границами и своим телом.
     */
    private void checkCollision() {
        Point head = snake.getFirst();

        // Проверка столкновения с границами
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            gameOver();
        }

        // Проверка столкновения с собственным телом
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
            }
        }
        //Проверка столкновений с синими блоками.
        for (Rectangle block : blueBlocks) {
            if (block.contains(head)) {
                gameOver();
            }
        }
    }

    /**
     * Проверка, съела ли змейка еду.
     */
    private void checkFood() {
        Point head = snake.getFirst();

        if (head.equals(food)) {
            snake.add(new Point(food));
            score += 1;
            spawnFood();
        }
    }

    /**
     * Спавн новой еды на случайной позиции.
     */
    private void spawnFood() {
        Random random = new Random();
        boolean validPosition = false;

        while (!validPosition) {
            int x = random.nextInt(BOARD_WIDTH);
            int y = random.nextInt(BOARD_HEIGHT);
            food = new Point(x, y);

            validPosition = true;
            for (Rectangle block : blueBlocks) {
                if (block.contains(food)) {
                    validPosition = false;
                    break;
                }
            }
        }
    }

    /**
     * Спавн синих блоков на случайных позициях.
     */
    private void spawnBlueBlocks() {
        blueBlocks = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfBlocks; i++) {
            int x = random.nextInt(BOARD_WIDTH);
            int y = random.nextInt(BOARD_HEIGHT);
            int width = random.nextInt(3) + 1;
            int height = random.nextInt(3) + 1;
            blueBlocks.add(new Rectangle(x, y, width, height));
        }
    }

    /**
     * Завершение игры.
     */
    private void gameOver() {
        timer.stop();
        PressNamePanel pressNamePanel = new PressNamePanel(this);
        getParent().add(pressNamePanel);
        setVisible(false);
    }

    /**
     * Запуск таймера.
     */
    private void timerStart() {
        this.startTime = System.nanoTime();
    }

    /**
     * Получение текущего времени.
     * @return Текущее время в секундах.
     */
    public int getTime() {
        long endTime = System.nanoTime();
        double durationSeconds = (endTime - startTime) / 1e9;
        return (int) durationSeconds;
    }

    /**
     * Получение имени игрока.
     * @return Имя игрока.
     */
    public String getNamePlayer() {
        return this.namePlayer;
    }

    /**
     * Получение текущего счета.
     * @return Текущий счет.
     */
    public int getScore() {
        return score;
    }

    /**
     * Перезапуск игры.
     */
    public void restartGame() {
        controller.restartGame();
    }

    /**
     * Установка имени игрока.
     * @param namePlayer Имя игрока.
     */
    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }
    /**
     * Установка нового направления движения змейки.
     * @param direction Новое направление движения змейки.
     */
    public void setDirection(String direction) {
        switch (direction) {
            case "UP":
                if (currentDirection != Definitions.Direction.DOWN) {
                    currentDirection = Definitions.Direction.UP;
                }
                break;
            case "DOWN":
                if (currentDirection != Definitions.Direction.UP) {
                    currentDirection = Definitions.Direction.DOWN;
                }
                break;
            case "LEFT":
                if (currentDirection != Definitions.Direction.RIGHT) {
                    currentDirection = Definitions.Direction.LEFT;
                }
                break;
            case "RIGHT":
                if (currentDirection != Definitions.Direction.LEFT) {
                    currentDirection = Definitions.Direction.RIGHT;
                }
                break;
            case "PAUSE":
                togglePause();
                break;
        }
    }

    /**
     * Отображение панели GameOver.
     */
    public void viewGameOverPanel() {
        GameOverPanel gameOverPanel = new GameOverPanel(this);
        getParent().add(gameOverPanel);
        gameOverPanel.setVisible(true);
    }

    /**
     * Завершение игры.
     */
    public void exitGame() {
        System.exit(1);
    }

    /**
     * Внутренний класс для обработки игрового цикла.
     */
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateGame();
            repaint();
        }
    }
}
