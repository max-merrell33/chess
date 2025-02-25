package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;
    private boolean isCastling = false;
    private boolean isEnPassant = false;
    private final int numColsMoved;
    private final int numRowsMoved;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;

        numColsMoved = Math.abs(startPosition.getColumn() - endPosition.getColumn());
        numRowsMoved = Math.abs(startPosition.getRow() - endPosition.getRow());
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition)
                && Objects.equals(endPosition, chessMove.endPosition)
                && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }


    @Override
    public String toString() {
        return "Move{" +
                "startPos=" + startPosition +
                ", endPos=" + endPosition +
                ", promotionPiece=" + promotionPiece +
                '}';
    }

    public boolean isCastling() {
        return isCastling;
    }

    public void setCastling(boolean isCastling) {
        this.isCastling = isCastling;
    }

    public int getNumColsMoved() {
        return numColsMoved;
    }

    public int getNumRowsMoved() {
        return numRowsMoved;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    public void setEnPassant(boolean enPassant) {
        isEnPassant = enPassant;
    }
}
