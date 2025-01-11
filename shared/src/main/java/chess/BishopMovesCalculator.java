package chess;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // the four directions a bishop can move
        int[][] directions = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        return pieceMovesHelper(board, myPosition, directions, false);
    }

}
