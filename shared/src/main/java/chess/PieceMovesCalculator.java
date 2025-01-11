package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public ArrayList<ChessMove> moves = new ArrayList<>();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    public Collection<ChessMove> pieceMovesHelper(ChessBoard board, ChessPosition myPosition, int[][] directions, boolean hasOneMove) {
        // color of the piece being moved
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        // loops through all the directions the piece can move
        for (int[] direction : directions) {
            int row = myPosition.getRow() + direction[0];
            int col = myPosition.getColumn() + direction[1];
            while (isValidMove(board, new ChessPosition(row, col), color)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));

                if (isSpaceEnemy(board, new ChessPosition(row, col), color)) break;
                if (hasOneMove) break;

                row += direction[0];
                col += direction[1];
            }
        }

        return moves;
    }

    public boolean isValidMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        //in bounds AND (empty OR enemy)
        return isInBounds(position) &&
                (isSpaceEmpty(board, position) || isSpaceEnemy(board, position, color));
    }

    public boolean isInBounds(ChessPosition position) {
        return position.getColumn() >= 1 &&
                position.getRow() >= 1 &&
                position.getRow() <= 8 &&
                position.getColumn() <= 8;
    }

    public boolean isSpaceEmpty(ChessBoard board, ChessPosition position) {
        return board.getPiece(position) == null;
    }

    public boolean isSpaceEnemy(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        if (board.getPiece(position) == null) return false;
        return board.getPiece(position).getTeamColor() != color;
    }

}

