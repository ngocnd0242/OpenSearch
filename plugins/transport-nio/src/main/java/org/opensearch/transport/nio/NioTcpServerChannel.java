/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.transport.nio;

import org.opensearch.action.ActionListener;
import org.opensearch.nio.NioServerSocketChannel;
import org.opensearch.transport.TcpServerChannel;

import java.nio.channels.ServerSocketChannel;

/**
 * This is an implementation of {@link NioServerSocketChannel} that adheres to the {@link TcpServerChannel}
 * interface. As it is a server socket, setting SO_LINGER and sending messages is not supported.
 */
public class NioTcpServerChannel extends NioServerSocketChannel implements TcpServerChannel {

    public NioTcpServerChannel(ServerSocketChannel socketChannel) {
        super(socketChannel);
    }

    public void close() {
        getContext().closeChannel();
    }

    @Override
    public void addCloseListener(ActionListener<Void> listener) {
        addCloseListener(ActionListener.toBiConsumer(listener));
    }

    @Override
    public String toString() {
        return "TcpNioServerSocketChannel{" +
            "localAddress=" + getLocalAddress() +
            '}';
    }
}
