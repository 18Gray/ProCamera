package com.eighteengray.procamera.model.database;

public class VersionFactory
{
    public static Upgrade getUpgrade(int i) {
        Upgrade upgrade = null;
        switch (i) {
            case 2:
                upgrade = new VersionSecond();
                break;
            case 3:
                upgrade = new VersionThird();
                break;
            case 4:
                upgrade = new VersionFourth();
                break;
        }
        return upgrade;
    }
}
