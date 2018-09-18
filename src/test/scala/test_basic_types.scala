/**
  * Created by Michał Krzysztof Feiler on 18.09.18.
  */

import testz.{Harness, PureHarness, assert}

final class BasicTypesTests {
  def tests[T](harness: Harness[T]) : T = {
    import harness._
    section (
      test("say 1 + 1 == 2") { () =>
        assert(1 + 1 == 2)
      },
      test("say 2 * 2 == 4") { () =>
        assert(2 * 2 == 4)
      }
    )
  }
}

val harness: Harness[PureHarness.Uses[Unit]] =
  PureHarness.makeFromPrinter((result, name) =>
    println(s"${name.reverse.mkString("[\"", "\"->\"", "\"]:")} $result")
  )
// harness: testz.Harness[testz.PureHarness.Uses[Unit]] = testz.ResourceHarness$$anon$1@44129689

(new BasicTypesTests()).tests(harness)((), Nil).print()
// ["say 1 + 1 == 2"]: Succeed
// ["say 2 * 2 == 4"]: Succeed
