package chess;

import java.util.ArrayList;
import java.util.Collection;

public class SpecialMoves {

    /**
     * Gets the valid castling moves for a given position
     *
     * @param game the ChessGame
     * @param position the position being checked for valid castling moves
     * @return Set of valid castling moves for the requested piece
     */
    public Collection<ChessMove> getCastlingMoves(ChessGame game, ChessPosition position) {
        ChessPiece piece = game.getBoard().getPiece(position);
        ChessPiece.PieceType type = piece.getPieceType();

        // check that the piece is a king, has not moved, and is not in check
        if (type != ChessPiece.PieceType.KING) { return null; }
        if (piece.hasMoved()) { return null; }
        if (position.getColumn() != 5) { return null; }
        if (game.isInCheck(piece.getTeamColor())) { return null; }

        Collection<ChessMove> moves = new ArrayList<>();

        int baseRow = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : 8;
        if (position.getRow() != baseRow) { return null;}

        // Check Queen side
        if (isSideValid(game, baseRow, true)) {
            ChessPosition endPosition = new ChessPosition(baseRow, position.getColumn() - 2);
            moves.add(new ChessMove(position, endPosition, null));
        }

        // Check King side
        if (isSideValid(game, baseRow, false)) {
            ChessPosition endPosition = new ChessPosition(baseRow, position.getColumn() + 2);
            moves.add(new ChessMove(position, endPosition, null));
        }

        return moves;
    }

    /**
     * Determines if a king can castle
     *
     * @param game the ChessGame
     * @param row the row where the king is found
     * @param isQueenSide true if queen side castling is being checked, false if king side
     * @return Whether the king can castle to the given side
     */
    public boolean isSideValid(ChessGame game, int row, boolean isQueenSide) {
        ChessBoard board = game.getBoard();
        int[] verifyEmptyColNums; // The columns between the rook and king, need to be empty
        int[] verifyCheckColNums; // The columns that the king travels on, the king cannot be in check in any
        int rookColNum;

        if (isQueenSide) {
            verifyEmptyColNums = new int[]{ 4, 3, 2 };
            verifyCheckColNums = new int[]{ 4, 3 };
            rookColNum = 1;

        } else {
            verifyEmptyColNums = new int[]{ 6, 7 };
            verifyCheckColNums = new int[]{ 6, 7 };
            rookColNum = 8;
        }

        //Verify that the spaces between king and rook are empty
        for (int col : verifyEmptyColNums) {
            ChessPosition newPosition = new ChessPosition(row, col);
            if ( board.getPiece(newPosition) != null ) { return false; }
        }

        //Verify that the spaces that the king moves to do not create check
        for (int col : verifyCheckColNums) {
            ChessPosition startPosition = new ChessPosition(row, 5);
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(startPosition, newPosition, null);
            if ( game.moveCreatesCheck(move) ) { return false; }
        }

        //Verify that the piece in the corner is a rook and that it has not moved
        ChessPiece rook = board.getPiece(new ChessPosition(row, rookColNum));
        if (rook == null) { return false; }
        if (rook.getPieceType() != ChessPiece.PieceType.ROOK) { return false; }
        return !rook.hasMoved();
    }


    /**
     * Determines if a pawn can en passant
     *
     * @param game the ChessGame
     * @param position the position of the pawn that is capturing
     * @param lastDoubleMovePosition the position on the board of the pawn that double moved last turn,
     *                               null if a pawn did not double move last turn
     * @return The valid En Passant moves that the pawn can perform
     */
    public Collection<ChessMove> getEnPassantMoves(ChessGame game, ChessPosition position, ChessPosition lastDoubleMovePosition) {
        ChessPiece piece = game.getBoard().getPiece(position);
        ChessPiece.PieceType type = piece.getPieceType();

        int direction = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        Collection<ChessMove> moves = new ArrayList<>();

        if (type != ChessPiece.PieceType.PAWN) { return null; }
        ChessPosition leftSquare = new ChessPosition(position.getRow(), position.getColumn() - 1);
        ChessPosition rightSquare = new ChessPosition(position.getRow(), position.getColumn() + 1);

        if (leftSquare.equals(lastDoubleMovePosition)) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + direction, position.getColumn() - 1);
            moves.add(new ChessMove(position, endPosition, null));
        }
        if (rightSquare.equals(lastDoubleMovePosition)) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + direction, position.getColumn() + 1);
            moves.add(new ChessMove(position, endPosition, null));
        }

        return moves;
    }
}
