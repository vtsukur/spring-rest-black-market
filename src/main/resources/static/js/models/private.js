var Backbone = require("backbone"),
    $ = require("jquery");
    require("backbone-relational");
    require("backbone-relational-hal");

var config = require("../config.js"),
    rootUri = config.rootUri,
    prefix = config.prefix,
    JsonHalAdapter = require("traverson-hal"),
    traverson = require("traverson");

traverson.registerMediaType(JsonHalAdapter.mediaType,
    JsonHalAdapter);

var api = traverson.from(rootUri);
var resource = require("../controller/resource.js");

var AdsModel = Backbone.RelationalHalResource.extend({
    initialize: function () {
        resource.getRootHal(function (halUrl) {
            this.halUrl = halUrl;
            $.extend(this.defaults, { _links: { self: { href: halUrl }}});
        }.bind(this));
        resource.getCurrentUser(function (user) {
            this.set("user", user);
        }.bind(this));
    },
    defaults: {
        location: {
            city: null,
            area: null
        },
        amount: null,
        type: "SELL",
        publishedAt: null,
        rate: null,
        currency: "USD",
        comment: null,
        phoneNumber: null
    }
});

module.exports = AdsModel;