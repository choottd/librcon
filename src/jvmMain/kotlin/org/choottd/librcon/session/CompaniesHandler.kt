/*
 * Copyright 2021 Giordano Battilana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.choottd.librcon.session

import org.choottd.librcon.gamestate.*
import org.choottd.librcon.packet.data.*
import org.choottd.librcon.session.event.CompanyData
import org.choottd.librcon.session.event.CompanyRemoveEvent
import org.choottd.librcon.session.event.CompanyUpdateEvent

/**
 * Coroutine that manages the state about the companies
 */
suspend fun Session.companiesHandler(data: CompaniesPacketData) {
    val event = when (data) {
        is ServerCompanyInfo -> {
            val company = CompanyState(
                data.companyId, data.name, data.president, Color.valueOf(data.color),
                GameStateService.convertDate(data.inaugurated), data.ai
            )
            globalState.companies[company.companyId] = company
            CompanyUpdateEvent(CompanyData.from(company))
        }

        is ServerCompanyEconomy -> {
            val company = globalState.companies[data.companyId]
            val dateCurrent = globalState.dateCurrent
            if (dateCurrent == null || company == null) {
                null
            } else {
                val economy = EconomyState(
                    dateCurrent,
                    data.economy.money,
                    data.economy.loan,
                    data.economy.income,
                )
                val economyQM1 = PastEconomyState(
                    dateCurrent.previousQuarter(),
                    data.economyQM1.value,
                    data.economyQM1.cargo,
                    data.economyQM1.performance,
                )
                val economyQM2 = PastEconomyState(
                    economyQM1.date.previousQuarter(),
                    data.economyQM2.value,
                    data.economyQM2.cargo,
                    data.economyQM2.performance,
                )

                company.economy = economy
                company.economyHistory = listOf(economyQM1, economyQM2)

                CompanyUpdateEvent(CompanyData.from(company))
            }
        }

        is ServerCompanyNew -> {
            val company = globalState.companies[data.companyId]
            if (company != null) {
                CompanyUpdateEvent(CompanyData.from(company))
            } else {
                fetchCompany(data.companyId)
                null
            }
        }

        is ServerCompanyRemove -> {
            val company = globalState.companies.remove(data.companyId)
            if (company != null) {
                CompanyRemoveEvent(
                    CompanyData.from(company),
                    CompanyRemoveEvent.AdminCompanyRemoveReason.valueOf(data.removeReason)
                )
            } else null
        }

        is ServerCompanyStats -> {
            val company = globalState.companies[data.companyId]
            if (company != null) {
                company.vehicles = data.vehicles
                company.stations = data.stations
                CompanyUpdateEvent(CompanyData.from(company))
            } else {
                fetchCompany(data.companyId)
                null
            }
        }

        is ServerCompanyUpdate -> {
            val company = globalState.companies[data.companyId]
            if (company != null) {
                company.name = data.name
                company.president = data.president
                company.color = Color.valueOf(data.color)
                company.passwordProtected = data.passwordProtected
                company.bankruptcy = data.bankruptcy
                company.shares = data.shares
                CompanyUpdateEvent(CompanyData.from(company))
            } else {
                fetchCompany(data.companyId)
                null
            }
        }
    }

    sendEvent(event)
}
