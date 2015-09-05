var View = require("./views/private.js");

var models = require("./models/private.js"),
    AdsModel = models.AdsModel,
    ViewModel = models.ViewModel;

var controller = require("./controller/controller.js");

new View({
    model: new ViewModel(),
    adModel: new AdsModel(),
    controller: controller
}).render();