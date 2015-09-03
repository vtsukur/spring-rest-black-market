module.exports = function() {
    var views = require("./views/public.js");

    var AdsModel = require("./models/public.js").AdsModel;
    var ad = new AdsModel();

    var Controller = require("./controller.js");

    var controller = new Controller();

    new views.View({
        model: ad,
        controller: controller
    }).render();
};
