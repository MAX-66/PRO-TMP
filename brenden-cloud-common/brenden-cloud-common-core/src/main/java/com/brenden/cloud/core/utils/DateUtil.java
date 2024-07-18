package com.brenden.cloud.core.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/7/18
 */
public class DateUtil {

    // 私有构造函数防止实例化
    private DateUtil() {
    }

    // 日期时间格式常量
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";

    // 日期时间格式化器
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * 获取自定义的 DateTimeFormatter
     *
     * @param pattern 格式化模式
     * @return DateTimeFormatter
     */
    public static DateTimeFormatter getFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * 将 LocalDateTime 格式化为字符串
     *
     * @param dateTime 要格式化的 LocalDateTime
     * @return 格式化后的日期时间字符串
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 将 LocalDateTime 按自定义格式格式化为字符串
     *
     * @param dateTime 要格式化的 LocalDateTime
     * @param pattern 格式化模式
     * @return 格式化后的日期时间字符串
     */
    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(getFormatter(pattern));
    }

    /**
     * 将字符串解析为 LocalDateTime
     *
     * @param dateTimeString 要解析的日期时间字符串
     * @return 解析后的 LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

    /**
     * 将字符串按自定义格式解析为 LocalDateTime
     *
     * @param dateTimeString 要解析的日期时间字符串
     * @param pattern 格式化模式
     * @return 解析后的 LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeString, String pattern) {
        return LocalDateTime.parse(dateTimeString, getFormatter(pattern));
    }

    /**
     * 将 LocalDate 格式化为字符串
     *
     * @param date 要格式化的 LocalDate
     * @return 格式化后的日期字符串
     */
    public static String formatLocalDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * 将 LocalDate 按自定义格式格式化为字符串
     *
     * @param date 要格式化的 LocalDate
     * @param pattern 格式化模式
     * @return 格式化后的日期字符串
     */
    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(getFormatter(pattern));
    }

    /**
     * 将字符串解析为 LocalDate
     *
     * @param dateString 要解析的日期字符串
     * @return 解析后的 LocalDate
     */
    public static LocalDate parseLocalDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * 将字符串按自定义格式解析为 LocalDate
     *
     * @param dateString 要解析的日期字符串
     * @param pattern 格式化模式
     * @return 解析后的 LocalDate
     */
    public static LocalDate parseLocalDate(String dateString, String pattern) {
        return LocalDate.parse(dateString, getFormatter(pattern));
    }

    /**
     * 获取当前日期时间的字符串表示
     *
     * @return 当前日期时间字符串
     */
    public static String getCurrentDateTimeString() {
        return formatLocalDateTime(LocalDateTime.now());
    }

    /**
     * 获取当前日期的字符串表示
     *
     * @return 当前日期字符串
     */
    public static String getCurrentDateString() {
        return formatLocalDate(LocalDate.now());
    }

    /**
     * 计算两个 LocalDateTime 之间的差异（天数）
     *
     * @param start 开始的 LocalDateTime
     * @param end   结束的 LocalDateTime
     * @return 天数差异
     */
    public static Long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 计算两个 LocalDate 之间的差异（天数）
     *
     * @param start 开始的 LocalDate
     * @param end   结束的 LocalDate
     * @return 天数差异
     */
    public static Long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 增加或减少 LocalDateTime 的天数
     *
     * @param dateTime 要操作的 LocalDateTime
     * @param days     要增加或减少的天数
     * @return 增加或减少天数后的 LocalDateTime
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }

    /**
     * 增加或减少 LocalDate 的天数
     *
     * @param date 要操作的 LocalDate
     * @param days 要增加或减少的天数
     * @return 增加或减少天数后的 LocalDate
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date.plusDays(days);
    }

    /**
     * 获取当前时间，并格式化为字符串
     *
     * @return 当前日期时间字符串
     */
    public static String getFormattedCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DATE_TIME_FORMATTER);
    }

    /**
     * 获取当前日期，并格式化为字符串
     *
     * @return 当前日期字符串
     */
    public static String getFormattedCurrentDate() {
        LocalDate today = LocalDate.now();
        return today.format(DATE_FORMATTER);
    }

    /**
     * 将 LocalDateTime 转换为 LocalDate
     *
     * @param dateTime 要转换的 LocalDateTime
     * @return 转换后的 LocalDate
     */
    public static LocalDate toLocalDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    /**
     * 将 LocalDate 转换为 LocalDateTime
     *
     * @param date 要转换的 LocalDate
     * @return 转换后的 LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * 将时间戳转换为 LocalDateTime
     *
     * @param timestamp 时间戳（毫秒）
     * @return 转换后的 LocalDateTime
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * 将 LocalDateTime 转换为时间戳
     *
     * @param dateTime 要转换的 LocalDateTime
     * @return 转换后的时间戳（毫秒）
     */
    public static long localDateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 将时间戳转换为 LocalDate
     *
     * @param timestamp 时间戳（毫秒）
     * @return 转换后的 LocalDate
     */
    public static LocalDate timestampToLocalDate(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将 LocalDate 转换为时间戳
     *
     * @param date 要转换的 LocalDate
     * @return 转换后的时间戳（毫秒）
     */
    public static long localDateToTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}

