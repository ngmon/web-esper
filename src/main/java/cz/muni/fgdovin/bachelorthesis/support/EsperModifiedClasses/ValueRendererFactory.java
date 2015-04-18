package cz.muni.fgdovin.bachelorthesis.support.EsperModifiedClasses;

import com.espertech.esper.util.JavaClassHelper;

/**
 * For rendering an output value returned by a property.
 * Modified by Filip Gdovin on 18. 4. 2015.
 */
public class ValueRendererFactory {

    private static ValueRenderer jsonStringOutput = new ValueRendererJSONString();
    private static ValueRenderer baseOutput = new ValueRendererBase();

    /**
     * Returns a renderer for an output value.
     * @param type to render
     * @return renderer
     */
    protected static ValueRenderer getValueRenderer(Class type)
    {
        if (type.isArray())
        {
            type = type.getComponentType();
        }
        if (type == String.class ||
                type == Character.class ||
                type == char.class ||
                type.isEnum() ||
                (!JavaClassHelper.isNumeric(type) && JavaClassHelper.getBoxedType(type) != Boolean.class))
        {
            return jsonStringOutput;
        }
        else
        {
            return baseOutput;
        }
    }
}
