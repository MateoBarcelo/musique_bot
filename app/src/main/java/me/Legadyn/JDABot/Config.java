package me.Legadyn.JDABot;

public class Config {

    public static String get(ConfigKeys key) {

        switch (key) {
            case TOKEN: return System.getenv().get("TOKEN");
            case PREFIX: return System.getenv().get("PREFIX");
            case CREADOR: return System.getenv().get("CREADOR");
            default: break;
        }
        return null;
    }

}
