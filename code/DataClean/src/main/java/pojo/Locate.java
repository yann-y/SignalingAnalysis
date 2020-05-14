package pojo;

public class Locate {


    private String longitude;

    private String latitude;

    private String laci;


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLaci() {
        return laci;
    }

    public void setLaci(String laci) {
        this.laci = laci;
    }

    @Override
    public String toString() {
        return "Locate{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", laci='" + laci + '\'' +
                '}';
    }
}
