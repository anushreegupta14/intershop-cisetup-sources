package tests.com.intershop.application.rest.custom.user.internal.modules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.application.rest.custom.user.internal.modules.CustomUserMapperModule;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomUserMapperModuleTest
{
    private static final String OBJECTGRAPH_RESOURCE = "resources/app_sf_rest_custom/objectgraph/objectgraph.properties";

    @Test
    public void bindsExtensionIntoPlatformExtensionSet()
    {
        Set<FunctionExtension<UserBO, SMBCustomerUserRO>> extensions = Guice.createInjector(new CustomUserMapperModule())
                        .getInstance(Key.get(new TypeLiteral<Set<FunctionExtension<UserBO, SMBCustomerUserRO>>>() { }));

        assertThat(extensions, hasSize(1));
        assertThat(extensions.iterator().next(), is(instanceOf(CustomSMBCustomerUserROFunctionExtension.class)));
    }

    @Test
    public void registersModuleInObjectGraphProperties() throws Exception
    {
        String modules = loadGlobalModules();

        assertThat(modules, containsString(CustomUserMapperModule.class.getName()));
        assertThat(modules, not(containsString(",")));
    }

    @Test
    public void allRegisteredModulesAreLoadableGuiceModules() throws Exception
    {
        for (String className : loadGlobalModules().trim().split("\\s+"))
        {
            assertThat(className, Module.class.isAssignableFrom(Class.forName(className)), is(true));
        }
    }

    private String loadGlobalModules() throws Exception
    {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(OBJECTGRAPH_RESOURCE))
        {
            assertThat(OBJECTGRAPH_RESOURCE, in, is(notNullValue()));
            Properties properties = new Properties();
            properties.load(in);
            String modules = properties.getProperty("global.modules");
            assertThat("global.modules", modules, is(notNullValue()));
            return modules;
        }
    }
}
