package ThreeManChess.Engine

import eu.timepit.refined
import refined.api.Refined

import refined.numeric.Positive
import refined.collection.NonEmpty

import refined.auto._

/**
  * Created by Michał Krzysztof Feiler on 30.08.18.
  */

//class SegmentHalf(val next: Boolean) {
//  def first() : Boolean = !next
//  def second() : Boolean = next
//}
//
//object SegmentHalf {
//  val First = SegmentHalf(false)
//  val Second = SegmentHalf(true)
//}
//
//class SegmentQuarter(val inHalf: SegmentHalf, val whichHalf: SegmentHalf)
//class SegmentEight(val inQuarter: SegmentQuarter, val whichHalf: SegmentHalf)

object Pos {
  type SegmentHalf = Int Refined Interval.Closed[W.`0`.T,W.`1`.T]
  object SegmentHalf {
    val First : SegmentHalf = 0
    val Second : SegmentHalf = 1
  }
  type SegmentQuarter = Int Refined Interval.Closed[W.`0`.T,W.`3`.T]
  type SegmentEight = Int Refined Interval.Closed[W.`0`.T,W.`7`.T]
  type Rank = Int Refined Interval.Closed[W.`0`.T,W.`5`.T]
  type File = Int Refined Interval.Closed[W.`0`.T,W.`23`.T]
}

class Pos(val rank: Rank, val file: File) {

}
