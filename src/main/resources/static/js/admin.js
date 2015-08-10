module.exports = function() {
    var rootUri = '/',
        JsonHalAdapter = require("traverson-hal"),
        Backbone = require("backbone"),
        traverson = require("traverson");


    traverson.registerMediaType(JsonHalAdapter.mediaType,
        JsonHalAdapter);

    var api = traverson.from(rootUri);
    var views = require("./views/private.js");

    var AdsModel = require("./models/private.js");
    var ad = new AdsModel();
    var adsCollection = new Backbone.Collection();

    var Resource = require("./orderResource.js");

    var OrdersResource = Resource(api, AdsModel, adsCollection);
    var ordersResource = new OrdersResource();


    var Controller = require("./controller.js");
    var controllerConfig = {
        api: api,
        ad: ad,
        ordersResource: ordersResource
    };
    var controller = new Controller(controllerConfig);

    new views.View({ model: adsCollection, controller: controller }).render();


};