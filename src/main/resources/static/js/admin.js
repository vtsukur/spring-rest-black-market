var MainView = require("./private/views/MainView.js"),
    ViewModel = require("./private/models/ViewModel.js"),
    controller = require("./controller/controller.js");

new MainView({
    model: new ViewModel(),
    controller: controller
}).render();