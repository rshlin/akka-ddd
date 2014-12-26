package pl.newicom.dddd.test.dummy

import akka.actor.Props
import pl.newicom.dddd.actor.PassivationConfig
import pl.newicom.dddd.aggregate.AggregateRootActorFactory
import pl.newicom.dddd.eventhandling.LocalPublisher
import pl.newicom.dddd.test.dummy.DummyAggregateRoot._
import pl.newicom.dddd.test.support.OfficeSpec
import pl.newicom.dddd.test.support.TestConfig.testSystem
import DummyOfficeSpec._
import scala.concurrent.duration.{Duration, _}

object DummyOfficeSpec {
  implicit def actorFactory(implicit it: Duration = 1.minute): AggregateRootActorFactory[DummyAggregateRoot] =
    new AggregateRootActorFactory[DummyAggregateRoot] {
      override def props(pc: PassivationConfig): Props = Props(new DummyAggregateRoot with LocalPublisher)
      override def inactivityTimeout: Duration = it
    }
}

class DummyOfficeSpec extends OfficeSpec[DummyAggregateRoot](testSystem) {

  def dummyOffice = officeUnderTest

  "Dummy office" should {
    "handle Create command" in {
      whenCommand(
        CreateDummy(aggregateId)
      )
      .expectEvent(
        DummyCreated(aggregateId, version = 0)
      )
    }
  }

  "Dummy office" should {
    "handle Update command" in {
      givenCommand(
        CreateDummy(aggregateId)
      )
      .whenCommand(
        UpdateDummy(aggregateId)
      )
      .expectEvent(
        DummyUpdated(aggregateId, version = 1)
      )
    }
  }

  "Dummy office" should {
    "handle subsequent Update command" in {
      givenCommands(
        CreateDummy(aggregateId),
        UpdateDummy(aggregateId)
      )
      .whenCommand(
        UpdateDummy(aggregateId)
      )
      .expectEvent(
        DummyUpdated(aggregateId, version = 2)
      )
    }
  }

  "Dummy office" should {
    "reject InvalidUpdate command" in {
      givenCommand(
        CreateDummy(aggregateId)
      )
      .whenCommand(
        InvalidUpdateDummy(aggregateId)
      )
      .expectException[RuntimeException]("Update rejected")
    }
  }

}
