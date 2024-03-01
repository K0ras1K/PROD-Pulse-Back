package online.k0ras1k.pulse.data.database

import online.k0ras1k.pulse.data.models.dto.CountryData
import online.k0ras1k.pulse.data.enums.Region
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Country: Table("countries") {
    //==============================================SCHEMA==============================================================
    private val _id = Country.integer("id").autoIncrement()
    private val _name = Country.text("name")
    private val _alpha2 = Country.text("alpha2")
    private val _alpha3 = Country.text("alpha3")
    private val _region = Country.text("region")

    override val primaryKey = PrimaryKey(_id)
    //==============================================SCHEMA==============================================================


    //===============================================CRUD===============================================================
    fun selectCountry(regions: List<Region>): List<CountryData> {
        return transaction {
            val query = if (regions.isNotEmpty()) {
                val string_regions_list = regions.map { it.toString() }
                Country.select { Country._region inList string_regions_list }.orderBy(Country._alpha2)
            } else {
                Country.selectAll().orderBy(Country._alpha2)
            }
            query.map { rowToCountryData(it) }
        }
    }
    //===============================================CRUD===============================================================


    //===============================================UTIL===============================================================
    fun selectCountryByAlpha2(alpha2: String): CountryData? {
        return try {
            transaction {
                val respond = Country.select { Country._alpha2 eq alpha2 }.single()
                rowToCountryData(respond)
            }
        }
        catch(exception: Exception) {
            null
        }

    }

    private fun rowToCountryData(row: ResultRow): CountryData {
        if (row[Country._region] != "") {
            println("reyurning all country data")
            return CountryData(
                name = row[Country._name],
                alpha2 = row[Country._alpha2],
                alpha3 = row[Country._alpha3],
                region = Region.valueOf(row[Country._region])
            )
        }
        return CountryData(
            name = row[Country._name],
            alpha2 = row[Country._alpha2],
            alpha3 = row[Country._alpha3],
            null
        )
    }
    //===============================================UTIL===============================================================
}