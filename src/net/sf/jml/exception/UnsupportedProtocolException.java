/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jml.exception;

import net.sf.jml.MsnProtocol;

/**
 * If the MSN server don't support the protocol.
 * 
 * @author Roger Chen
 */
public class UnsupportedProtocolException extends JmlException {

    private MsnProtocol[] protocol;

    public UnsupportedProtocolException(MsnProtocol[] procotol) {
        this.protocol = procotol;
    }

    public MsnProtocol[] getUnsupportedProtocol() {
        return protocol;
    }

    @Override
	public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("unsupported protocol : ");
        for (MsnProtocol aProtocol : protocol) {
            buffer.append(aProtocol).append(" ");
        }
        return buffer.toString();
    }

}