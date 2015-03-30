package cz.muni.fgdovin.bachelorthesis.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Filip Gdovin on 30. 3. 2015.
 */
public class PropLoader {

    public static void loadFromPropFile(Properties prop) throws IOException {
        InputStream in = PropLoader.class.getClassLoader().getResourceAsStream("config.properties");
        prop.load(in);
        in.close();
    }

}
