var Backbone = require("backbone");
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

var AdsModel = Backbone.RelationalHalResource.extend({
    initialize: function () {
        var self = this;
        api.jsonHal()
            .follow(prefix + "ads")
            .getUri(function (err, uri) {
                if (err) {
                    console.log(err);
                    return;
                }
                self.halUrl = uri;
                $.extend(self.defaults, { _links: { self: { href: uri }}});
            });
        api.jsonHal()
            .follow(prefix + "users", "search", prefix + "current-user")
            .getResource(function (err, res) {
                if (err) {
                    console.log(err);
                    return;
                }
                self.set("user", res._links.self.href);
            });
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