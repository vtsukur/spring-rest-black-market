module.exports = function() {
    var views = require("./views/private.js");

    var AdsModel = require("./models/private.js").AdsModel;
    var ad = new AdsModel();

    var Controller = require("./controller.js");

    var controller = new Controller();

    new views.View({
        model: controller.resource,
        adModel: ad,
        controller: controller
    }).render();
};