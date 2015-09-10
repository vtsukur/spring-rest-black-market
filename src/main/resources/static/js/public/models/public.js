var Backbone = require("backbone");
require("backbone-relational");
require("backbone-relational-hal");

var rootUri = "/",
    prefix = "currency-black-market:",
    JsonHalAdapter = require("traverson-hal"),
    traverson = require("traverson");


traverson.registerMediaType(JsonHalAdapter.mediaType,
    JsonHalAdapter);

var api = traverson.from(rootUri);
var resource = require("../../controller/resource.js");

var AdsModel = Backbone.RelationalHalResource.extend({
    initialize: function () {
        resource.getRootHal(function (halUrl) {
            this.halUrl = halUrl;
            this.fetch();
        }.bind(this));
    }
});

module.exports = AdsModel;