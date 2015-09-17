var Backbone = require("backbone"),
    $ = require("jquery"),
    resource = require("../../controller/resource.js"),
    AdModel;

require("backbone-relational");
require("backbone-relational-hal");

AdModel = Backbone.RelationalHalResource.extend({
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

module.exports = AdModel;