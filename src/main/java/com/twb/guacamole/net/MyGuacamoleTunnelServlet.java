/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.twb.guacamole.net;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.servlet.GuacamoleHTTPTunnelServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * Simple tunnel example with hard-coded configuration parameters.
 */
public class MyGuacamoleTunnelServlet extends GuacamoleHTTPTunnelServlet {

    private final Logger logger = Logger.getLogger(MyGuacamoleTunnelServlet.class.getSimpleName());


    // Connection Headers
    private static final String HEADER_CONNECTION_PROTOCOL = "X-Connection-Protocol";
    private static final String HEADER_CONNECTION_HOSTNAME = "X-Connection-Hostname";
    private static final String HEADER_CONNECTION_PORT = "X-Connection-Port";
    private static final String HEADER_CONNECTION_USER = "X-Connection-User";
    private static final String HEADER_CONNECTION_PASSWORD = "X-Connection-Password";

    //Guacd Headers
    private static final String HEADER_GUACD_HOSTNAME = "X-Guacd-Hostname";
    private static final String HEADER_GUACD_PORT = "X-Guacd-Port";

    @Override
    protected GuacamoleTunnel doConnect(HttpServletRequest request) throws GuacamoleException {
        // VNC connection information
        // parameters found here: https://guacamole.apache.org/doc/0.8.3/gug/configuring-guacamole.html
        GuacamoleConfiguration config = new GuacamoleConfiguration();
        config.setProtocol(request.getHeader(HEADER_CONNECTION_PROTOCOL));
        config.setParameter("hostname", request.getHeader(HEADER_CONNECTION_HOSTNAME));
        config.setParameter("port", request.getHeader(HEADER_CONNECTION_PORT));
        config.setParameter("username", request.getHeader(HEADER_CONNECTION_USER));
        config.setParameter("password", request.getHeader(HEADER_CONNECTION_PASSWORD));

        // Connect to guacd, proxying a connection to the VNC server above
        String guacdHostname = request.getHeader(HEADER_GUACD_HOSTNAME);
        int guacdPort = Integer.parseInt(request.getHeader(HEADER_GUACD_PORT));
        InetGuacamoleSocket inetGuacamoleSocket = new InetGuacamoleSocket(guacdHostname, guacdPort);
        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(inetGuacamoleSocket, config);
        return new SimpleGuacamoleTunnel(socket);
    }
}
