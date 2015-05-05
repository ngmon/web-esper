package cz.muni.fgdovin.bachelorthesis.support;

/**
 * @author Filip Gdovin
 * @version 5. 5. 2015
 */
public class EventProperty {

    private String key;
    private Object value;

    public EventProperty() {
    }

    public EventProperty(String key, Object value) {
        this.key = key;
        setValue(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        switch (String.valueOf(value)) {
            case "Long" : {
                this.value = Long.class;
                break;
            }
            case "Integer" : {
                this.value = Integer.class;
                break;
            }
            case "Boolean" : {
                this.value = Boolean.class;
                break;
            }
            default: {
                this.value = String.class;
            }
        }
    }

    @Override
    public String toString() {
        int lastComma = value.toString().lastIndexOf(".");
        String classType = value.toString().substring(lastComma + 1);
        return this.key + " " + classType;
    }
}
