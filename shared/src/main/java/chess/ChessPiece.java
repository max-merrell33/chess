package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    private static final Map<PieceType, String> TYPE_TO_CHAR_MAP = Map.of(
            ChessPiece.PieceType.PAWN, "p",
            ChessPiece.PieceType.KNIGHT, "n",
            ChessPiece.PieceType.ROOK, "r",
            ChessPiece.PieceType.QUEEN, "q",
            ChessPiece.PieceType.KING, "k",
            ChessPiece.PieceType.BISHOP, "b");
    
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            return TYPE_TO_CHAR_MAP.get(type).toUpperCase();
        } else {
            return TYPE_TO_CHAR_MAP.get(type).toLowerCase();
        }
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        switch (board.getPiece(myPosition).getPieceType()) {
            case KING:
                KingMovesCalculator kingMovesCalculator = new KingMovesCalculator();
                moves = kingMovesCalculator.pieceMoves(board, myPosition);
                break;
            case QUEEN:
                QueenMovesCalculator queenMovesCalculator = new QueenMovesCalculator();
                moves = queenMovesCalculator.pieceMoves(board, myPosition);
                break;
            case BISHOP:
                BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator();
                moves = bishopMovesCalculator.pieceMoves(board, myPosition);
                break;
            case KNIGHT:
                KnightMovesCalculator knightMovesCalculator = new KnightMovesCalculator();
                moves = knightMovesCalculator.pieceMoves(board, myPosition);
                break;
            case ROOK:
                RookMovesCalculator rookMovesCalculator = new RookMovesCalculator();
                moves = rookMovesCalculator.pieceMoves(board, myPosition);
                break;
            case PAWN:
                PawnMovesCalculator pawnMovesCalculator = new PawnMovesCalculator();
                moves = pawnMovesCalculator.pieceMoves(board, myPosition);
                break;
            default:
        }
        return moves;
    }
}
