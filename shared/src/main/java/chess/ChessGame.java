package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private boolean isWhiteTurn = true;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (isWhiteTurn) {
            return TeamColor.WHITE;
        } else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        isWhiteTurn = (team == TeamColor.WHITE);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }

        TeamColor pieceColor = board.getPiece(startPosition).getTeamColor();

        Collection<ChessMove> pieceMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> invalidMoves = new ArrayList<>();

        //loop through all the possible moves that piece can do and remove the invalid ones
        for (ChessMove move : pieceMoves) {
            //save the original pieces so the move can be undone
            ChessPiece startPiece = board.getPiece(move.getStartPosition());
            ChessPiece endPiece = board.getPiece(move.getEndPosition());

            board.makeMove(move);

            if (isInCheck(pieceColor)) {
                invalidMoves.add(move);
            }

            //undo the move
            board.addPiece(move.getStartPosition(), startPiece);
            board.addPiece(move.getEndPosition(), endPiece);
        }
        pieceMoves.removeAll(invalidMoves);

        Collection<ChessMove> castlingMoves = new SpecialMoves().getCastlingMoves(board, startPosition);

        System.out.println("Castling moves: " + castlingMoves);

        if (castlingMoves != null) { pieceMoves.addAll(castlingMoves); }
        return pieceMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());

        //Check that the move is valid
        Collection<ChessMove> validPieceMoves = validMoves(move.getStartPosition());
        if ( validPieceMoves == null ) { throw new InvalidMoveException(); }
        if ( !(validPieceMoves.contains(move)) ) { throw new InvalidMoveException(); }


        //check that it is the teams turn of the piece making the move
        TeamColor pieceColor = piece.getTeamColor();
        if ( getTeamTurn() != pieceColor ) { throw new InvalidMoveException(); }

        //set the hasMoved flag on the piece
        piece.setHasMoved();

        if (piece.getPieceType() == ChessPiece.PieceType.KING && move.getNumColsMoved() > 1) {
            move.setCastling(true);
        }

        //make the move and change whose turn it is
        board.makeMove(move);
        setTeamTurn(pieceColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);

        System.out.println(board);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyTeam = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        Collection<ChessPosition> enemyPositions = board.getTeamPositions(enemyTeam);

        for (ChessPosition position : enemyPositions) {
            Collection<ChessMove> enemyMoves = board.getPiece(position).pieceMoves(board, position);
            for (ChessMove move : enemyMoves) {
                if (move.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        if ( !(isInCheck(teamColor))) { return false; }

        Collection<ChessPosition> teamPositions = board.getTeamPositions(teamColor);
        for (ChessPosition position : teamPositions) {
            if ( !(validMoves(position).isEmpty()) ) { return false; }
        }

        return true;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) { return false; }

        Collection<ChessPosition> teamPositions = board.getTeamPositions(teamColor);
        for (ChessPosition position : teamPositions) {
            if ( !(validMoves(position).isEmpty()) ) { return false; }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
