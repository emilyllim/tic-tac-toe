import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * An intelligent player for the game of TicTacToe.
 *
 */
public class AI extends Player {

    public HashMap<Board, Outcome[]> ticTacToe = new HashMap<Board, Outcome[]>();
    public ArrayList<Board> history; // To store board history
    public ArrayList<Integer> cellHistory; // To store moves history
    Board empty = new Board(); // an empty board to use for comparisons later

    /**
     * _ Part 1: Implement this method _
     *
     * This method is called by the TicTacToe class when a new game begins.
     * Any work that is done to prepare for a new game should be done here.
     * However, you should *not* reset any outcome data that you have already
     * obtained.  
     * 
     * That is, outcome data should persists across multiple individual games
     * (otherwise your statistics will be really boring!), but there may
     * still be things you need to do when a new game starts. If so, do them 
     * here.
     *
     * @param dim the width (also height) of the board
     * @param playernum 1 if player 1; 2 if player 2
     * @param q true iff the player should be "quiet" during play (to minimize output)
     */
    public void startGame(int dim, int playernum, boolean q) {
        // Sets up game same as the player class
        super.startGame(dim, playernum, q);
        // Creates history and cellHistory array lists
        history = new ArrayList<Board>();
        cellHistory = new ArrayList<Integer>();
        return;
    }

    /**
     * _ Part 2: Implement this method _
     *
     * This method is called by the TicTacToe class when the
     * player is being asked for a move.  The current Board
     * (state) is passed in, and the player should respond 
     * with an integer value corresponding to the cell it
     * would like to move into. (cell numbers start at 0
     * and increase from left to right and top to bottom).
     *
     * @param board the current board
     *
     * @return the cell the player would like to move into
     */
    public int requestMove(Board board){
        // First, AI gets a move based on board passed in
        int cell = super.requestMove(board);
        // Array of outcomes to be added to hashmap if needed
        Outcome[] outcomes = new Outcome[9];
        Outcome[] outcomes2 = new Outcome[9];
        Outcome[] outcomes3 = new Outcome[9];

        // If an empty board is not already in the hashmap, it is added
        if (ticTacToe.get(empty) == null) {
            for (int i = 0; i < outcomes.length; i++) {
                outcomes[i] = new Outcome();
            }
            ticTacToe.put(empty, outcomes);
        }

        // If the board passed in is not an empty board, but the
        // game has just started, then the empty board is added to the
        // game's history and -1 is added to cell history
        if ((board.equals(empty) == false) && (history.size() == 0)){
            history.add(empty);
            cellHistory.add(empty.getMostRecentCellOccupied());
        }

        // The board passed in is added to history and the last move made
        // is added to cell history
        history.add(board);
        cellHistory.add(board.getMostRecentCellOccupied());

        // If board passed in does not exist in the hashmap, then
        // it is added
        if (ticTacToe.get(board) == null){
            for (int i = 0; i < outcomes2.length; i++) {
                outcomes2[i] = new Outcome();
            }
            ticTacToe.put(board, outcomes2);
        }

        // A new board that represents what happens when AI makes the next move
        // is added to the history and the move is added to cell history
        Board myBoard = board.move(marker, cell);
        history.add(myBoard);
        cellHistory.add(myBoard.getMostRecentCellOccupied());

        // If that board is not already in hashmap, then it is added
        if (ticTacToe.get(myBoard) == null){
            for (int i = 0; i < outcomes3.length; i++) {
                outcomes3[i] = new Outcome();
            }
            ticTacToe.put(myBoard, outcomes3);
        }

        // Move that AI made is returned
        return cell;
    }


    /**
     *
     * _ Part 3: Implement this method _
     *
     * This method is called by the TicTacToe game when a 
     * game completes.  It passes in the final board state
     * (since this player may not know what it is, if the 
     * opponent moved last) and the winner.  Note that
     * the argument winner, is not necessarily consistent
     * with the winner obtianed via b.getWinner() because
     * it is possible that the winner was declared by
     * disqualification. In that case, the TicTacToe game 
     * declares the winner (passed here as the argument winner)
     * but b.getWinner() will return -1 (since the game appears
     * incomplete).
     * 
     * Any work to compute outcomes should probably happen here
     * once you know how the game ended.
     *
     * @param b
     * @param winner
     */
    public void endGame(Board b, int winner) {
        Outcome[] values = null; // To store outcome array list that will be updated

        // If the last board added to history is not board b, then board b is
        // added to the history and it's move is added to cell history
        if (history.get(history.size()-1).equals(b) == false){
            history.add(b);
            cellHistory.add(b.getMostRecentCellOccupied());
        }

        // If player 1 is the winner
        if (winner == 1){
            // Updating p1 wins of all outcomes of each board in history (except final)
            for (int i = 1; i < history.size(); i++){
                values = ticTacToe.get(history.get(i-1));
                values[history.get(i).getMostRecentCellOccupied()].p1wins += 1;
            }
        }

        // If player 2 is the winner
        if (winner == 2){
            // Updating p2 wins of all outcomes of each board in history (except final)
            for (int i = 1; i < history.size(); i++){
                values = ticTacToe.get(history.get(i-1));
                values[history.get(i).getMostRecentCellOccupied()].p2wins += 1;
            }
        }

        // Updating attempts of all outcomes of each board in history (except final)
        for (int i = 1; i < history.size(); i++){
            values = ticTacToe.get(history.get(i-1));
            values[history.get(i).getMostRecentCellOccupied()].attempts += 1;
        }

        // Tests
//        Outcome[] outcomeAdd = ticTacToe.get(empty);
//        int attempt = 0;
//        for (int i = 0; i < outcomeAdd.length; i++){
//            attempt += outcomeAdd[i].attempts;
//        }
//        System.out.println("# of games: " + attempt);
//        for (int j = 0; j < history.size(); j++){
//            System.out.println(history.get(j).toString());
//        }

        return;
    }

    /**
     * _ Part 4: Implement this method _
     *
     * Retrieve an outcome for a particular move from a particular board
     * state. If that board/move combination was never encountered, return
     * null.

     * @param state
     * @param move
     * @return
     */
    public Outcome getOutcomeForMove(Board state, int move) {
        // Gets outcome array of that board state
        Outcome[] boardValue = (Outcome[]) ticTacToe.get(state);
        // Returns outcome of that move in that board (may be null if never encountered)
        return boardValue[move];
    }


}
