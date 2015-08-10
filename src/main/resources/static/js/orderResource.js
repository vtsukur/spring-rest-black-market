var Resource = function(api, AdsModel, adsCollection) {
    var Backbone = require("backbone"),
        prefix = "currency-black-market:",
        OrdersResource;
        require("backbone-relational");
        require("backbone-relational-hal");

    OrdersResource = Backbone.RelationalHalResource.extend({
        initialize: function () {
            var self = this;
            api.jsonHal()
                .follow(prefix + "ads", "search", prefix + "my")
                .getUri(function (err, uri) {
                    if (err) {
                        console.log(err);
                        return;
                    }
                    self.url = uri;
                    self.updateCollection();
                });
            api.jsonHal()
                .follow(prefix + "users", "search", prefix + "current-user")
                .getResource(function (err, res) {
                    if (err) {
                        console.log(err);
                        return;
                    }
                    self.set("user", res._links.self.href); //TODO save and use for make create
                });
        },

        updateCollection: function () {
            var self = this;
            this.fetch().done(function () {
                var models = self.embedded(prefix + "ads", { all: true });
                models = models.map(function (model) {
                    return new AdsModel(model);
                });
                adsCollection.reset(models);
            });
        }
    });

    return OrdersResource;
};

module.exports = Resource;