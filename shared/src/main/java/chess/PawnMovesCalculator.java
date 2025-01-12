package chess;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    ChessPiece.PieceType[] promotionPieces = {
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.QUEEN
    };

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessGame.TeamColor pawnColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int direction;
        boolean hasNotMoved;

        // white pawn = 1 is "forward", black pawn = -1 is "forward"
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            direction = 1;
            hasNotMoved = myPosition.getRow() == 2;
        } else {
            direction = -1;
            hasNotMoved = myPosition.getRow() == 7;
        }
        int newRow = row + direction;

        // if the square directly ahead is empty, the pawn can move forward
        ChessPosition oneSquareForward = new ChessPosition(newRow, col);
        if (isSpaceEmpty(board, oneSquareForward)) {
            addPawnMove(myPosition, oneSquareForward);
            // check two squares ahead if the pawn has not moved all game
            if (hasNotMoved) {
                ChessPosition twoSquaresForward = new ChessPosition(newRow+direction, col);
                if (isSpaceEmpty(board, twoSquaresForward)) {
                    addPawnMove(myPosition, twoSquaresForward);
                }
            }
        }

        // if the two squares diagonal and forward contain an enemy, they are valid moves
        int[] diagonals = {-1, 1};
        for (int changeCol: diagonals) {
            ChessPosition diagonalSquare = new ChessPosition(newRow, col + changeCol);
            if (isInBounds(diagonalSquare) && isSpaceEnemy(board, diagonalSquare, pawnColor)) {
                addPawnMove(myPosition, diagonalSquare);
            }
        }

        return moves;
    }

    public void addPawnMove(ChessPosition start, ChessPosition end) {
        if (end.getRow() == 1 || end.getRow() == 8) {
            for (ChessPiece.PieceType piece : promotionPieces) {
                moves.add(new ChessMove(start, end, piece));
            }
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }
}
