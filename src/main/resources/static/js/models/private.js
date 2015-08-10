var Backbone = require("backbone");
    require("backbone-relational");
    require("backbone-relational-hal");

var rootUri = '/',
    JsonHalAdapter = require("traverson-hal"),
    traverson = require("traverson");
    //TODO remove that -^

traverson.registerMediaType(JsonHalAdapter.mediaType,
    JsonHalAdapter);

var api = traverson.from(rootUri);

var AdsModel = Backbone.RelationalHalResource.extend({
    initialize: function () {
        var self = this;
        api.jsonHal()
            .follow("currency-black-market:ads")
            .getUri(function (err, uri) {
                if (err) {
                    console.log(err);
                    return;
                }
                self.halUrl = uri;
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