package expo.modules.backgrounddownloader

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DownloadConcurrencyTest {
  @Test
  fun oneAtATimeByDefault() {
    assertTrue(DownloadConcurrency.hasFreeSlot(running = 0, limit = 1))
    assertFalse(DownloadConcurrency.hasFreeSlot(running = 1, limit = 1))
  }

  @Test
  fun startsUpToTheLimit() {
    assertTrue(DownloadConcurrency.hasFreeSlot(running = 2, limit = 3))
    assertFalse(DownloadConcurrency.hasFreeSlot(running = 3, limit = 3))
  }

  // Lowering the limit stops nothing: the running downloads finish, and nothing starts until
  // fewer than the new limit are left.
  @Test
  fun waitsWhileMoreThanTheLimitAreStillRunning() {
    assertFalse(DownloadConcurrency.hasFreeSlot(running = 3, limit = 1))
  }

  // A zero or negative limit would stall the queue for good; it runs one at a time instead.
  @Test
  fun treatsALimitBelowOneAsOne() {
    assertTrue(DownloadConcurrency.hasFreeSlot(running = 0, limit = 0))
    assertFalse(DownloadConcurrency.hasFreeSlot(running = 1, limit = -2))
  }

  @Test
  fun combinesTheRunningDownloadsIntoOnePercent() {
    assertEquals(
      50,
      DownloadConcurrency.combinedPercent(listOf(100L to 100L, 0L to 100L))
    )
    assertEquals(
      25,
      DownloadConcurrency.combinedPercent(listOf(50L to 100L, 50L to 300L))
    )
  }

  // A transcoded download sends no Content-Length, so its total is 0 until it ends.
  @Test
  fun leavesADownloadOfUnknownSizeOutOfTheBar() {
    assertEquals(
      40,
      DownloadConcurrency.combinedPercent(listOf(40L to 100L, 5_000L to 0L))
    )
    assertEquals(0, DownloadConcurrency.combinedPercent(listOf(5_000L to 0L)))
    assertEquals(0, DownloadConcurrency.combinedPercent(emptyList()))
  }
}
