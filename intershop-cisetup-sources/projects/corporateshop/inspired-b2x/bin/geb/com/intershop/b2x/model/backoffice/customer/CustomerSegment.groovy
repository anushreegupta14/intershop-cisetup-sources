package geb.com.intershop.b2x.model.backoffice.customer

import geb.com.intershop.b2x.model.storefront.responsive.NamedObject

/**
 * This class represents a Customer Segment.
 */
class CustomerSegment extends NamedObject
{
    String description

    /**
     * Constructor for CustomerSegment.
     */
    CustomerSegment(String id, String name)
    {
        super(id, name)
    }

    /**
     * Constructor for CustomerSegment with description.
     */
    CustomerSegment(String id, String name, String description)
    {
        super(id, name)
        this.description = description
    }

    String getIdWithoutPrefix()
    {
        int index = this.id.indexOf("_")
        index > 0 ? this.id.substring(index+1) : this.id
    }
}