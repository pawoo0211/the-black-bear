package com.blackbear.business.accountbook.framework.`in`.resolver

import com.commerce.proxy.shopping.infrastructure.dto.types.CreateTransactionInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

/**
 * Account book DGS Resolver
 */
@DgsComponent
class AccountBookResolver {
    @DgsMutation
    fun createTransaction(
        @InputArgument("input") input: CreateTransactionInput
    ) {
        println("createTransaction input : $input")
    }
}
