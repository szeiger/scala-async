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

package scala.async.internal

import java.util
import java.util.function.{Consumer, IntConsumer}

import scala.collection.JavaConverters.{asScalaIteratorConverter, iterableAsScalaIterableConverter}

// Set for StateIds, which are either small positive integers or -symbolID.
final class StateSet {
  private val bitSet = new java.util.BitSet()
  private val caseSet = new util.HashSet[Integer]()
  def +=(stateId: Int): Unit = if (storeInBitSet(stateId)) bitSet.set(stateId) else caseSet.add(stateId)
  def contains(stateId: Int): Boolean = if (storeInBitSet(stateId)) bitSet.get(stateId) else caseSet.contains(stateId)
  private def storeInBitSet(stateId: Int) = {
    stateId > 0 && stateId < 1024
  }
  def iterator: Iterator[Integer] = {
    bitSet.stream().iterator().asScala ++ caseSet.asScala.iterator
  }
  def foreach(f: IntConsumer): Unit = {
    bitSet.stream().forEach(f)
    caseSet.stream().forEach(new Consumer[Integer] {
      override def accept(value: Integer): Unit = f.accept(value)
    })
  }
}
