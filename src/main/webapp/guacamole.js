const display = document.getElementById("display");

const connectionProtocol = "vnc";
const connectionHostname = "192.168.1.88";
const connectionPort = "5900";
const connectionUser = "";
const connectionPassword = "password";
const guacdHostname = "guacd";
const guacdPort = "4822";

const headers = {
    "X-Connection-Protocol": connectionProtocol,
    "X-Connection-Hostname": connectionHostname,
    "X-Connection-Port": connectionPort,
    "X-Connection-User": connectionUser,
    "X-Connection-Password": connectionPassword,
    "X-Guacd-Hostname": guacdHostname,
    "X-Guacd-Port": guacdPort,
};

// Here is a more complete example of the tunnel usage.
// https://github.com/apache/incubator-datalab/blob/4b1ec16844fefbf857071738ee94d6923fe4e447/services/self-service/src/main/resources/webapp/src/app/webterminal/webterminal.component.ts#L63
// https://github.com/padarom/guacamole-common-js/blob/main/guacamole-common-js/src/main/webapp/modules/Tunnel.js#L251
const tunnel = new Guacamole.HTTPTunnel("tunnel", false, headers);
const guac = new Guacamole.Client(tunnel);

guac.onerror = function (error) {
    guac.disconnect();
    alert(JSON.stringify(error));
};

display.appendChild(guac.getDisplay().getElement());

guac.connect();
window.onunload = function () {
    guac.disconnect();
}

const mouse = new Guacamole.Mouse(guac.getDisplay().getElement());
mouse.onmousedown =
    mouse.onmouseup =
        mouse.onmousemove = function (mouseState) {
            guac.sendMouseState(mouseState);
        };

const keyboard = new Guacamole.Keyboard(document);
keyboard.onkeydown = function (keysym) {
    guac.sendKeyEvent(1, keysym);
};
keyboard.onkeyup = function (keysym) {
    guac.sendKeyEvent(0, keysym);
};