package com.blackbear.business.configuration

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsRuntimeWiring
import com.netflix.graphql.dgs.DgsScalar
import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.StringValue
import graphql.language.Value
import graphql.scalars.ExtendedScalars
import graphql.schema.Coercing
import graphql.schema.CoercingSerializeException
import graphql.schema.DataFetcherFactories
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@DgsComponent
class DgsRuntimeWiringRegistration {
    @DgsRuntimeWiring
    fun addScalarLong(builder: RuntimeWiring.Builder): RuntimeWiring.Builder = builder.scalar(ExtendedScalars.GraphQLLong)

    @DgsRuntimeWiring
    fun addScalarDouble(builder: RuntimeWiring.Builder): RuntimeWiring.Builder = builder.scalar(ExtendedScalars.GraphQLBigDecimal)

    @DgsRuntimeWiring
    fun addTimezoneDirective(builder: RuntimeWiring.Builder): RuntimeWiring.Builder =
        builder.directive("timezone", TimeZoneDirectiveWiring())
}

@DgsScalar(name = "LocalDate")
class LocalDateScalar : Coercing<LocalDate, String> {
    private val customFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun serialize(
        input: Any,
        context: GraphQLContext,
        locale: Locale,
    ): String =
        when (input) {
            is LocalDate -> input.format(customFormatter)
            else -> throw CoercingSerializeException("Expected a LocalDate object.")
        }

    override fun parseValue(
        input: Any,
        context: GraphQLContext,
        locale: Locale,
    ): LocalDate = parseLocalDate(input.toString())

    override fun parseLiteral(
        input: Value<*>,
        variables: CoercedVariables,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): LocalDate? = (input as? StringValue)?.value?.let { parseLocalDate(it) }

    private fun parseLocalDate(value: String): LocalDate =
        runCatching { LocalDate.parse(value, customFormatter) }
            .getOrElse { throw CoercingSerializeException("Invalid LocalDate format: $value. Only yyyy-MM-dd is allowed.") }
}

class TimeZoneDirectiveWiring : SchemaDirectiveWiring {
    override fun onField(environment: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>): GraphQLFieldDefinition {
        val fieldDefinition = environment.fieldDefinition
        val fieldContainer = environment.fieldsContainer as GraphQLObjectType

        val originalDataFetcher = environment.codeRegistry.getDataFetcher(fieldContainer, fieldDefinition)
        val dataFetcher =
            DataFetcherFactories.wrapDataFetcher(originalDataFetcher) { env: DataFetchingEnvironment, value: Any? ->
                val zoneId = env.getArgument<String>("zoneId") ?: "Asia/Seoul"
                if (value is OffsetDateTime) {
                    return@wrapDataFetcher value.atZoneSameInstant(ZoneId.of(zoneId))
                }
                value
            }

        environment.codeRegistry.dataFetcher(fieldContainer, fieldDefinition, dataFetcher)
        return fieldDefinition
    }
}
