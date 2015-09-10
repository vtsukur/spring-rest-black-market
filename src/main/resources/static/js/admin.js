var MainView = require("./private/views/MainView.js");

var ViewModel = require("./private/models/ViewModel");

var controller = require("./controller/controller.js");

new MainView({
    model: new ViewModel(),
    controller: controller
}).render();