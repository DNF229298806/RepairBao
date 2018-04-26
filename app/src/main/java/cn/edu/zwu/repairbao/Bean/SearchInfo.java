package cn.edu.zwu.repairbao.Bean;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/25
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class SearchInfo {
    private String name;
    private double lat;
    private double lng;
    private String address;
    private String streetIds;
    private String uids;

    public SearchInfo(String name, double lat, double lng, String address, String streetIds, String uids) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.streetIds = streetIds;
        this.uids = uids;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public String getStreetIds() {
        return streetIds;
    }

    public String getUids() {
        return uids;
    }
}
