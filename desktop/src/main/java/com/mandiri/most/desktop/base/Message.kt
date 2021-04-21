package com.mandiri.most.desktop.base

class Message {
    var messageHeading: String? = null
    var messageInfo: String? = null

    constructor() {}
    constructor(messageHeading: String?, messageInfo: String?) {
        this.messageHeading = messageHeading
        this.messageInfo = messageInfo
    }
}