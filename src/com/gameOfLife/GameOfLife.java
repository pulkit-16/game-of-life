package com.gameOfLife;

import processing.core.PApplet;
import java.util.ArrayList;

public class GameOfLife extends PApplet {

    int rows = 30;
    int cols = 30;
    int cellSize = 20;
    int[][] grid = new int[rows][cols];
    int[][] nextGrid = new int[rows][cols];
    boolean running = false;

    public static void main(String[] args) {
        PApplet.main("com.gameOfLife.GameOfLife");
    }

    public void settings() {
        size(cols * cellSize, rows * cellSize + 50);
    }

    public void setup() {
        frameRate(10);
        randomizeGrid();
        createButtons();
    }

    public void draw() {
        background(255);
        drawGrid();
        for (Button button : buttons) {
            button.checkHover();
            button.draw();
            button.checkClick();
        }
        if (running) {
            updateGrid();
        }
    }

    void drawGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    fill(0);
                } else {
                    fill(255);
                }
                stroke(0);
                rect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    void updateGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int livingNeighbors = countLivingNeighbors(i, j);

                if (grid[i][j] == 1) {
                    if (livingNeighbors < 2 || livingNeighbors > 3) {
                        nextGrid[i][j] = 0;
                    } else {
                        nextGrid[i][j] = 1;
                    }
                } else {
                    if (livingNeighbors == 3) {
                        nextGrid[i][j] = 1;
                    } else {
                        nextGrid[i][j] = 0;
                    }
                }
            }
        }

        int[][] temp = grid;
        grid = nextGrid;
        nextGrid = temp;
    }

    int countLivingNeighbors(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    count += grid[r][c];
                }
            }
        }
        return count;
    }

    void randomizeGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = (int) random(2);
            }
        }
    }

    class Button {
        PApplet parent;
        String label;
        int x, y, w, h;
        boolean hovered = false;
        Runnable onClick;

        Button(PApplet parent, String label, int x, int y, int w, int h) {
            this.parent = parent;
            this.label = label;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        void setOnClickListener(Runnable onClick) {
            this.onClick = onClick;
        }

        void draw() {
            parent.fill(hovered ? 200 : 150);
            parent.rect(x, y, w, h);
            parent.fill(0);
            parent.textAlign(CENTER, CENTER);
            parent.text(label, x + w / 2, y + h / 2);
        }

        void checkHover() {
            hovered = parent.mouseX > x && parent.mouseX < x + w && parent.mouseY > y && parent.mouseY < y + h;
        }

        void checkClick() {
            if (hovered && parent.mousePressed) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        }
    }

    ArrayList<Button> buttons = new ArrayList<>();

    void createButtons() {
        Button startStopButton = new Button(this, "Start/Stop", 10, height - 40, 100, 30);
        startStopButton.setOnClickListener(() -> running = !running);
        buttons.add(startStopButton);

        Button randomizeButton = new Button(this, "Randomize", 120, height - 40, 100, 30);
        randomizeButton.setOnClickListener(this::randomizeGrid);
        buttons.add(randomizeButton);
    }

    public void mousePressed() {
        int col = mouseX / cellSize;
        int row = mouseY / cellSize;
        if (row < rows && col < cols) {
            grid[row][col] = grid[row][col] == 1 ? 0 : 1;
        }
    }
}
