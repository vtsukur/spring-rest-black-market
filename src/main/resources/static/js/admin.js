module.exports = function() {
    var Backbone = require("backbone");

    var views = require("./views/private.js");

    var AdsModel = require("./models/private.js").AdsModel;
    var ad = new AdsModel();

    var adsCollection = new Backbone.Collection();


    var Controller = require("./controller.js");
    var controllerConfig = {
        ad: ad,
        adsCollection: adsCollection,
        Model: AdsModel
    };
    var controller = new Controller(controllerConfig);

};