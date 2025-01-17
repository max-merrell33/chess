package chess;

import java.util.ArrayList;
import java.util.Collection;

public class SpecialMoves {

    private final int whiteBaseRow = 1;
    private final int blackBaseRow = 8;

    public Collection<ChessMove> getCastlingMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        ChessPiece.PieceType type = piece.getPieceType();

        // check that the piece is a king and has not moved
        if (type != ChessPiece.PieceType.KING) { return null; }
        if (piece.hasMoved()) { return null; }

        Collection<ChessMove> moves = new ArrayList<>();

        int baseRow = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? whiteBaseRow : blackBaseRow;
        if (isQueenSideValid(board, position, baseRow)) {
            ChessPosition endPosition = new ChessPosition(baseRow, position.getColumn() - 2);
            moves.add(new ChessMove(position, endPosition, null));
        }

        if (isShortSideValid(board, position, baseRow)) {
            ChessPosition endPosition = new ChessPosition(baseRow, position.getColumn() + 2);
            moves.add(new ChessMove(position, endPosition, null));
        }

        return moves;
    }

    public boolean isQueenSideValid(ChessBoard board, ChessPosition position, int row) {
        for (int col = position.getColumn()-1; col > 1; col--) {
            ChessPosition newPosition = new ChessPosition(row, col);
            if ( board.getPiece(newPosition) != null ) {
                return false;
            }
        }
        ChessPiece rook = board.getPiece(new ChessPosition(row, 1));
        if (rook == null) { return false; }
        if (rook.getPieceType() != ChessPiece.PieceType.ROOK) { return false; }
        if (rook.hasMoved()) { return false; }

        return true;
    }

    public boolean isShortSideValid(ChessBoard board, ChessPosition position, int row) {
        for (int col = position.getColumn()+1; col < 8; col++) {
            ChessPosition newPosition = new ChessPosition(row, col);
            if ( board.getPiece(newPosition) != null ) {
                return false;
            }
        }

        ChessPiece rook = board.getPiece(new ChessPosition(row, 8));
        if (rook == null) { return false; }
        if (rook.getPieceType() != ChessPiece.PieceType.ROOK) { return false; }
        if (rook.hasMoved()) { return false; }

        return true;
    }
}
