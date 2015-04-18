package cz.muni.fgdovin.bachelorthesis.support.EsperModifiedClasses;

/**
 * Renderer for a Object values that can simply be output via to-string.
 * Created by Filip Gdovin on 18. 4. 2015.
 */
public class ValueRendererBase implements ValueRenderer
{
    public void render(Object object, StringBuilder buf)
    {
        if (object == null)
        {
            buf.append("null");
            return;
        }
        buf.append(object.toString());
    }
}
