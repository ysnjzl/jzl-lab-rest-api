package com.lab.api.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TRACE DEBUG INFO WARN ERROR
 */
public final class LabApiLogger {

  private LabApiLogger() {}

  private static final Logger LOGGER = LoggerFactory.getLogger(LabApiLogger.class);

  @SuppressWarnings("rawtypes")
  public static void trace(Class className, String message, Throwable throwable) {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("[" + className.getSimpleName() + "] " + message, throwable);
    }
  }

  @SuppressWarnings("rawtypes")
  public static void debug(Class className, String message, Throwable throwable) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[" + className.getSimpleName() + "] " + message, throwable);
    }
  }

  @SuppressWarnings("rawtypes")
  public static void info(Class className, String message, Throwable throwable) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("[" + className.getSimpleName() + "] " + message, throwable);
    }
  }

  @SuppressWarnings("rawtypes")
  public static void warn(Class className, String message, Throwable throwable) {
    if (LOGGER.isWarnEnabled()) {
      LOGGER.warn("[" + className.getSimpleName() + "] " + message, throwable);
    }
  }

  @SuppressWarnings("rawtypes")
  public static void error(Class className, String message, Throwable throwable) {
    if (LOGGER.isErrorEnabled()) {
      LOGGER.error("[" + className.getSimpleName() + "] " + message, throwable);
    }
  }
}

