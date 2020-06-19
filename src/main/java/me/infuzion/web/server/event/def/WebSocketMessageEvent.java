/*
 * Copyright 2020 Srikavin Ramkumar
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

package me.infuzion.web.server.event.def;

import me.infuzion.web.server.event.reflect.param.CanSetBody;
import me.infuzion.web.server.event.reflect.param.HasBody;
import me.infuzion.web.server.http.parser.BodyData;
import me.infuzion.web.server.network.websocket.WebsocketFrameOpcodes;
import me.infuzion.web.server.websocket.WebsocketClient;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public abstract class WebSocketMessageEvent extends WebSocketEvent implements HasBody, CanSetBody {
    private final WebsocketFrameOpcodes opcode;

    private final ByteBuffer rawBuffer;

    public WebSocketMessageEvent(WebsocketClient client, WebsocketFrameOpcodes opcode, ByteBuffer rawBuffer) {
        super(client);
        this.opcode = opcode;
        this.rawBuffer = rawBuffer.rewind().asReadOnlyBuffer();
    }

    @Override
    public ByteBuffer getRawRequestData() {
        return rawBuffer.rewind();
    }

    @Override
    public String getRequestData() {
        return StandardCharsets.UTF_8.decode(rawBuffer).toString();
    }

    @Override
    public BodyData getBodyData() {
        //TODO: implement properly
        return new BodyData(Collections.emptyMap());
    }

    @Override
    public void setResponseBody(String body) {
        getClient().send(body);
    }

    @Override
    public void setBody(ByteBuffer body) {
        getClient().sendBinary(body.rewind());
    }

    public WebsocketFrameOpcodes getOpcode() {
        return opcode;
    }
}