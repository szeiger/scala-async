/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.async
package run
package block1

import language.{reflectiveCalls, postfixOps}
import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration._
import scala.async.Async.{async, await}
import org.junit.Test


class Test1Class {

  import ExecutionContext.Implicits.global

  def m1(x: Int): Future[Int] = Future {
    x + 2
  }

  def m4(y: Int): Future[Int] = async {
    val f1 = m1(y)
    val f2 = m1(y + 2)
    val x1 = await(f1)
    val x2 = await(f2)
    x1 + x2
  }
}

class Block1Spec {

  @Test def `support a simple await`(): Unit = {
    val o = new Test1Class
    val fut = o.m4(10)
    val res = Await.result(fut, 2 seconds)
    res mustBe (26)
  }
}
