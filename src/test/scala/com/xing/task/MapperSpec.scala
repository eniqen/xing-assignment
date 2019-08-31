package com.xing.task

import java.time.Month._
import java.time.LocalDate

import cats.Id
import com.xing.task.mappers.Mapper
import com.xing.task.mappers.Mapper.UserMapper
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.JobTitle.{
  J2EEDeveloper,
  PHPDev,
  ScalaDev,
  WebDeveloper
}
import com.xing.task.models.UserInfo.{JobTitle, UserId}
import org.scalatest.{Matchers, Outcome, fixture}
import cats.syntax.option._

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
class MapperSpec extends fixture.FlatSpec with Matchers {

  case class FixtureParam(mapper: Mapper[Id, Seq[UserInfo], Seq[JobTitle]])

  def withFixture(test: OneArgTest): Outcome = {
    val mapper: Mapper[Id, Seq[UserInfo], Seq[JobTitle]] = UserMapper
    val fixture = FixtureParam(mapper)
    withFixture(test.toNoArgTest(fixture))
  }

  it should "return nothing" in { f =>
    val empty: Seq[UserInfo] = Seq.empty[UserInfo]

    val result = f.mapper.map(empty)

    result shouldBe empty
  }

  it should "return nothing when web developer started after" in { f =>
    val userInfos = Seq(
      UserInfo(UserId(1),
               ScalaDev,
               LocalDate.of(1970, JANUARY, 1),
               LocalDate.of(1970, JANUARY, 3).some),
      UserInfo(UserId(1),
               WebDeveloper,
               LocalDate.of(1971, JANUARY, 1),
               none[LocalDate])
    )

    val result = f.mapper.map(userInfos)

    result shouldBe empty
  }

  it should "return only first position after web developer when it is current" in {
    f =>
      val userInfos = Seq(
        UserInfo(UserId(1),
                 ScalaDev,
                 LocalDate.of(1970, JANUARY, 1),
                 LocalDate.of(1970, JANUARY, 3).some),
        UserInfo(UserId(1),
                 WebDeveloper,
                 LocalDate.of(1971, JANUARY, 1),
                 LocalDate.of(1972, FEBRUARY, 2).some),
        UserInfo(UserId(1),
                 PHPDev,
                 LocalDate.of(1972, FEBRUARY, 3),
                 none[LocalDate])
      )

      val result = f.mapper.map(userInfos)

      result shouldBe Seq(PHPDev)
  }

  it should "return only first position after web developer when he doesn't work" in {
    f =>
      val userInfos = Seq(
        UserInfo(UserId(1),
                 ScalaDev,
                 LocalDate.of(1970, JANUARY, 1),
                 LocalDate.of(1970, JANUARY, 3).some),
        UserInfo(UserId(1),
                 WebDeveloper,
                 LocalDate.of(1971, JANUARY, 1),
                 LocalDate.of(1972, FEBRUARY, 2).some),
        UserInfo(UserId(1),
                 PHPDev,
                 LocalDate.of(1972, FEBRUARY, 3),
                 LocalDate.of(1972, MARCH, 3).some)
      )

      val result = f.mapper.map(userInfos)

      result shouldBe Seq(PHPDev)
  }

  it should "return only first position after web developer when it is more than one" in {
    f =>
      val userInfos = Seq(
        UserInfo(UserId(1),
                 ScalaDev,
                 LocalDate.of(1970, JANUARY, 1),
                 LocalDate.of(1970, JANUARY, 3).some),
        UserInfo(UserId(1),
                 WebDeveloper,
                 LocalDate.of(1971, JANUARY, 1),
                 LocalDate.of(1972, FEBRUARY, 2).some),
        UserInfo(UserId(1),
                 PHPDev,
                 LocalDate.of(1972, FEBRUARY, 3),
                 LocalDate.of(1972, MARCH, 3).some),
        UserInfo(UserId(1),
                 J2EEDeveloper,
                 LocalDate.of(1972, MARCH, 4),
                 none[LocalDate])
      )

      val result = f.mapper.map(userInfos)

      result shouldBe Seq(PHPDev)
  }

  it should "return only first position except web developer" in { f =>
    val userInfos = Seq(
      UserInfo(UserId(1),
               ScalaDev,
               LocalDate.of(1970, JANUARY, 1),
               LocalDate.of(1970, JANUARY, 3).some),
      UserInfo(UserId(1),
               WebDeveloper,
               LocalDate.of(1971, JANUARY, 1),
               LocalDate.of(1972, FEBRUARY, 2).some),
      UserInfo(UserId(1),
               WebDeveloper,
               LocalDate.of(1972, FEBRUARY, 3),
               LocalDate.of(1972, MARCH, 3).some),
      UserInfo(UserId(1),
               J2EEDeveloper,
               LocalDate.of(1972, MARCH, 4),
               none[LocalDate])
    )

    val result = f.mapper.map(userInfos)

    result shouldBe Seq(J2EEDeveloper)
  }

  it should "skip when simultaneous work exists" in { f =>
    val userInfos = Seq(
      UserInfo(UserId(1),
               ScalaDev,
               LocalDate.of(1970, JANUARY, 1),
               LocalDate.of(1970, JANUARY, 3).some),
      UserInfo(UserId(1),
               WebDeveloper,
               LocalDate.of(1971, JANUARY, 1),
               LocalDate.of(1972, FEBRUARY, 2).some),
      UserInfo(UserId(1),
               PHPDev,
               LocalDate.of(1972, FEBRUARY, 2),
               LocalDate.of(1972, MARCH, 3).some),
      UserInfo(UserId(1),
               J2EEDeveloper,
               LocalDate.of(1972, MARCH, 4),
               none[LocalDate])
    )

    val result = f.mapper.map(userInfos)

    result shouldBe Seq(J2EEDeveloper)
  }
}
