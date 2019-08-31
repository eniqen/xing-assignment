package com.xing.task.readers

import java.nio.file.Path
import java.time.LocalDate
import java.time.Month._

import cats.Id
import cats.effect.{Effect, Sync}
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.JobTitle._
import com.xing.task.models.UserInfo.{JobTitle, UserId}
import doobie.util.transactor.Transactor
import cats.implicits._
import com.xing.task.{Ord, Result}
import doobie._
import doobie.implicits._

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
trait Reader[F[_], T] {
  def read: F[T]
}

object Reader {
  def apply[F[_], T](implicit R: Reader[F, T]): Reader[F, T] = R

  object InMemmoryUserReader extends Reader[Id, Seq[UserInfo]] {
    override def read: Seq[UserInfo] = Seq(
      UserInfo(UserId(42), ScalaDev, LocalDate.of(2018, MAY, 15), None),
      UserInfo(UserId(42),
               J2EEDeveloper,
               LocalDate.of(2017, JANUARY, 15),
               Some(LocalDate.of(2018, FEBRUARY, 15))),
      UserInfo(UserId(42),
               WebDeveloper,
               LocalDate.of(2015, MAY, 15),
               Some(LocalDate.of(2016, OCTOBER, 15))),
      UserInfo(UserId(76),
               DataScientist,
               LocalDate.of(2017, JANUARY, 15),
               None),
      UserInfo(UserId(76),
               WebDeveloper,
               LocalDate.of(2013, DECEMBER, 15),
               Some(LocalDate.of(2016, DECEMBER, 15))),
      UserInfo(UserId(103),
               J2EEDeveloper,
               LocalDate.of(2016, DECEMBER, 15),
               None),
      UserInfo(UserId(103),
               WebDeveloper,
               LocalDate.of(2011, JULY, 15),
               Some(LocalDate.of(2015, JULY, 15)))
    )
  }

  case class FileReader[F[_]: Sync](path: Path) extends Reader[F, Seq[UserInfo]] {
    override def read: F[Seq[UserInfo]] = ??? // todo add implementation
  }

  case class DatabaseReader[F[_]: Effect](xa: Transactor[F])
      extends Reader[F, Seq[UserInfo]] {

    override def read: F[Seq[UserInfo]] =
      UserInfoSQL.getAll.to[Seq].transact(xa)

    def getTopK(k: Int): F[List[Result]] =
      UserInfoSQL.topK(k).to[List].transact(xa)

    private object UserInfoSQL {

      implicit val userIdPut: Put[UserId] = Put[Int].contramap(_.id)
      implicit val userIdGet: Get[UserId] = Get[Int].map(UserId.apply)
      implicit val jobTitlePut: Put[JobTitle] =
        Put[String].contramap(_.entryName)
      implicit val jobTitleGet: Get[JobTitle] =
        Get[String].map(JobTitle.withName)

      def insert(userInfo: UserInfo): Update0 =
        sql"""INSERT INTO USER_INFO (ID, JOB_TITLE, START, END)
             | VALUES (${userInfo.id}, ${userInfo.jobTitle.entryName}, ${userInfo.start}, ${userInfo.end})
           """.update

      def insertAll(infos: List[UserInfo]): ConnectionIO[Unit] = {
        val sql =
          "INSERT INTO USER_INFO (ID, JOB_TITLE, START, END) values (?, ?, ?, ?)"
        Update[UserInfo](sql).updateMany(infos).void
      }

      def topK(k: Int, ord: Ord = Ord.DESC): Query0[Result] =
        Fragment
          .const(
            s"""
               |SELECT TOP $k X.JOB_TITLE, COUNT(*) AS NUMBER
               |FROM USER_INFO AS X
               |     INNER JOIN (
               |     SELECT U1.ID, Min(U1.START) AS MIN
               |     FROM USER_INFO U1 JOIN USER_INFO U2
               |     ON U1.ID = U2.ID AND U1.START > U2.END AND U1.JOB_TITLE != 'Web Developer'
               |     WHERE U2.END IS NOT NULL
               |     AND U2.JOB_TITLE = 'Web developer'
               |     GROUP BY U1.ID
               |  ) AS X2 ON X.ID = X2.ID WHERE X.START = X2.MIN
               |GROUP BY X.JOB_TITLE
               |ORDER BY NUMBER ${ord.toString}
         """.stripMargin
          )
          .query[Result]

      def getById(id: UserId): Query0[UserInfo] =
        sql"SELECT ID, JOB_TITLE, START, END FROM USER_INFO WHERE ID = $id"
          .query[UserInfo]

      def getAll: Query0[UserInfo] =
        sql"SELECT ID, JOB_TITLE, START, END FROM USER_INFO".query[UserInfo]
    }
  }
}
