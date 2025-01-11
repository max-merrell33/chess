package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}}; // the four directions a bishop can move
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        for (int[] direction : directions) {
            int row = myPosition.getRow() + direction[0];
            int col = myPosition.getColumn() + direction[1];
            while (isValidMove(board, new ChessPosition(row, col), color)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                if (isSpaceEnemy(board, new ChessPosition(row, col), color)) {break;}
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
