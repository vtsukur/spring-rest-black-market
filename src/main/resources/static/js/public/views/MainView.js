var $ = require("jquery"),
    _ = require("underscore"),
    Backbone = require("backbone");

var View = Backbone.View.extend({
    el: $(".container"),
    initialize: function (options) {
        this.controller = options.controller;
        this.AdView = require("./AdView.js");
        _.bindAll(this, "render");
        this.model.bind("change", this.render);
    },
    $tbody: $("#ads-list tbody"),
    render: function () {
        this.$tbody.empty();
        var models = this.controller.getModelItems(this.model);
        models && models.forEach(function (model) {
            this.$tbody.append(new this.AdView({ model: model }).render().el);
        }.bind(this));
        this.$("#next").prop("disabled", !this.model.link("next"));
        this.$("#prev").prop("disabled", !this.model.link("prev"));
    },
    events: {
        "click .navigation": function(e) {
            e.preventDefault(e);
            this.go(e.target.id);
        }
    },
    go: function (where) {
        var href = this.model.link(where).href(),
            data = this.controller.getLinkToGo(href);
        this.model.fetch({data: data});
    }
});

module.exports = View;