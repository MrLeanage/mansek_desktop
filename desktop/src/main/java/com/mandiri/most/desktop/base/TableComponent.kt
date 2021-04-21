package com.mandiri.most.desktop.base

class TableComponent {
    var headComponent : String? = null
    var msgComponent : String? = null

    constructor(headComponent: String?, msgComponent: String?) {
        this.headComponent = headComponent
        this.msgComponent = msgComponent
    }
    constructor(headComponent: Int?, msgComponent: Int?) {
        this.headComponent = headComponent.toString()
        this.msgComponent = msgComponent.toString()
    }
    constructor(headComponent: String?, msgComponent: Int?) {
        this.headComponent = headComponent
        this.msgComponent = msgComponent.toString()
    }
    constructor(headComponent: Int?, msgComponent: String?) {
        this.headComponent = headComponent.toString()
        this.msgComponent = msgComponent
    }
    constructor(headComponent: Double?, msgComponent: Double?) {
        this.headComponent = headComponent.toString()
        this.msgComponent = msgComponent.toString()
    }
}