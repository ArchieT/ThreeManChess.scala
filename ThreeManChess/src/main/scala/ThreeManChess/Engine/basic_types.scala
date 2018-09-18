package ThreeManChess.Engine

import eu.timepit.refined
import refined.api.Refined
import refined.numeric.Positive
import refined.collection.NonEmpty
import refined.auto._

import scala.None
import scalaz._

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

//sealed trait Color
//case object White extends Color
//case object Gray extends Color
//case object Black extends Color
//object Color {
//  def fromInt(i:Int )
//}

object Color {
  type IntColorValue = Int Refined Interval.Closed[W.`0`.T,W.`2`.T]
}
final class Color(val intval: IntColorValue) {

}

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
  def opposite(a : File) : File = a<12 ? f+12 : f-12
  def plus(a : File, b : Int Refined NonNegative) : File = (a+b)%24
  def minus(a : File, b : Int Refined NonNegative) : File = Math.floorMod(a-b, 24)
  def signSum(s : FilewiseDirection, a : File, b : Int Refined NonNegative) : File = s match {
    case Pluswards => plus(a, b)
    case Minuswards => minus(a, b)
  }

  type ColorSegm = Color
}

final class Pos(val rank: Rank, val file: File) {
  def segmColor() : ColorSegm = Color(file/8)
  def segmFile() : SegmentEight = file%3
}

trait Reversible[A <: Reversible[A]] { this: A =>
  def rever() : A
}
sealed trait LinearDirection {
  def addOneTo(from : Pos) : Option[Pos]
}
sealed trait StraightDirection extends LinearDirection
sealed trait RankwiseDirection extends StraightDirection, Reversible[RankwiseDirection]
case object Inwards extends RankwiseDirection {
  def rever = Outwards
  def addOneTo(from: Pos) : Option[Pos] =
    if (from.rank==5) Some(Pos(5, from.file.opposite)) else Some(Pos(from.rank+1, from.file))
}
case object Outwards extends RankwiseDirection {
  def rever = Inwards
  def addOneTo(from: Pos) : Option[Pos] =
    if (from.rank==0) None else Some(Pos(from.rank-1, from.file))
}
sealed trait FilewiseDirection extends StraightDirection, Reversible[FilewiseDirection]
case object Pluswards extends FilewiseDirection {
  def rever = Minuswards
  def addOneTo(from: Pos) : Option[Pos] = Some(Pos(from.rank, Pos.plus(from.file, 1)))
}
case object Minuswards extends FilewiseDirection {
  def rever = Pluswards
  def addOneTo(from: Pos) : Option[Pos] = Some(Pos(from.rank, Pos.minus(from.file, 1)))
}
case final class DiagonalDirection(val rankwise : RankwiseDirection,
                                   val filewise: FilewiseDirection) {
  def rever = DiagonalDirection(rankwise.rever, filewise.rever)
  def addOneTo(from: Pos) : Option[Pos] = rankwise match {
    case Inwards => from.rank match {
      case 5 => Some(Pos(5, filewise match {
        case Pluswards => Pos.minus(from.file, 10)
        case Minuswards => Pos.plus(from.file, 10)
      }))
      case _ => Some(Pos(from.rank+1, Pos.signSum(filewise, from.file, 1)))
    }
    case Outwards => from.rank match {
      case 0 => None
      case _ => Some(Pos(from.rank-1), Pos.signSum(filewise, from.file, 1))
    }
  }
}

trait Vec {
  def addTo(from : Pos) : Option[Pos]
}
class LinearVec[DirectionClass <: LinearDirection](val direction : DirectionClass,
                                                   val distance : Int Refined Positive)
  extends Vec {
  def tailRankInvol : Option[\/[Rank => LinearVec[DirectionClass], LinearVec[DirectionClass]]] =
    if (distance==1) None
    else if (DirectionClass==FilewiseDirection)
      Some(\/-(LinearVec(direction, distance-1)))
    else if (DirectionClass==RankwiseDirection)
      direction match {
        Inwards => Some(-\/((r:Rank) => r match {
          5 => LinearVec(direction.rever, distance-1)
          _ => LinearVec(direction, distance-1)
        }))
        Outwards => Some(\/-(LinearVec(direction, distance-1)))
      }
    else if(DirectionClass==DiagonalDirection)
      direction.rankwise match {
        Inwards => Some(-\/((r:Rank) => r match {
          5 => LinearVec(DiagonalDirection(direction.rankwise.rever, direction.filewise), distance-1)
          _ => LinearVec(direction, distance-1)
        }))
        Outwards => Some(\/-(LinearVec(direction, distance-1)))
      }
  def tail(r:Rank) : Option[LinearVec[DirectionClass]] = tailRankInvol.map(x =>
    x.leftMap(f => f(r)))


  def addTo(from: Pos) : Option[Pos] =
}

//trait RankInvolVec[A <: LinearVec[RankwiseDirection] | A<: LinearVec[DiagonalDirection]] {
//  def tail(from: Pos) : Option[Rank => Pos]
//}

sealed trait Orientation {
  val perpendicular : Orientation
}
case object Rankwise extends Orientation {
  val perpendicular = Filewise
}
case object Filewise extends Orientation {
  val perpendicular = Rankwise
}

