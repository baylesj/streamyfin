package expo.modules.backgrounddownloader

/**
 * The queue policy, kept free of Android types so it runs in plain JVM tests.
 *
 * The module runs up to `limit` downloads at once and starts queued ones as slots free up. The
 * foreground notification has one progress bar, so it shows the running downloads together.
 */
internal object DownloadConcurrency {
  /** Whether another queued download may start while `running` are in flight. */
  fun hasFreeSlot(running: Int, limit: Int): Boolean = running < limit.coerceAtLeast(1)

  /**
   * Combined percent of the running downloads, from each one's (bytes written, total bytes).
   * A download whose size is still unknown counts as 0% of nothing, so it neither holds the bar
   * back nor pushes it past 100.
   */
  fun combinedPercent(transfers: Collection<Pair<Long, Long>>): Int {
    val known = transfers.filter { it.second > 0 }
    val total = known.sumOf { it.second }
    if (total <= 0) return 0
    val written = known.sumOf { it.first.coerceIn(0, it.second) }
    return ((written * 100) / total).toInt()
  }
}
