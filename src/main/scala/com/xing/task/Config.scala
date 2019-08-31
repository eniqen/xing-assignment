package com.xing.task

import cats.effect._
import doobie._
import doobie.h2._

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
case class Config(db: Database, poolSize: Int)

case class Database(path: String = "./data/db/users",
                    username: String = "sa",
                    password: String = "") {

  def url: String =
    s"jdbc:h2:$path;INIT=runscript from 'classpath:initDb.sql'"
}

object Database {
  def transactor[F[_]: Effect: ContextShift](
      config: Config)
  : Resource[F, H2Transactor[F]] =
    for {
      ex   <- ExecutionContexts.fixedThreadPool[F](config.poolSize)
      trEx <- ExecutionContexts.cachedThreadPool[F]
      xa   <- H2Transactor.newH2Transactor[F](
        config.db.url,
        config.db.username,
        config.db.password,
        ex,
        Blocker.liftExecutionContext(trEx)
      )
    } yield xa
}
