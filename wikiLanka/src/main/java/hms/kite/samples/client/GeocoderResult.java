/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import java.util.List;

/**
 *
 * @author Udara
 */
public class GeocoderResult {
    protected List<String> types;
    protected String formatted_address;
    protected List<GeocoderAddressComponent> address_components;
    protected GeocoderGeometry geometry;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formatted_address = formattedAddress;
    }

    public List<GeocoderAddressComponent> getAddressComponents() {
        return address_components;
    }

    public void setAddressComponents(List<GeocoderAddressComponent> addressComponents) {
        this.address_components = addressComponents;
    }

    public GeocoderGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeocoderGeometry geometry) {
        this.geometry = geometry;
    }
}