/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2023 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.addon.postman;

import java.io.IOException;
import org.apache.commons.httpclient.URI;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

public class Requestor {
    private int initiator;
    private HistoryPersister listener;
    private HttpSender sender;

    public Requestor(int initiator, HistoryPersister listener) {
        this.initiator = initiator;
        this.listener = listener;
        this.sender = new HttpSender(initiator);
    }

    public String getResponseBody(URI uri) throws IOException {
        HttpMessage httpRequest = new HttpMessage(uri);
        httpRequest.getRequestHeader().setHeader("Accept", "application/json,*/*");

        sender.sendAndReceive(httpRequest, true);
        listener.handleMessage(httpRequest, initiator);

        return httpRequest.getResponseBody().toString();
    }
}
