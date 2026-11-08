export const DOWNLOAD_TICKS_PER_SECOND = 10_000_000;
export const DOWNLOAD_BITS_PER_BYTE = 8;
// Leave 10% room for the container and metadata in transcoded estimates.
export const DOWNLOAD_SIZE_OVERHEAD = 1.1;

// Names of what a download writes next to its video. The writer and the cleanup both go
// through the builders in providers/Downloads/utils.ts, so a rename cannot leave the cleanup
// looking for the old name.
export const DOWNLOAD_SUBTITLE_INFIX = "_subtitle_";
export const DOWNLOAD_TRICKPLAY_DIR_SUFFIX = "_trickplay";
// Android stages the transfer in `<destination>.part` and renames it once it is complete
// (OkHttpDownloadManager.kt). The native side owns the value; this mirrors it.
export const DOWNLOAD_PART_FILE_SUFFIX = ".part";

// A failed download's toast says why and what to do in two or three lines, more than the four
// seconds the Toaster gives a toast leave time to read.
export const DOWNLOAD_FAILURE_TOAST_DURATION_MS = 8000;

// How many downloads the native queue runs at once. One by default: a transcoded download makes
// the server transcode the whole file, and several at once can saturate a small server. Users on
// a server with headroom raise it in the storage settings.
export const DEFAULT_MAX_CONCURRENT_DOWNLOADS = 1;
export const MIN_CONCURRENT_DOWNLOADS = 1;
// Kept well under the six connections per host an iOS background URLSession opens, so a burst of
// downloads never starves the rest of the app's requests to the same server.
export const MAX_CONCURRENT_DOWNLOADS = 3;
