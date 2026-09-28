package tests.com.intershop.application.rest.custom.customer.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import java.io.InputStream;
import java.util.Properties;
import org.junit.Test;
import com.google.inject.Module;

public class MapperRegistrationTest
{
    @Test
    public void packagedConfigurationContainsLoadableWhitespaceSeparatedModules() throws Exception
    {
        Properties properties = new Properties();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(
            "resources/app_sf_rest_custom/objectgraph/objectgraph.properties"))
        {
            assertNotNull("Object graph resource must have the runtime resources prefix", stream);
            properties.load(stream);
        }
        String[] modules = properties.getProperty("global.modules").trim().split("\\s+");
        assertEquals(2, modules.length);
        for (String module : modules)
        {
            assertNotNull(Class.forName(module).asSubclass(Module.class).newInstance());
        }
    }
}
