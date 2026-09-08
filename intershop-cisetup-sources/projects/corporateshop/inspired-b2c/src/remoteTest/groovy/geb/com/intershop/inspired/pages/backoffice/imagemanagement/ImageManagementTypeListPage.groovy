package geb.com.intershop.inspired.pages.backoffice.imagemanagement

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.module.Select

class ImageManagementTypeListPage extends BackOfficePage
{
    static at = {
        waitFor { $('table[data-testing-id="typeList-imagemanagement-mastercatalog-organization"]').size() > 0 }
    }

    static content = {
        imageTypeSelect {$("select", id:"SelectedImageTypeID").module(Select)}
        applyButton(to : ImageManagementTypeListPage) {$("input", name:"SetCatalogImageType")}
    }

    def selectImageType(def imageType)
    {
        imageTypeSelect.selected = imageType
        applyButton.click()
    }

    def selectImageTypeByIndex(int index)
    {
        selectImageType(imageTypeSelect.children().getAt(index).value())
    }

    boolean hasSelectedImageType()
    {
        imageTypeSelect.getSelected() != ""
    }

    def getSelectedImageType()
    {
        imageTypeSelect.selected
    }
}