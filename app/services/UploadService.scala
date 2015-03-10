package services

import play.api.mvc.Request
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile
import play.api.Logger

object UploadService extends UploadService

trait UploadService {

  private val log: Logger = Logger(this.getClass)

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

}