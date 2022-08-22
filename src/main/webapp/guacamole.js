// Get display div from document
const display = document.getElementById("display");

// Instantiate client, using an HTTP tunnel for communications.
const tunnel = new Guacamole.HTTPTunnel("tunnel");
const guac = new Guacamole.Client(tunnel);

// Add client to display div
display.appendChild(guac.getDisplay().getElement());

// Error handler
guac.onerror = function (error) {
    console.log('error:', error);
    alert(error);
};

// Connect
guac.connect();

// Disconnect on close
window.onunload = function () {
    guac.disconnect();
}

// Mouse
const mouse = new Guacamole.Mouse(guac.getDisplay().getElement());

mouse.onmousedown =
    mouse.onmouseup =
        mouse.onmousemove = function (mouseState) {
            guac.sendMouseState(mouseState);
        };

// Keyboard
const keyboard = new Guacamole.Keyboard(document);

keyboard.onkeydown = function (keysym) {
    guac.sendKeyEvent(1, keysym);
};

keyboard.onkeyup = function (keysym) {
    guac.sendKeyEvent(0, keysym);
};
