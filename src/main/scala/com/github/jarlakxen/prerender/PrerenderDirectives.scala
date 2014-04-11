package com.github.jarlakxen.prerender

import akka.actor._
import spray.routing._
import spray.routing.directives._
import spray.http._
import spray.http.MediaTypes._
import spray.http.HttpHeaders._
import spray.client.pipelining._
import scala.concurrent._
import scala.util.Success

trait PrerenderDirectives {
  import BasicDirectives._
  import RouteDirectives._
  import RespondWithDirectives._
  import ExecutionDirectives._

  val DEFAULT_CRAWLER_USER_AGENTS = Seq("googlebot", "yahoo", "bingbot", "baiduspider",
    "facebookexternalhit", "twitterbot", "rogerbot", "linkedinbot", "embedly")

  val DEFAULT_EXTENSIONS_TO_IGNORE = Seq(".js", ".css", ".less", ".png", ".jpg", ".jpeg",
    ".gif", ".pdf", ".doc", ".txt", ".zip", ".mp3", ".rar", ".exe", ".wmv", ".doc", ".avi", ".ppt", ".mpg",
    ".mpeg", ".tif", ".wav", ".mov", ".psd", ".ai", ".xls", ".mp4", ".m4a", ".swf", ".dat", ".dmg",
    ".iso", ".flv", ".m4v", ".torrent")

  def routePrerender(serverHost: String,
    actorRefFactory: ActorRefFactory,
    crawlerUserAgents: Seq[String] = DEFAULT_CRAWLER_USER_AGENTS,
    extensionToIgnore: Seq[String] = DEFAULT_EXTENSIONS_TO_IGNORE) = {
    PrerenderConfiguration(serverHost, crawlerUserAgents, extensionToIgnore, actorRefFactory.dispatcher, sendReceive(actorRefFactory, actorRefFactory.dispatcher))
  }

  def prerender(implicit config: PrerenderConfiguration): Directive0 = mapInnerRoute { route ⇒
    implicit ctx ⇒
      val request = ctx.request
      if (!isGetMethod(request) || isResource(request) || !isCrawler(request)) {
        route(ctx)
      } else {
        respondWithMediaType(`text/html`) {
          detach(config.executionContext) {
            getFromPrerender
          }
        }(ctx)
      }
  }

  private def getFromPrerender(ctx: RequestContext)(implicit config: PrerenderConfiguration) {
    implicit val executionContext = config.executionContext
    val prerenderRequest = Get(ctx.request.uri.toString)
    config.pipeline(prerenderRequest).onComplete{
       case Success(HttpResponse(StatusCodes.OK, entity, _, _)) if entity.nonEmpty =>  {
         ctx.complete(entity.asString)
       }
      case result => ctx.complete(StatusCodes.InternalServerError -> result.toString)
    }
  }

  private def isGetMethod(request: HttpRequest)(implicit config: PrerenderConfiguration) = request.method == HttpMethods.GET

  private def isCrawler(request: HttpRequest)(implicit config: PrerenderConfiguration) = request.headers.exists {
    case userAgent: `User-Agent` if userAgent.products.exists(u => config.crawlerUserAgents.exists(_.toLowerCase.contains(u.value.toLowerCase))) ⇒ true
    case _ ⇒ false
  }

  private def isResource(request: HttpRequest)(implicit config: PrerenderConfiguration) = {
    val uri = request.uri.toString.toLowerCase()
    config.extensionToIgnore.exists(uri.endsWith(_))
  }
}

object PrerenderDirectives extends PrerenderDirectives

case class PrerenderConfiguration(serverHost: String, crawlerUserAgents: Seq[String], extensionToIgnore: Seq[String], executionContext: ExecutionContext, pipeline: HttpRequest => Future[HttpResponse])
