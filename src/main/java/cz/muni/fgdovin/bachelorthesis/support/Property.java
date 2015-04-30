package cz.muni.fgdovin.bachelorthesis.support;

/**
 * @author Filip Gdovin
 * @version 30. 4. 2015
 */
public class Property {
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
