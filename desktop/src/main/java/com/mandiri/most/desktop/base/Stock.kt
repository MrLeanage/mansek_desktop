package com.mandiri.most.desktop.base

import java.text.DecimalFormat


class Stock {
    var mSymbol: TableComponent? = null
    var mLotShare : TableComponent? = null
    var mAvailableLastPrice : String? = null
    var mAveragePrice : TableComponent? = null
    var mGainLost : TableComponent? = null
    var mMarketStockValue : TableComponent? = null
    var mHaircutValue : String? = null

    constructor(

        mSymbolHead: String?,
        mSymbolMessage: String?,
        mLot: Int,
        mShare: Int,
        mAvailable: Int,
        mLastPrice: Int,
        mAveragePrice: Double,
        mGain: String?,
        mLoss: String?,
        mMarketValue: Int,
        mStockValue: Int,
        mHaircutValue: Double
    ) {
        val twoDecimalFormat = DecimalFormat("#####.00")
        val commaDecimalFormat = DecimalFormat("#,###")

        this.mSymbol = TableComponent(mSymbolHead, mSymbolMessage)
        this.mLotShare = TableComponent(mLot,mShare)
        this.mAvailableLastPrice = mAvailable.toString()
        this.mAveragePrice = TableComponent(commaDecimalFormat.format(mLastPrice), commaDecimalFormat.format(mAveragePrice))
        this.mGainLost = TableComponent(mGain, mLoss)
        this.mMarketStockValue = TableComponent(commaDecimalFormat.format(mMarketValue), commaDecimalFormat.format(mStockValue))
        this.mHaircutValue = mHaircutValue.toString()
    }
}