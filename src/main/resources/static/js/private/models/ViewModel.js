var Backbone = require("backbone"),
    $ = require("jquery"),
    resource = require("../../controller/resource.js"),
    ViewModel;

require("backbone-relational");
require("backbone-relational-hal");

ViewModel = Backbone.RelationalHalResource.extend({
    initialize: function() {
        resource.getMyUri(function (uri) {
            this.url = uri;
            this.fetch();
        }.bind(this));
    }
});

module.exports = ViewModel;