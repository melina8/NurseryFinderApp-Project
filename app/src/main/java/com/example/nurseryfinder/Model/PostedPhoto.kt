package com.example.nurseryfinder.Model


class PostedPhoto {

    private var photoId: String = ""
    private var postImage: String = ""
    private var publisher: String = ""
    private var description: String = ""

    constructor()

    constructor(photoId: String, postImage: String, publisher: String, description: String) {
        this.photoId = photoId
        this.postImage = postImage
        this.publisher = publisher
        this.description = description
    }

    fun getPhotoId():String{
        return photoId
    }

    fun getPostImage():String{
        return postImage
    }

    fun getPublisher():String{
        return publisher
    }

    fun getDescription():String{
        return description
    }

    fun setPhotoId(photoId: String){
        this.photoId = photoId
    }

    fun setPostImage(postImage: String){
        this.postImage = postImage
    }

    fun setPublisher(publisher: String){
        this.publisher = publisher
    }

    fun setDescription(description: String){
        this.description = description
    }



}