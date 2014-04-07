/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of both licenses is available at the root of this project (under the
 * names LGPL-3.0.txt and ASL-2.0.txt respectively) or, if you have a jar instead,
 * in the META-INF/ directory.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.jsonschema.core.load.uri;

import com.github.fge.Frozen;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfigurationBuilder;
import com.google.common.collect.ImmutableMap;

import java.net.URI;
import java.util.Map;

/**
 * Configuration for a {@link URITranslator}
 *
 * <p>This class configures all aspects of URI translation. In order to create
 * a new configuration, use {@link #newBuilder()}.</p>
 *
 * @see LoadingConfigurationBuilder#setURITranslatorConfiguration(URITranslatorConfiguration)
 */
public final class URITranslatorConfiguration
    implements Frozen<URITranslatorConfigurationBuilder>
{
    final URI namespace;
    final Map<URI, URI> pathRedirects;
    final Map<URI, URI> schemaRedirects;

    /**
     * Create a new configuration builder
     *
     * @return a new {@link URITranslatorConfigurationBuilder}
     */
    public static URITranslatorConfigurationBuilder newBuilder()
    {
        return new URITranslatorConfigurationBuilder();
    }

    /**
     * Create a default configuration
     *
     * <p>The default configuration has no namespace, no path redirections and
     * no schema redirections.</p>
     *
     * @return a default configuration
     */
    public static URITranslatorConfiguration byDefault()
    {
        return newBuilder().freeze();
    }

    URITranslatorConfiguration(final URITranslatorConfigurationBuilder builder)
    {
        namespace = builder.namespace;
        pathRedirects = ImmutableMap.copyOf(builder.pathRedirects.build());
        schemaRedirects = ImmutableMap.copyOf(builder.schemaRedirects.build());
    }

    @Override
    public URITranslatorConfigurationBuilder thaw()
    {
        return new URITranslatorConfigurationBuilder(this);
    }
}
