module.exports = function() {
    var rootUri = '/',
        JsonHalAdapter = require("traverson-hal"),
        $ = require("jquery"),
        _ = require("underscore"),
        Backbone = require("backbone"),
        traverson = require("traverson"),
        api = traverson.from(rootUri);
    require("backbone-relational");
    require("backbone-relational-hal");

    traverson.registerMediaType(JsonHalAdapter.mediaType,
        JsonHalAdapter);

    var AdsModel = Backbone.RelationalHalResource.extend({});

    var ads = new AdsModel();

    api.jsonHal()
        .follow('currency-black-market:ads')
        .getUri(function (err, uri) {
            if (err) {
                console.log(err);
                return;
            }
            ads.url = uri;
            ads.fetch();
        });
    var View = require("./views/public.js");
    new View({model: ads}).render();
};