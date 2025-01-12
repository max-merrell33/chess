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
            addPawnMoves(myPosition, oneSquareForward);
        }

        // if the pawn has not moved, check two squares ahead
        if (hasNotMoved && isSpaceEmpty(board, oneSquareForward)) {
            ChessPosition twoSquaresForward = new ChessPosition(newRow+direction, col);
            if (isSpaceEmpty(board, twoSquaresForward)) {
                addPawnMoves(myPosition, twoSquaresForward);
            }
        }

        // if the two squares diagonal and forward contain an enemy, they are valid moves
        ChessPosition diagonalSquare = new ChessPosition(newRow, col+1);
        if (isInBounds(diagonalSquare) && isSpaceEnemy(board, diagonalSquare, pawnColor)) {
            addPawnMoves(myPosition, diagonalSquare);
        }

        diagonalSquare = new ChessPosition(newRow, col-1);
        if (isInBounds(diagonalSquare) && isSpaceEnemy(board, diagonalSquare, pawnColor)) {
            addPawnMoves(myPosition, diagonalSquare);
        }

        return moves;
    }

    public void addPawnMoves(ChessPosition start, ChessPosition end) {
        if (end.getRow() == 1 || end.getRow() == 8) {
            for (ChessPiece.PieceType piece : promotionPieces) {
                moves.add(new ChessMove(start, end, piece));
            }
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }
}
