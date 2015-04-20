package cz.muni.fgdovin.bachelorthesis.support.EsperModifiedClasses;/*
 * *************************************************************************************
 *  Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 *  http://esper.codehaus.org                                                          *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.util.EventPropertyRenderer;
import com.espertech.esper.client.util.EventPropertyRendererContext;
import com.espertech.esper.client.util.JSONEventRenderer;
import com.espertech.esper.client.util.JSONRenderingOptions;
import com.espertech.esper.event.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Render for the JSON format.
 * Modified by Filip Gdovin on 18. 4. 2015.
 */
public class CustomJSONRenderer implements JSONEventRenderer {

    private static final Log log = LogFactory.getLog(CustomJSONRenderer.class);

    private final static String COMMA_DELIMITER = ",";

    private final RendererMeta meta;
    private final RendererMetaOptions rendererOptions;

    /**
     * Ctor.
     * @param eventType type of event(s)
     * @param options rendering options
     */
    public CustomJSONRenderer(EventType eventType, JSONRenderingOptions options)
    {
        EventPropertyRenderer propertyRenderer = null;
        EventPropertyRendererContext propertyRendererContext = null;
        if (options.getRenderer() != null) {
            propertyRenderer = options.getRenderer();
            propertyRendererContext = new EventPropertyRendererContext(eventType, true);
        }

        rendererOptions = new RendererMetaOptions(options.isPreventLooping(), false, propertyRenderer, propertyRendererContext);
        meta = new RendererMeta(eventType, new Stack<>(), rendererOptions);
    }

    public String render(String title, EventBean theEvent)
    {
        return render(theEvent);
    }

    public String render(EventBean theEvent) {
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        recursiveRender(theEvent, buf, meta, rendererOptions);
        buf.append('}');
        return buf.toString();
    }

