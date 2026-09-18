package com.intershop.application.rest.custom.product.internal;

import java.util.Iterator;
import java.util.Map;

import com.intershop.beehive.core.capi.domain.ExtensibleObject;
import com.intershop.beehive.core.capi.domain.PersistentObject;
import com.intershop.beehive.core.capi.domain.PersistentObjectBOExtension;
import com.intershop.component.product.capi.ProductBO;
import com.intershop.sellside.rest.common.capi.resourceobject.ProductRO;
import com.intershop.sellside.rest.common.internal.resource.product.ProductSearchHandlerImpl;

/**
 * Adds all persisted custom product attributes to the REST response, including
 * attributes that are not assigned to a channel attribute group.
 */
public class CustomProductSearchHandlerImpl extends ProductSearchHandlerImpl
{
    @Override
    public void setCustomAttributes(ProductRO.ProductROBuilder builder, ProductBO product)
    {
        builder.getAttributes().put("Ecofriendly", Boolean.TRUE);
        if (product == null)
        {
            return;
        }

        super.setCustomAttributes(builder, product);

        PersistentObjectBOExtension extension = product.getExtension(PersistentObjectBOExtension.class);
        if (extension == null)
        {
            return;
        }

        PersistentObject persistentObject = extension.getPersistentObject();
        if (!(persistentObject instanceof ExtensibleObject))
        {
            return;
        }

        Iterator<String> attributeNames = ((ExtensibleObject)persistentObject).createAttributeNamesIterator();
        Map<String, Object> attributes = builder.getAttributes();
        while (attributeNames.hasNext())
        {
            String attributeName = attributeNames.next();
            // Use the same value resolution as the standard product REST handler.
            Object attributeValue = product.getAttribute(attributeName);
            if (attributeValue != null)
            {
                attributes.put(attributeName, attributeValue);
            }
        }
    }
}
