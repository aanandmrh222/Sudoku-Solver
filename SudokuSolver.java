public class SudokuSolver {
    private static final int SIZE = 9;
    private static final int EMPTY = 0;
    private VisualizerCallback callback;

    public SudokuSolver(VisualizerCallback callback) {
        this.callback = callback;
    }

    public boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            callback.updateBoard(board);
                            sleep();  // Add delay for visualization
                            if (solve(board)) {
                                return true;
                            }
                            board[row][col] = EMPTY;
                            callback.updateBoard(board);
                            sleep();  // Add delay for visualization
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num || board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private void sleep() {
        try {
            Thread.sleep(15);  // Adjust the delay for smoother animation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public interface VisualizerCallback {
        void updateBoard(int[][] board);
    }
}
