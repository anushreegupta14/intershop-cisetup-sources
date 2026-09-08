package geb.com.intershop.inspired.pages.backoffice.imagemanagement

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class ImageManagementPage extends BackOfficePage
{
    static at = {
        waitFor { $('table[data-testing-id="imagemanagement-mastercatalog-organization"]').size() > 0 }
    }

    static content = {
        imageTypesLink(to: ImageManagementTypeListPage)  {$("a", href: contains("ViewImageTypeList-ListAll"))}
    }

    def clickImageTypesTab()
    {
        imageTypesLink.click()
    }
}