package com.xing.task

import cats.effect.{ContextShift, Effect, ExitCode, IO, IOApp}
import com.xing.task.readers.Reader.DatabaseReader

import scala.concurrent.ExecutionContext

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
object DbApp extends IOApp {

  private implicit val CS: ContextShift[IO] =
    IO.contextShift(ExecutionContext.global)
  private val db = Database()
  private val config = Config(db, 32)

  override def run(args: List[String]): IO[ExitCode] = start[IO]

  private def start[F[_]: Effect: ContextShift]: F[ExitCode] =
    Database.transactor[F](config).use { xa =>
      import cats.syntax.flatMap._
      import cats.syntax.functor._
      val dbReader = DatabaseReader[F](xa)

      for {
        all <- dbReader.read
        _ <- Effect[F].delay(println(s"""|
            |CURRENT
            |------------------------------
            |${all.mkString("\n")}
          """.stripMargin))
        top3 <- dbReader.getTopK(3)
        _ <- Effect[F].delay(println(s"""
            |TopK
            |------------------------------
            |${top3.mkString("\n")}
          """.stripMargin))
      } yield ExitCode.Success
    }
}
