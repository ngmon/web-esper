package cz.muni.fgdovin.bachelorthesis.support.EsperModifiedClasses;

/**
 * For rendering an output value returned by a property.
 * Created by Filip Gdovin on 18. 4. 2015.
 */
public interface ValueRenderer
{
    /**
     * Renders the value to the buffer.
     * @param object to render
     * @param buf buffer to populate
     */
    public void render(Object object, StringBuilder buf);
}