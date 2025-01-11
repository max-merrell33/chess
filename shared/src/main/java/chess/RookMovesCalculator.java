package chess;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // the four directions a bishop can move
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        // color of the bishop being moved
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        // loops through all the directions the bishop can move
        for (int[] direction : directions) {
            int row = myPosition.getRow() + direction[0];
            int col = myPosition.getColumn() + direction[1];
            while (isValidMove(board, new ChessPosition(row, col), color)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));

                if (isSpaceEnemy(board, new ChessPosition(row, col), color)) break;

                row += direction[0];
                col += direction[1];
            }
        }

        return moves;
    }

}
