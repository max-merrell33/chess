package chess;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // the eight directions a knight can move
        int[][] directions = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

        return pieceMovesHelper(board, myPosition, directions, true);
    }
}
