package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public ArrayList<ChessMove> moves = new ArrayList<>();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) { return null; }

    /**
     * Gets the valid moves a piece can make (used for all except pawn)
     *
     * @param board a ChessBoard
     * @param myPosition the position of the piece being moved
     * @param directions an array of row-col pairs that represent the directions that the piece can move
     * @param hasOneMove true if the piece can move one square (king, knight) and false if it can move multiple (queen, bishop, rook)
     * @return a collection of valid moves
     */
    public Collection<ChessMove> pieceMovesHelper(ChessBoard board, ChessPosition myPosition, int[][] directions, boolean hasOneMove) {
        // color of the piece being moved
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        // loops through all the possible directions the piece can move
        for (int[] direction : directions) {
            int row = myPosition.getRow() + direction[0];
            int col = myPosition.getColumn() + direction[1];

            while (isValidMove(board, new ChessPosition(row, col), color)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));

                if (isSpaceEnemy(board, new ChessPosition(row, col), color)) { break; }
                if (hasOneMove) { break; }

                row += direction[0];
                col += direction[1];
            }
        }

        return moves;
    }

    /**
     * Determines if a move is valid
     *
     * @param board a ChessBoard
     * @param position the position that the piece is moving to
     * @param color the color of the piece being moved (white or black)
     * @return true if the move is valid
     */
    public boolean isValidMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        //in bounds AND (empty OR enemy)
        return isInBounds(position) &&
                (isSpaceEmpty(board, position) || isSpaceEnemy(board, position, color));
    }

    /**
     * Determines if a position is in bounds
     *
     * @param position the position being moved to
     * @return true if the position is in bounds
     */
    public boolean isInBounds(ChessPosition position) {
        return position.getColumn() >= 1 &&
                position.getRow() >= 1 &&
                position.getRow() <= 8 &&
                position.getColumn() <= 8;
    }

    /**
     * Determines if the space being moved to is empty
     *
     * @param board a ChessBoard
     * @param position the position being moved to
     * @return true is the space is empty (i.e. no other piece is in that space)
     */
    public boolean isSpaceEmpty(ChessBoard board, ChessPosition position) {
        return board.getPiece(position) == null;
    }

    /**
     * Determines if the space being moved to contains an enemy
     *
     * @param board a ChessBoard
     * @param position the position being moved to
     * @param color the color of the piece being moved
     * @return true is the space contains an enemy
     */
    public boolean isSpaceEnemy(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        if (board.getPiece(position) == null) { return false; } //checks if the space is empty
        return board.getPiece(position).getTeamColor() != color; // checks if the space contains a piece of the opposite color
    }

}

