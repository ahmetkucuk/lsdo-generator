package app.utils;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public enum EventType {
    AR, CH, FL, SG, NONE;

    public EventType fromString(String s) {
        switch (s) {
            case "AR":
                return AR;
            case "CH":
                return CH;
            case "FL":
                return FL;
            case "SG":
                return SG;
        }
        return NONE;
    }

    public double getCDELT() {
        switch (this) {
            case AR:
                return Constants.CDELT.AR_CDELT;
            case CH:
                return Constants.CDELT.CH_CDELT;
            case FL:
                return Constants.CDELT.FL_CDELT;
            case SG:
                return Constants.CDELT.SG_CDELT;
            case NONE:
                return 0;
        }
        return 0;
    }
}
