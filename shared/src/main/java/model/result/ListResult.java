package model.result;

import model.GameDataTX;

import java.util.Collection;

public class ListResult extends Result {
    public Collection<GameDataTX> games;

    public ListResult(Collection<GameDataTX> games) {
        this.games = games;
    }
}
