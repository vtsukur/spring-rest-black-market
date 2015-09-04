var View = require("./views/public.js");

var AdsModel = require("./models/public.js");

var Controller = require("./controller/controller.js");

new View({
    model: new AdsModel(),
    controller: new Controller()
}).render();
