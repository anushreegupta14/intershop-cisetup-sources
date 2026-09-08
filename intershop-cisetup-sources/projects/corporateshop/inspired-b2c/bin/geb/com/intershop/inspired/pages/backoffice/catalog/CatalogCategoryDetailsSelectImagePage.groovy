package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class CatalogCategoryDetailsSelectImagePage extends BackOfficePage
{
    static content= {
        localeForm                  { $("form", name: "setLocale") }
        btnApplyLanguage            { localeForm.$('input', name: 'apply') }
        
        imageDirectoriesTable       { $('table[data-testing-id="bo-catalog-category-properties-image-selection-directory-table"]') }
        
        searchForm                  { $("form", name: "SearchForm") }
        
        uploadImageForm             { $("form", name: "ProductAttachmentsUploadForm") }
        btnBrowseImage              { uploadImageForm.$('input', name: 'File') }
        btnUploadImage              { uploadImageForm.$('input', name: 'Upload') }
        
        imgAssignmentForm           { $("form", name: "ImageAssignmentForm") }
        pagingImageGalleryShowAll   { $('input', value: "Show All") }
    }

    def searchForImage(directoryPath, imageName)
    {
        def testingRootId = "bo-contentdirectory-"
        def testingId = "bo-contentdirectory-" + directoryPath
        // check if directory 'M' below 'root' is visible and can be clicked
        def imageRootDirectory = imageDirectoriesTable.$("a", "data-testing-id": "${testingRootId}")
        def imageSubDirectoryExpanded = imageDirectoriesTable.$("a", "data-testing-id": "${testingId}")
        
        waitFor{ ($(imageDirectoriesTable).displayed) }

        if(!imageSubDirectoryExpanded.displayed)
        {
            imageRootDirectory.click()
            // click reloads the page
            waitFor{ ($(imageDirectoriesTable).displayed) }
            imageSubDirectoryExpanded = imageDirectoriesTable.$("a", "data-testing-id": "${testingId}")
            waitFor{ ($(imageSubDirectoryExpanded).displayed) }
        }
        imageSubDirectoryExpanded.click()
        // click reloads the page
        waitFor{ ($(imageDirectoriesTable).displayed) }

        searchForm.$('input', name: "Expression").value imageName
        btnSearchImage.click()
        
    }

    def assignImage(directoryPath, imageName)
    {
        searchForImage(directoryPath, imageName)
        waitFor{ ($(imgAssignmentForm).size() > 0) }
        
        def imageRadioBtn = imgAssignmentForm.$('input', name:"Image", type:"radio", id: imageName)
        assert imageRadioBtn.displayed
        
        imageRadioBtn.click()
        btnOkImageAssignment.click()
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

