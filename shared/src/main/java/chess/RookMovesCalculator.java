package chess;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // the four directions a rook can move
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        return pieceMovesHelper(board, myPosition, directions, false);
    }

}
