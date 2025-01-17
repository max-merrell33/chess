package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] squares = new ChessPiece[8][8];

    private final ChessPiece.PieceType[] baseRowPieces = {
            null, // positions are indexed 1-8 so this null value takes up the 0 index
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KING,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK
    };

    public ChessBoard() {

    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getColumn()-1][position.getRow()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return squares[position.getColumn()-1][position.getRow()-1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int[] baseRows = {1,8};
        int[] pawnRows = {2,7};
        ChessGame.TeamColor[] teamColors = {ChessGame.TeamColor.WHITE, ChessGame.TeamColor.BLACK};

        for (int i = 0; i < 2; i++) {
            addRowOfPieces(baseRows[i], teamColors[i], false);
            addRowOfPieces(pawnRows[i], teamColors[i], true);
        }
    }

    /**
     * Adds a row of pieces, called in resetBoard
     *
     * @param row the number of the row where the pieces are being placed (1,2,7,8)
     * @param color the color of the pieces in the row
     * @param isPawnRow true if the row is a row of pawns, false if it is a base row
     */
    public void addRowOfPieces(int row, ChessGame.TeamColor color, boolean isPawnRow) {
        ChessPiece.PieceType[] pieces = new ChessPiece.PieceType[9];
        if (isPawnRow) {
            Arrays.fill(pieces, ChessPiece.PieceType.PAWN);
        } else {
            pieces = baseRowPieces;
        }

        for (int i = 1; i <= 8; i++) {
            ChessPosition position = new ChessPosition(row, i);
            ChessPiece piece = new ChessPiece(color, pieces[i]);
            addPiece(position, piece);
        }
    }

    /**
     * Gets the position of a team's king
     *
     * @param color the team color of the desired king
     * @return The position of a team's king
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j] == null) {continue;}
                if (squares[i][j].getPieceType() == ChessPiece.PieceType.KING && squares[i][j].getTeamColor() == color) {
                    return new ChessPosition(j+1, i+1);
                }
            }
        }
        return null;
    }


    /**
     * Gets the positions of every piece for a given team
     *
     * @param color the team of the desired pieces
     * @return the positions of every piece for a given team
     */
    public Collection<ChessPosition> getTeamPositions(ChessGame.TeamColor color) {
        Collection<ChessPosition> teamPositions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j] == null) {continue;}
                if (squares[i][j].getTeamColor() == color) {
                    teamPositions.add(new ChessPosition(j+1, i+1));
                }
            }
        }
        return teamPositions;
    }


    /**
     * Makes a move on the board. Does not check if the move is valid
     *
     * @param move the ChessMove to be made
     */
    public void makeMove(ChessMove move) {
        ChessPiece movePiece = getPiece(move.getStartPosition());

        if ( move.getPromotionPiece() != null ) {
            movePiece = new ChessPiece(movePiece.getTeamColor(), move.getPromotionPiece());
        }

        addPiece(move.getStartPosition(), null);
        addPiece(move.getEndPosition(), movePiece);
        System.out.println(move.isCastling());
        if (move.isCastling()) {
            int row = move.getStartPosition().getRow();
            int rookStartColumn = move.getEndPosition().getColumn() == 3 ? 1 : 8;
            int rookEndColumn = move.getEndPosition().getColumn() == 3 ? 4 : 6;

            ChessPiece rook = new ChessPiece(movePiece.getTeamColor(), ChessPiece.PieceType.ROOK);

            addPiece(new ChessPosition(row, rookStartColumn), null);
            addPiece(new ChessPosition(row, rookEndColumn), rook);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                sb.append(squares[j][i] == null ? "_" : squares[j][i].toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
