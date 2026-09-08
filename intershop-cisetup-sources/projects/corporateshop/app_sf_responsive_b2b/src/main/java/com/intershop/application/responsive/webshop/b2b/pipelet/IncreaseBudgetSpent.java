package com.intershop.application.responsive.webshop.b2b.pipelet;

import javax.inject.Inject;

import com.intershop.beehive.core.capi.log.Logger;
import com.intershop.beehive.core.capi.pipeline.Pipelet;
import com.intershop.beehive.core.capi.pipeline.PipeletExecutionException;
import com.intershop.beehive.core.capi.pipeline.PipelineDictionary;
import com.intershop.component.application.capi.CurrentApplicationBOProvider;
import com.intershop.component.approval.capi.budget.BudgetPeriod;
import com.intershop.component.approval.capi.budget.BudgetPeriodProvider;
import com.intershop.component.approval.capi.budget.BudgetType;
import com.intershop.component.approval.capi.user.UserBOOrderApprovalExtension;
import com.intershop.component.order.capi.OrderBO;
import com.intershop.component.user.capi.UserBO;

/**
 * Increases budget spent for given {@link BudgetPeriod} If {@link BudgetPeriod}
 * configuration parameter is not provided,
 * UserBOOrderApprovalExtension.getBudgetPeriodHandler.getBudgetPeriod() value is used.
 *
 */
public class IncreaseBudgetSpent extends Pipelet
{

    public static final String DN_USER_BO = "UserBO";
    public static final String DN_ORDER_BO = "OrderBO";
    public static final String DN_BUDGET_TYPE = "BudgetType";

    @Inject
    private CurrentApplicationBOProvider applicationBOProvider;

    @Override
    public int execute(PipelineDictionary dict) throws PipeletExecutionException
    {
        // lookup 'User' in pipeline dictionary
        UserBO user = dict.getRequired(DN_USER_BO);

        // lookup 'Order' in pipeline dictionary
        OrderBO order = dict.getRequired(DN_ORDER_BO);

        String budgetPeriodId = dict.getOptional(DN_BUDGET_TYPE);
        BudgetPeriod budgetPeriod = BudgetType.NONE;
        UserBOOrderApprovalExtension extension = user.getExtension(UserBOOrderApprovalExtension.class);
        try
        {
            if (null == budgetPeriodId)
            {
                budgetPeriod = extension.getBudgetPeriodHandler().getBudgetPeriod();
            }
            else
            {
                BudgetPeriodProvider budgetPeriodProvider = applicationBOProvider.get().getNamedObject("BudgetPeriodProvider");
                budgetPeriod =  budgetPeriodProvider.getBudgetPeriodById(budgetPeriodId);
            }
            
            extension.getBudgetPeriodHandler().increaseBudgetSpent(order.getGrandTotalGross(), budgetPeriod);
        }
        catch(Exception e)
        {
            Logger.error(this,
                            "Adding total amount of order #" + order.getDocumentNo() + " ("
                                            + order.getGrandTotalGross() + ") to monthly budget spent of user "
                                            + user.getLastName() + ", " + user.getFirstName() + " ("
                                            + extension.getBudgetPeriodHandler().getBudgetSpent(budgetPeriod) + ") finished with an error: ", e);
            return PIPELET_ERROR;
        }

        return PIPELET_NEXT;
    }
}
