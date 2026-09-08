package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage


/**
 * This page describes only the outer html page details of every BO catalog category properties handling page,
 * i.e. the  tabulator links and the 'Back to List' button.
 * All other category properties handling detail pages should inherit from this page.
 *
 */
class CatalogCategoryDetailsPage extends BackOfficePage
{
    static content= {
        backToListForm              { $("form", name:"backForm") }
    }

    def clickGeneral()
    {
        linkGeneral.click()

    }

    def clickContent()
    {
        linkContent.click()

    }

    def clickSubCategories()
    {
        linkSubCategories.click()

    }

    def clickAttributes()
    {
        linkAttributes.click()

    }

    def clickLabels()
    {
        linkLabels.click()

    }

    def clickLinks()
    {
        linkLinks.click()

    }

    def clickBackToList()
    {
        btnBackToList.click()
    }

}

