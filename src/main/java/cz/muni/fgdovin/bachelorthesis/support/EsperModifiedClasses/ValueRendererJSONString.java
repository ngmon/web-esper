package cz.muni.fgdovin.bachelorthesis.support.EsperModifiedClasses;

/**
 * Renderer for a String-value into JSON strings.
 * Created by Filip Gdovin on 18. 4. 2015.
 */
public class ValueRendererJSONString implements ValueRenderer
{
    public void render(Object object, StringBuilder buf)
    {
        if (object == null)
        {
            buf.append("null");
            return;
        }

        enquote(object.toString(), buf);
    }

    /**
     * JSON-Enquote the passed string.
     * @param s string to enqoute
     * @param sb buffer to populate
     */
    public static void enquote(String s, StringBuilder sb)
    {
        if (s == null || s.length() == 0)
        {
            sb.append("\"\"");
            return;
        }

        char c;
        int i;
        int len = s.length();
        String t;

        sb.append('"');
        for (i = 0; i < len; i += 1)
        {
            c = s.charAt(i);
            if ((c == '\\') || (c == '"'))
            {
                sb.append(c);
            }
            else
            {
                if (c < ' ')
                {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u");
                    sb.append(t.substring(t.length() - 4));
                }
                else
                {
                    sb.append(c);
                }
            }
        }
        sb.append('"');
    }
}