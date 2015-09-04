/**
 * Created by my8bit on 8/4/15.
 */
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
                self.fetch();
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