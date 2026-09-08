package com.intershop.application.responsive.webshop.b2b.pipelet;

import java.util.Date;

import com.intershop.beehive.core.capi.pipeline.Pipelet;
import com.intershop.beehive.core.capi.pipeline.PipeletExecutionException;
import com.intershop.beehive.core.capi.pipeline.PipelineDictionary;
import com.intershop.component.approval.capi.basket.BasketBOOrderApprovalExtension;
import com.intershop.component.basket.capi.BasketBO;

/**
 * Sets the system rejected information to the given basket.
 */
public class SetBasketSystemRejectedInformation extends Pipelet
{
    public static final String DN_BASKET_BO = "BasketBO";
                
    @Override
    public int execute(PipelineDictionary dict) throws PipeletExecutionException
    {
        // lookup 'BasketBO' in pipeline dictionary
        BasketBO basket = dict.getRequired(DN_BASKET_BO);

        BasketBOOrderApprovalExtension extension = basket.getExtension(BasketBOOrderApprovalExtension.EXTENSION_ID);
        extension.setSystemRejected(true);        
        extension.setApprovalDate(new Date());
        return PIPELET_NEXT;
    }
}
