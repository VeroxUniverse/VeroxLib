package net.veroxuniverse.veroxlib.client;

public class ClientSanityData {
    private static float clientSanity = 100f;
    private static boolean isCultist = false; // Neu

    public static void setClientData(float sanity, boolean cultist) {
        clientSanity = sanity;
        isCultist = cultist;
    }

    public static float getSanity() {
        return clientSanity;
    }

    public static boolean isCultist() {
        return isCultist;
    }
}