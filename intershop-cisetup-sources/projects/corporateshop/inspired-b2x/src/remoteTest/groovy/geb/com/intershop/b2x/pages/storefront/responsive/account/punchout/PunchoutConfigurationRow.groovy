package geb.com.intershop.b2x.pages.storefront.responsive.account.punchout

import geb.Module

class PunchoutConfigurationRow extends Module
{
    static at =
    {
        waitFor { label.displayed }
        waitFor { transform.displayed }
        waitFor { mappingfrom.displayed }
        waitFor { mappingto.displayed }
        waitFor { format.displayed }
    }

    static content = {
        label { $('[data-testing-class="row-label"]').text() }
        transform { $('[data-testing-class="row-transform"]') }
        mappingfrom { $('[data-testing-class="row-map-from"]') }
        mappingto { $('[data-testing-class="row-map-to"]') }
        format { $('[data-testing-class="row-format"]') }
    }
}
