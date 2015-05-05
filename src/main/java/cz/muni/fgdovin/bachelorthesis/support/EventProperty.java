package cz.muni.fgdovin.bachelorthesis.support;

/**
 * Created by Filip Gdovin on 5. 5. 2015.
 */
public class EventProperty {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.key + " " + this.value;
    }
}
