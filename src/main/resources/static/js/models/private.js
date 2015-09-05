var Backbone = require("backbone"),
    $ = require("jquery");
    require("backbone-relational");
    require("backbone-relational-hal");

var config = require("../config.js"),
    rootUri = config.rootUri,
    prefix = config.prefix,
    resource = require("../controller/resource.js");

var AdsModel = Backbone.RelationalHalResource.extend({
    initialize: function () {
        resource.getRootHal(function (halUrl) {
            this.halUrl = halUrl;
            $.extend(this.defaults, {
                _links: {
                    self: {
                        href: halUrl
                    }
                }
            });
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

var ViewModel = Backbone.RelationalHalResource.extend({
    initialize: function() {
        resource.getMyUri(function (uri) {
            this.url = uri;
            this.fetch();
        }.bind(this));
    }
});

module.exports = {
    AdsModel: AdsModel,
    ViewModel: ViewModel
};