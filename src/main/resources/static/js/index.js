var MainView = require("./public/views/MainView.js"),
    AdModel = require("./public/models/AdModel.js"),
    controller = require("./controller/controller.js");

new MainView({
    model: new AdModel(),
    controller: controller
}).render();
