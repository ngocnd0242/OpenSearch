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

package org.opensearch.rest.action.admin.indices;

import org.opensearch.common.settings.Settings;
import org.opensearch.common.util.concurrent.ThreadContext;
import org.opensearch.rest.RestRequest;
import org.opensearch.rest.RestStatus;
import org.opensearch.test.rest.FakeRestChannel;
import org.opensearch.test.rest.FakeRestRequest;
import org.opensearch.test.rest.RestActionTestCase;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.opensearch.rest.BaseRestHandler.INCLUDE_TYPE_NAME_PARAMETER;

public class RestPutMappingActionTests extends RestActionTestCase {

    @Before
    public void setUpAction() {
        controller().registerHandler(new RestPutMappingAction());
    }

    public void testIncludeTypeName() {
        Map<String, String> params = new HashMap<>();
        params.put(INCLUDE_TYPE_NAME_PARAMETER, randomFrom("true", "false"));
        RestRequest deprecatedRequest = new FakeRestRequest.Builder(xContentRegistry())
            .withMethod(RestRequest.Method.PUT)
            .withPath("/some_index/_mapping/")
            .withParams(params)
            .build();

        dispatchRequest(deprecatedRequest);
        assertWarnings(RestPutMappingAction.TYPES_DEPRECATION_MESSAGE);

        RestRequest validRequest = new FakeRestRequest.Builder(xContentRegistry())
            .withMethod(RestRequest.Method.PUT)
            .withPath("/some_index/_mapping")
            .build();
        dispatchRequest(validRequest);
    }

    public void testTypeInPath() {
        // Test that specifying a type while include_type_name is false
        // results in an illegal argument exception.
        RestRequest request = new FakeRestRequest.Builder(xContentRegistry())
            .withMethod(RestRequest.Method.PUT)
            .withPath("/some_index/_mapping/some_type")
            .build();

        FakeRestChannel channel = new FakeRestChannel(request, false, 1);
        ThreadContext threadContext = new ThreadContext(Settings.EMPTY);
        controller().dispatchRequest(request, channel, threadContext);

        assertEquals(1, channel.errors().get());
        assertEquals(RestStatus.BAD_REQUEST, channel.capturedResponse().status());
    }
}
