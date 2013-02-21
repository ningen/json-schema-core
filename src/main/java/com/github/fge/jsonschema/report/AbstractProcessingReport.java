/*
 * Copyright (c) 2013, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.fge.jsonschema.report;

import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.google.common.collect.Iterators;

import java.util.Iterator;

/**
 * Base implementation of a processing report
 *
 * <p>This abstract class implements all the logic of a processing report. The
 * only method you need to implement is {@link #log(LogLevel,
 * ProcessingMessage)}, which will implement the actual logging of the message.
 * When entering this method, the message's log level will already have been set
 * correctly.</p>
 */
public abstract class AbstractProcessingReport
    implements ProcessingReport
{
    /**
     * The highest log level seen so far
     */
    private LogLevel currentLevel = LogLevel.DEBUG;

    /**
     * The log threshold
     */
    private final LogLevel logLevel;

    /**
     * The exception threshold
     */
    private final LogLevel exceptionThreshold;

    /**
     * Main constructor
     *
     * @param logLevel the log threshold for this report
     * @param exceptionThreshold the exception threshold for this report
     */
    protected AbstractProcessingReport(final LogLevel logLevel,
        final LogLevel exceptionThreshold)
    {
        this.logLevel = logLevel;
        this.exceptionThreshold = exceptionThreshold;
    }

    /**
     * Alternate constructor
     *
     * <p>This constructor calls {@link #AbstractProcessingReport(LogLevel,
     * LogLevel)} with {@link LogLevel#FATAL} as the second argument.</p>
     *
     * @param logLevel the log threshold
     */
    protected AbstractProcessingReport(final LogLevel logLevel)
    {
        this(logLevel, LogLevel.FATAL);
    }

    /**
     * Alternate constructor
     *
     * <p>This constructor calls {@link #AbstractProcessingReport(LogLevel,
     * LogLevel)} with {@link LogLevel#INFO} as the first argument and {@link
     * LogLevel#FATAL} as the second argument.</p>
     */
    protected AbstractProcessingReport()
    {
        this(LogLevel.INFO, LogLevel.FATAL);
    }

    @Override
    public final LogLevel getLogLevel()
    {
        return logLevel;
    }

    @Override
    public final LogLevel getExceptionThreshold()
    {
        return exceptionThreshold;
    }

    @Override
    public final void debug(final ProcessingMessage message)
        throws ProcessingException
    {
        dispatch(message.setLogLevel(LogLevel.DEBUG));
    }

    @Override
    public final void info(final ProcessingMessage message)
        throws ProcessingException
    {
        dispatch(message.setLogLevel(LogLevel.INFO));
    }

    @Override
    public final void warn(final ProcessingMessage message)
        throws ProcessingException
    {
        dispatch(message.setLogLevel(LogLevel.WARNING));
    }

    @Override
    public final void error(final ProcessingMessage message)
        throws ProcessingException
    {
        dispatch(message.setLogLevel(LogLevel.ERROR));
    }

    @Override
    public final boolean isSuccess()
    {
        return currentLevel.compareTo(LogLevel.ERROR) < 0;
    }

    /**
     * The only method to be implemented when extending this class
     *
     * <p>Note that the message's log level will have been correctly set. The
     * log level is passed as an argument for convenience.</p>
     *
     * @param level the level of the message
     * @param message the message itself
     */
    public abstract void log(final LogLevel level,
        final ProcessingMessage message);

    /**
     * Main dispatch method
     *
     * <p>All messages logged go through this method. According to the report
     * configuration, the message will either be ignored, logged or raise an
     * exception.</p>
     *
     * @param message the message to log
     * @throws ProcessingException the message's level and report configuration
     * require that an exception be thrown
     */
    protected final void dispatch(final ProcessingMessage message)
        throws ProcessingException
    {
        final LogLevel level = message.getLogLevel();

        if (level.compareTo(exceptionThreshold) >= 0)
            throw message.asException();
        if (level.compareTo(currentLevel) > 0)
            currentLevel = level;
        if (level.compareTo(logLevel) >= 0)
            log(level, message);
    }

    @Override
    public Iterator<ProcessingMessage> iterator()
    {
        return Iterators.emptyIterator();
    }

    @Override
    public final void mergeWith(final ProcessingReport other)
        throws ProcessingException
    {
        for (final ProcessingMessage message: other)
            dispatch(message);
    }
}
