#Playing MultipartFormData
A basic example to handle and test MultipartFormData request in Play Framework 2.3.8

The standard way to upload files in a web application is to use a form with a special multipart/form-data encoding, which lets you mix standard form data with file attachment data.
Please note: the HTTP method used to submit the form must be POST (not GET). 

-----------------------------------------------------
###Code in action for Controller and Service
-----------------------------------------------------
- Multipart form upload handlers looks like this:
[Application.scala](https://github.com/knoldus/playing-multipartform/blob/master/app/controllers/Application.scala)

```scala
  val uploadService: UploadService

  def upload = Action(parse.multipartFormData) { implicit request =>
    val result = uploadService.uploadFile(request)
    Redirect(routes.Application.index).flashing("message" -> result)
  }
```


- Service for upload file looks like this:
[UploadService.scala](https://github.com/knoldus/playing-multipartform/blob/master/app/services/UploadService.scala)
```scala
  /**
   * Get file from the request and move it in your location
   *
   * @param request
   * @return
   */
  def uploadFile(request: Request[MultipartFormData[TemporaryFile]]): String = {
    log.error("Called uploadFile function" + request)
    request.body.file("file").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      log.error(s"File name : $filename, content type : $contentType")
      picture.ref.moveTo(new File(s"/tmp/$filename"))
      "File uploaded"
    }.getOrElse {
      "Missing file"
    }
  }

```
------------------------------------------------------
###Test Code for Controller and Service
------------------------------------------------------
[ApplicationSpec.scala](https://github.com/knoldus/playing-multipartform/blob/master/test/ApplicationSpec.scala)
```scala
"should be valid" in new WithApplication {
  val request = mock[Request[MultipartFormData[TemporaryFile]]]
  mockedUploadService.uploadFile(request) returns "File Uploaded"
  val result: Future[Result] = TestController.upload().apply(request)
  status(result) must equalTo(SEE_OTHER)
}
```

[UploadServiceSpec.scala](https://github.com/knoldus/playing-multipartform/blob/master/test/services/UploadServiceSpec.scala)
```scala
"UploadService" should {
    "uploadFile returns (File uploaded)" in new WithApplication {
      val files = Seq[FilePart[TemporaryFile]](FilePart("file", "UploadServiceSpec.scala", None, TemporaryFile("file", "spec")))
      val multipartBody = MultipartFormData(Map[String, Seq[String]](), files, Seq[BadPart](), Seq[MissingFilePart]())
      val fakeRequest = FakeRequest[MultipartFormData[Files.TemporaryFile]]("POST", "/", FakeHeaders(), multipartBody)
      val success = UploadService.uploadFile(fakeRequest)
      success must beEqualTo("File uploaded")
    }
    
    "uploadFile returns (Missing file)" in new WithApplication {
      val files = Seq[FilePart[TemporaryFile]]()
      val multipartBody = MultipartFormData(Map[String, Seq[String]](), files, Seq[BadPart](), Seq[MissingFilePart]())
      val fakeRequest = FakeRequest[MultipartFormData[Files.TemporaryFile]]("POST", "/", FakeHeaders(), multipartBody)
      val success = UploadService.uploadFile(fakeRequest)
      success must beEqualTo("Missing file")
    }
}
```

-----------------------------------------------------------------------
###Build and Run the application :-
-----------------------------------------------------------------------
* To run the Play Framework, you need JDK 6 or later
* Install Typesafe Activator if you do not have it already. You can get it from [here](http://www.playframework.com/download) 
* Execute `./activator clean compile` to build the product
* Execute `./activator run` to execute the product
* playing-multipartform should now be accessible at localhost:9000

-----------------------------------------------------------------------
###Test the application with code coverage
-----------------------------------------------------------------------
* Execute `$ ./activator clean coverage test` to test
* Execute `$ ./activator coverageReport` to generate coverage report

File Upload Form
![alt tag](/public/images/multipartform.png)

Test Coverage
![alt tag](/public/images/code_coverage.png)

-----------------------------------------------------------------------
###References :-
-----------------------------------------------------------------------
* [Play Framework](http://www.playframework.com/)
* [Handling file upload](https://playframework.com/documentation/2.3.x/ScalaFileUpload)
* [stackoverflow](http://stackoverflow.com/a/19670860/2893807)
* [Bootstrap 3.1.1](http://getbootstrap.com/css/)
* [Bootswatch](http://bootswatch.com/darkly/)
* [WebJars](http://www.webjars.org/)
