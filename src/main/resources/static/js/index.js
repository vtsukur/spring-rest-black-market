var MainView = require("./public/views/public.js");

var AdsModel = require("./public/models/public.js");

var controller = require("./controller/controller.js");

new MainView({
    model: new AdsModel(),
    controller: controller
}).render();
