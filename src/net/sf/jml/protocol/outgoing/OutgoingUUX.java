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
package net.sf.jml.protocol.outgoing;

import net.sf.jml.*;
import net.sf.jml.exception.UnsupportedProtocolException;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.util.GUID;
import net.sf.jml.util.StringUtils;

/**
 * Set personal message and current media.
 * <p>
 * Supported Protocol: MSNP11
 * <p>
 * Syntax: UUX trId msgLen\r\n content
 *
 * @author Roger Chen
 */
public class OutgoingUUX extends MsnOutgoingMessage {

    private String personalMessage = "";
    private String currentMedia = "";
    private String machineGuid = null;

    public OutgoingUUX(MsnProtocol protocol) {
        super(protocol);
        if (protocol.before(MsnProtocol.MSNP11)){
            throw new UnsupportedProtocolException(new MsnProtocol[] { protocol }) ;
        }
        setCommand("UUX");
    }

    @Override
    protected boolean isSupportChunkData() {
        return true;
    }

    public void setPersonalMessage(String message) {
        this.personalMessage = message;
        buildData();
    }

    public void setCurrentMedia(String currentMedia) {
        this.currentMedia = currentMedia;
        buildData();
    }

    public void setMachineGuid(String machineGuid) {
        this.machineGuid = machineGuid;
        buildData();
    }

    public void setEndpoint(MsnClientId clientId)
    {
//        if (protocol >= 16)
//              MSN_CLIENT_ID_CAPABILITIES, MSN_CLIENT_ID_EXT_CAPS);
//        else
//            MSN_CLIENT_ID_CAPABILITIES);
        //todo fix it
//        setChunkData("<EndpointData><Capabilities>" + clientId.toString() + ":00</Capabilities></EndpointData>");
        setChunkData("<EndpointData><Capabilities>262180:00</Capabilities></EndpointData>");

    }

    public void setPrivateEndpoint()
    {
        // fix getting state
        setChunkData("<PrivateEndpointData><EpName>Java-JML</EpName><Idle>false</Idle><ClientType>1</ClientType><State>NLN</State></PrivateEndpointData>");
//        /* ClientType info (from amsn guys):
//            0: None
//            1: Computer
//            2: Website
//            3: Mobile / none
//            4: Xbox / phone /mobile
//            9: MsnGroup
//            32: Email member, currently Yahoo!
//        */
    }

    public void setCurrentMedia(String title, String artist, String album, GUID contentId) {
        String split = "\\0";
        StringBuffer buffer = new StringBuffer();
        buffer.append(split).append("Music");
        buffer.append(split).append("1");
        buffer.append(split).append("{0} - {1}");
        buffer.append(split).append(title == null ? "" : title);
        buffer.append(split).append(artist == null ? "" : artist);
        buffer.append(split).append(album == null ? "" : album);
        // See ASF special "Metadata Library Object--WM/WMContentID"
        buffer.append(split).append(
                contentId == null ? "" : "{" + contentId + "}");
        buffer.append(split);
        setCurrentMedia(buffer.toString());
    }

    private void buildData() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<Data>");
        if (this.personalMessage != null) {
            buffer.append("<PSM>");
            buffer.append(StringUtils.xmlEscaping(personalMessage));
            buffer.append("</PSM>");
        }
        if (this.currentMedia != null) {
            buffer.append("<CurrentMedia>");
            buffer.append(StringUtils.xmlEscaping(currentMedia));
            buffer.append("</CurrentMedia>");
        }
        if (this.machineGuid != null) {
            buffer.append("<MachineGuid>");
            buffer.append(StringUtils.xmlEscaping(machineGuid));
            buffer.append("</MachineGuid>");
        }
//        if (protocol.after(MsnProtocol.MSNP15))
//        {
//            buffer.append("</DDP>");
//        }
        buffer.append("</Data>");
        setChunkData(buffer.toString());
    }
}