package com.example.videoplayerkotlin.model

class VideoModel {
    var id: String
    var path: String
    var title: String
    var size: String
    var resolution: String
    var duration: String
    var displayName: String
    var wh: String

    constructor(
        id: String,
        path: String,
        title: String,
        size: String,
        resolution: String,
        duration: String,
        displayName: String,
        wh: String
    ) {
        this.id = id
        this.path = path
        this.title = title
        this.size = size
        this.resolution = resolution
        this.duration = duration
        this.displayName = displayName
        this.wh = wh
    }


}