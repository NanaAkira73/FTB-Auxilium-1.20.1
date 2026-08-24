package dev.ftb.mods.ftbauxilium.auxilium;

public class SessionCollector {
    public boolean multiplayer;
    public boolean hardcore;
    public boolean lan;
    public String sessionId;
    public int packId;
    public int packVersionId;

    public SessionCollector(boolean multiplayer, boolean hardcore, boolean lan, String sessionId, int packId, int packVersionId) {
        this.multiplayer = multiplayer;
        this.hardcore = hardcore;
        this.lan = lan;
        this.sessionId = sessionId;
        this.packId = packId;
        this.packVersionId = packVersionId;
    }
}