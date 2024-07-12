import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuVisualizer extends JFrame implements SudokuSolver.VisualizerCallback {
    private static final int SIZE = 9;
    private static final Font CELL_FONT = new Font("Arial", Font.BOLD, 20);
    private JTextField[][] cells;
    private int[][] board;
    private SudokuSolver solver;

    public SudokuVisualizer() {
        solver = new SudokuSolver(this);
        cells = new JTextField[SIZE][SIZE];
        board = new int[SIZE][SIZE];

        setTitle("Sudoku Solver Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        initializeGrid(gridPanel);
        add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new SolveAction());
        controlPanel.add(solveButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearAction());
        controlPanel.add(clearButton);

        JButton loadSampleButton = new JButton("Load Sample");
        loadSampleButton.addActionListener(new LoadSampleAction());
        controlPanel.add(loadSampleButton);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeGrid(JPanel gridPanel) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(CELL_FONT);
                cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(cells[row][col]);
            }
        }
    }

    private boolean readBoard() {
        try {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    String text = cells[row][col].getText().trim();
                    if (text.isEmpty()) {
                        board[row][col] = 0;
                    } else {
                        int value = Integer.parseInt(text);
                        if (value < 1 || value > 9) {
                            return false;
                        }
                        board[row][col] = value;
                    }
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void displayBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
            }
        }
    }

    @Override
    public void updateBoard(int[][] board) {
        SwingUtilities.invokeLater(() -> {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    cells[row][col].setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
                    cells[row][col].setBackground(board[row][col] == 0 ? Color.WHITE : Color.CYAN);
                }
            }
        });
    }

    private class SolveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (readBoard()) {
                SwingWorker<Boolean, int[][]> worker = new SwingWorker<>() {
                    @Override
                    protected Boolean doInBackground() {
                        return solver.solve(board);
                    }

                    @Override
                    protected void done() {
                        try {
                            if (get()) {
                                displayBoard();
                            } else {
                                JOptionPane.showMessageDialog(null, "Unsolvable puzzle");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                worker.execute();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input");
            }
        }
    }

    private class ClearAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    cells[row][col].setText("");
                    cells[row][col].setBackground(Color.WHITE);
                }
            }
            board = new int[SIZE][SIZE]; // Clear the board array
        }
    }

    private class LoadSampleAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] sampleBoard = {
                    {5, 3, 0, 0, 7, 0, 0, 0, 0},
                    {6, 0, 0, 1, 9, 5, 0, 0, 0},
                    {0, 9, 8, 0, 0, 0, 0, 6, 0},
                    {8, 0, 0, 0, 6, 0, 0, 0, 3},
                    {4, 0, 0, 8, 0, 3, 0, 0, 1},
                    {7, 0, 0, 0, 2, 0, 0, 0, 6},
                    {0, 6, 0, 0, 0, 0, 2, 8, 0},
                    {0, 0, 0, 4, 1, 9, 0, 0, 5},
                    {0, 0, 0, 0, 8, 0, 0, 7, 9}
            };
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    board[row][col] = sampleBoard[row][col];
                    cells[row][col].setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuVisualizer::new);
    }
}