var Backbone = require("backbone"),
    resource = require("../../controller/resource.js"),
    AdsModel;

require("backbone-relational");
require("backbone-relational-hal");

AdsModel = Backbone.RelationalHalResource.extend({
    initialize: function () {
        resource.getRootHal(function (uri) {
            this.url = uri;
            this.fetch();
        }.bind(this));
    }
});

module.exports = AdsModel;