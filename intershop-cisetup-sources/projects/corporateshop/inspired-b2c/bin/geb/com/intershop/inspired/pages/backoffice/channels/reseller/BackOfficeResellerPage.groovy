package geb.com.intershop.inspired.pages.backoffice.channels.reseller

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.BackOfficePage;

class BackOfficeResellerPage extends BackOfficePage
{
    static at=
    {
        waitFor(10) { $("div", "data-testing-id": "PartnerBackOffice") }
    }

    static content = {
        siteHeader     { $('#main_header') }
    }
}