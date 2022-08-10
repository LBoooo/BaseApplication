package com.hinacle.base.util.logcat

enum class LogPriority(
  val priorityInt: Int
) {
  VERBOSE(2),
  DEBUG(3),
  INFO(4),
  WARN(5),
  ERROR(6),
  ASSERT(7);
}
