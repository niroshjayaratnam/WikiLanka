/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;


import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Udara
 */
public class GeocodeResult {
    protected GeocoderStatus status;
    protected List<GeocoderResult> results;

    public GeocodeResult() {
    }

    public GeocoderStatus getStatus() {
        return status;
    }

    public void setStatus(GeocoderStatus status) {
        this.status = status;
    }

    public List<GeocoderResult> getResults() {
        return results;
    }

    public void setResults(List<GeocoderResult> result) {
        this.results = result;
    }
}
enum GeocoderResultType {

    STREET_ADDRESS("street_address"),
    ROUTE("route"),
    INTERSECTION("intersection"),
    POLITICAL("political"),
    COUNTRY("country"),
    ADMINISTRATIVE_AREA_LEVEL_1("administrative_area_level_1"),
    ADMINISTRATIVE_AREA_LEVEL_2("administrative_area_level_2"),
    ADMINISTRATIVE_AREA_LEVEL_3("administrative_area_level_3"),
    COLLOQUIAL_AREA("colloquial_area"),
    LOCALITY("locality"),
    SUBLOCALITY("sublocality"),
    NEIGHBORHOOD("neighborhood"),
    PREMISE("premise"),
    SUBPREMISE("subpremise"),
    POSTAL_CODE("postal_code"),
    NATURAL_FEATURE("natural_feature"),
    AIRPORT("airport"),
    PARK("park"),
    POINT_OF_INTEREST("point_of_interest"),
    POST_BOX("post_box"),
    STREET_NUMBER("street_number"),
    FLOOR("floor"),
    ROOM("room");

    private final String value;

    GeocoderResultType(final String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeocoderResultType fromValue(final String v) {
        for (GeocoderResultType c : GeocoderResultType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
enum GeocoderLocationType {
    APPROXIMATE,
    GEOMETRIC_CENTER,
    RANGE_INTERPOLATED,
    ROOFTOP;

    public String value() {
        return name();
    }

    public static GeocoderLocationType fromValue(String v) {
        return valueOf(v);
    }
}
enum GeocoderStatus {

    ERROR,
    INVALID_REQUEST,
    OK,
    OVER_QUERY_LIMIT,
    REQUEST_DENIED,
    UNKNOWN_ERROR,
    ZERO_RESULTS;

    public String value() {
        return name();
    }

    public static GeocoderStatus fromValue(String v) {
        return valueOf(v);
    }

}
class GeocoderAddressComponent {
    protected String long_name;
    protected String short_name;
    protected List<String> types;

    public String getLongName() {
        return long_name;
    }

    public void setLongName(String longName) {
        this.long_name = longName;
    }

    public String getShortName() {
        return short_name;
    }

    public void setShortName(String shortName) {
        this.short_name = shortName;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}

class GeocoderGeometry {
    protected LatLng location;
    protected GeocoderLocationType location_type;
    protected LatLngBounds viewport;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public GeocoderLocationType getLocationType() {
        return location_type;
    }

    public void setLocationType(GeocoderLocationType locationType) {
        this.location_type = locationType;
    }

    public LatLngBounds getViewport() {
        return viewport;
    }

    public void setViewport(LatLngBounds viewport) {
        this.viewport = viewport;
    }
}
class LatLng {
    private BigDecimal lat, lng;

    public LatLng() {
    }

    public LatLng(BigDecimal lat, BigDecimal lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }
}

class LatLngBounds {
    private LatLng southwest, northeast;

    public LatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }

    public LatLng getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }
class GeocoderRequest {
    private String address;         //Address. Optional.
    private LatLngBounds bounds;    //LatLngBounds within which to search. Optional.
    private String language;        //Preferred language for results. Optional.
    private LatLng location;        //LatLng about which to search. Optional.
    private String region;          //Country code top-level domain within which to search. Optional.

    public GeocoderRequest() {
    }

    public GeocoderRequest(String address) {
        this.address = address;
    }

    public GeocoderRequest(String address, String language) {
        this.address = address;
        this.language = language;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GeocoderRequest");
        sb.append("{address='").append(address).append('\'');
        sb.append(", bounds=").append(bounds);
        sb.append(", language='").append(language).append('\'');
        sb.append(", location=").append(location);
        sb.append(", region='").append(region).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeocoderRequest that = (GeocoderRequest) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (bounds != null ? !bounds.equals(that.bounds) : that.bounds != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        return result;
    }
}
}