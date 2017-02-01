package common

import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.{FlatSpec, Matchers}

trait CryptoSpec extends FlatSpec with Matchers with TimeLimitedTests
