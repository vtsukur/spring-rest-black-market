var View = require("./views/public.js");

var AdsModel = require("./models/public.js");

var controller = require("./controller/controller.js");

new View({
    model: new AdsModel(),
    controller: controller
}).render();
