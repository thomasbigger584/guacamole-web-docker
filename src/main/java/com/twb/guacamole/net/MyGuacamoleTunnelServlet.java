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

public class MyGuacamoleTunnelServlet extends GuacamoleHTTPTunnelServlet {
    private static final String HEADER_CONNECTION_PROTOCOL = "X-Connection-Protocol";
    private static final String HEADER_CONNECTION_HOSTNAME = "X-Connection-Hostname";
    private static final String HEADER_CONNECTION_PORT = "X-Connection-Port";
    private static final String HEADER_CONNECTION_USER = "X-Connection-User";
    private static final String HEADER_CONNECTION_PASSWORD = "X-Connection-Password";
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
