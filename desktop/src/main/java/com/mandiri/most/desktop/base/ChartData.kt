package com.mandiri.most.desktop.base

class ChartData {
    var dataYear: Int? = null
    var dataMonth: String? = null
    var dataValue: Double? = null
    var dataType: String? = null
    var dataYearMonth: String? = null

    constructor(dataYear: Int, dataMonth: String, dataValue: Double?, dataType: String?) {
        this.dataYear = dataYear
        this.dataMonth = dataMonth
        this.dataValue = dataValue
        this.dataType = dataType
        dataYearMonth = "$dataMonth - $dataYear"
    }

    constructor() {}
}