    private static void recursiveRender(EventBean theEvent, StringBuilder buf, RendererMeta meta, RendererMetaOptions rendererOptions)
    {
        String delimiter = "";

        // simple properties
        GetterPair[] simpleProps = meta.getSimpleProperties();
        if (rendererOptions.getRenderer() == null) {
            for (GetterPair simpleProp : simpleProps)
            {
                Object value = simpleProp.getGetter().get(theEvent);
                writeDelimitedIndentedProp(buf, delimiter, simpleProp.getName());
                simpleProp.getOutput().render(value, buf);
                delimiter = COMMA_DELIMITER;
            }
        }
        else {
            EventPropertyRendererContext context = rendererOptions.getRendererContext();
            context.setStringBuilderAndReset(buf);
            for (GetterPair simpleProp : simpleProps)
            {
                Object value = simpleProp.getGetter().get(theEvent);
                writeDelimitedIndentedProp(buf, delimiter, simpleProp.getName());
                context.setDefaultRenderer(simpleProp.getOutput());
                context.setPropertyName(simpleProp.getName());
                context.setPropertyValue(value);
                rendererOptions.getRenderer().render(context);
                delimiter = COMMA_DELIMITER;
            }
        }

        GetterPair[] indexProps = meta.getIndexProperties();
        for (GetterPair indexProp : indexProps)
        {
            Object value = indexProp.getGetter().get(theEvent);
            writeDelimitedIndentedProp(buf, delimiter, indexProp.getName());

            if (value == null)
            {
                buf.append("null");
            }
            else
            {
                if (!value.getClass().isArray())
                {
                    buf.append("[]");
                }
                else
                {
                    buf.append('[');
                    String arrayDelimiter = "";

                    if (rendererOptions.getRenderer() == null) {
                        for (int i = 0; i < Array.getLength(value); i++)
                        {
                            Object arrayItem = Array.get(value, i);
                            buf.append(arrayDelimiter);
                            indexProp.getOutput().render(arrayItem, buf);
                            arrayDelimiter = ", ";
                        }
                    }
                    else {
                        EventPropertyRendererContext context = rendererOptions.getRendererContext();
                        context.setStringBuilderAndReset(buf);
                        for (int i = 0; i < Array.getLength(value); i++)
                        {
                            Object arrayItem = Array.get(value, i);
                            buf.append(arrayDelimiter);
                            context.setPropertyName(indexProp.getName());
                            context.setPropertyValue(arrayItem);
                            context.setIndexedPropertyIndex(i);
                            context.setDefaultRenderer(indexProp.getOutput());
                            rendererOptions.getRenderer().render(context);
                            arrayDelimiter = ", ";
                        }
                    }
                    buf.append(']');
                }
            }
            delimiter = COMMA_DELIMITER;
        }

        GetterPair[] mappedProps = meta.getMappedProperties();
        for (GetterPair mappedProp : mappedProps)
        {
            Object value = mappedProp.getGetter().get(theEvent);

            if ((value != null) && (!(value instanceof Map)))
            {
                log.warn("Property '" + mappedProp.getName() + "' expected to return Map and returned " + value.getClass() + " instead");
                continue;
            }

            writeDelimitedIndentedProp(buf, delimiter, mappedProp.getName());

            if (value == null)
            {
                buf.append("null");
            }
            else
            {
                Map<String, Object> map = (Map<String, Object>) value;
                if (map.isEmpty())
                {
                    buf.append("{}");
                }
                else
                {
                    buf.append('{');

                    String localDelimiter = "";
                    Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                    for (;it.hasNext();)
                    {
                        Map.Entry<String, Object> entry = it.next();
                        if (entry.getKey() == null)
                        {
                            continue;
                        }

                        buf.append(localDelimiter);
                        buf.append('\"');
                        buf.append(entry.getKey());
                        buf.append("\": ");

                        if (entry.getValue() == null)
                        {
                            buf.append("null");
                        }
                        else
                        {
                            ValueRenderer outRenderer = ValueRendererFactory.getValueRenderer(entry.getValue().getClass());
                            if (rendererOptions.getRenderer() == null) {
                                outRenderer.render(entry.getValue(), buf);
                            }
                            else {
                                EventPropertyRendererContext context = rendererOptions.getRendererContext();
                                context.setStringBuilderAndReset(buf);
                                context.setPropertyName(mappedProp.getName());
                                context.setPropertyValue(entry.getValue());
                                context.setMappedPropertyKey(entry.getKey());
                                context.setDefaultRenderer((OutputValueRenderer)outRenderer);
                                rendererOptions.getRenderer().render(context);
                            }
                        }
                        localDelimiter = COMMA_DELIMITER;
                    }
                    buf.append('}');
                }
            }

            delimiter = COMMA_DELIMITER;
        }

        NestedGetterPair[] nestedProps = meta.getNestedProperties();
        for (NestedGetterPair nestedProp : nestedProps)
        {
            Object value = nestedProp.getGetter().getFragment(theEvent);

            writeDelimitedIndentedProp(buf, delimiter, nestedProp.getName());

            if (value == null)
            {
                buf.append("null");
            }
            else if (!nestedProp.isArray())
            {
                if (!(value instanceof EventBean))
                {
                    log.warn("Property '" + nestedProp.getName() + "' expected to return EventBean and returned " + value.getClass() + " instead");
                    buf.append("null");
                    continue;
                }
                EventBean nestedEventBean = (EventBean) value;
                buf.append('{');
                recursiveRender(nestedEventBean, buf, nestedProp.getMetadata(), rendererOptions);
                buf.append('}');
            }
            else
            {
                if (!(value instanceof EventBean[]))
                {
                    log.warn("Property '" + nestedProp.getName() + "' expected to return EventBean[] and returned " + value.getClass() + " instead");
                    buf.append("null");
                    continue;
                }


                StringBuilder arrayDelimiterBuf = new StringBuilder();
                arrayDelimiterBuf.append(',');

                EventBean[] nestedEventArray = (EventBean[]) value;
                String arrayDelimiter = "";
                buf.append('[');

                for (EventBean arrayItem : nestedEventArray) {
                    buf.append(arrayDelimiter);
                    arrayDelimiter = arrayDelimiterBuf.toString();

                    buf.append('{');
                    recursiveRender(arrayItem, buf, nestedProp.getMetadata(), rendererOptions);
                    buf.append('}');
                }
                buf.append(']');
            }
            delimiter = COMMA_DELIMITER;
        }
    }

    private static void writeDelimitedIndentedProp(StringBuilder buf, String delimiter, String name) {
        buf.append(delimiter);
        buf.append('\"');
        buf.append(name);
        buf.append("\": ");
    }
}

