var View = require("./views/private.js");

var AdsModel = require("./models/private.js");

var Controller = require("./controller/controller.js");

var controller = new Controller();

new View({
    model: controller.resource,
    adModel: new AdsModel(),
    controller: controller
}).render();