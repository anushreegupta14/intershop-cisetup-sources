package geb.com.intershop.inspired.pages.backoffice.channels.business

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.BackOfficePage;

class BackOfficeSMBApplicationPage extends BackOfficePage
{
    static at=
    {
        waitFor(10) { $("div", "data-testing-id": "bo-channel-overview") }
    }

    static content = {
        siteHeader     { $('#main_header') }
    }
}