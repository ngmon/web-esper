package cz.muni.fgdovin.bachelorthesis.web;

/**
 * Model class to support dynamic tables of event properties,
 * which is needed as Spring couldn't handle it as Map.
 * List of Property is used in EventTypeModel
 * so Spring can fill it from user-filled form.
 *
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
