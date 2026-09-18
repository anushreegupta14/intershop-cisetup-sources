package com.intershop.application.rest.custom.customer.internal;

import com.intershop.beehive.app.capi.AppContext;
import com.intershop.beehive.businessobject.capi.BusinessObjectAttribute;
import com.intershop.beehive.businessobject.capi.BusinessObjectAttributes;
import com.intershop.beehive.core.capi.app.AppContextUtil;
import com.intershop.beehive.core.capi.domain.AbstractPersistentObjectBO;
import com.intershop.beehive.core.capi.domain.Extensible;
import com.intershop.beehive.core.capi.domain.PersistentObject;
import com.intershop.beehive.core.capi.request.Request;
import com.intershop.component.customer.capi.CustomerBO;
import com.intershop.component.customer.capi.CustomerSegmentBORepository;
import com.intershop.component.customer.capi.RepositoryBOCustomerSegmentExtension;
import com.intershop.component.repository.capi.BusinessObjectRepositoryContext;
import com.intershop.sellside.rest.common.capi.resourceobject.customer.CustomerRO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;

/**
 * Adds a customer custom attribute to the customer REST response as a custom field.
 */
abstract class CustomCustomerROFunctionExtension<T extends CustomerRO>
    implements FunctionExtension<CustomerBO, T>
{
    private static final String[] ATTRIBUTE_NAMES =
        { "HideInvoiceDocument" };

    @Override
    public boolean isApplicable(CustomerBO source, T target)
    {
        return source != null && target != null;
    }

    @Override
    public T apply(CustomerBO source, T target)
    {
        if (source == null || target == null)
        {
            return target;
        }

        for (String name : ATTRIBUTE_NAMES)
        {
            Object value = getAttributeValue(source, name);
            if (value != null)
            {
                target.addCustomField(name, value);
            }
        }

                return target;
    }

    private static Object getAttributeValue(CustomerBO customer, String name)
    {
        BusinessObjectAttributes attributes = customer.getExtension(BusinessObjectAttributes.class);
        if (attributes != null)
        {
            BusinessObjectAttribute<Object> attribute = attributes.getAttribute(name);
            Object value = attribute == null ? null : attribute.getValue();
            if (value != null)
            {
                return value;
            }
        }

        // The standard BO attributes extension reads BusinessObjectAttributes#<name>.
        // Legacy unprefixed customer attributes are not exposed by that extension.
        return getLegacyAttributeValue(customer, name);
    }

    protected CustomerSegmentBORepository getSegmentRepository()
    {
        // Resolve per request so a shared mapper never retains another application's repository.
        if (Request.getCurrent() == null)
        {
            return null;
        }
        AppContext appContext =
            AppContextUtil.getCurrentAppContext();
        BusinessObjectRepositoryContext repositories =
            appContext == null ? null : appContext.getVariable(
                BusinessObjectRepositoryContext.CURRENT);
        return repositories == null ? null : repositories.getRepository(
            RepositoryBOCustomerSegmentExtension.EXTENSION_ID);
    }

    private static Object getLegacyAttributeValue(CustomerBO customer, String name)
    {
        if (!(customer instanceof AbstractPersistentObjectBO))
        {
            return null;
        }
        PersistentObject po = ((AbstractPersistentObjectBO<?>) customer).getPersistentObject();
        if (!(po instanceof Extensible))
        {
            return null;
        }
        Extensible extensible = (Extensible) po;
        return extensible.containsAttribute(name) ? extensible.getAttribute(name) : null;
    }
}
