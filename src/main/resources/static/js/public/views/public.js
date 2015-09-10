var $ = require("jquery"),
    _ = require("underscore"),
    prefix = require("../../config.js").prefix,
    URI = require("URIjs");
    Backbone = require("backbone");

var View = Backbone.View.extend({
    el: $(".container"),
    initialize: function (options) {
        this.controller = options.controller;
        _.bindAll(this, "render");
        this.model.bind("change", this.render);
    },
    $tbody: $("#ads-list tbody"),
    render: function () {
        this.$tbody.empty();
        var models = this.controller.getModelItems(this.model);
        models.forEach(function (model) {
            this.$tbody.append(new AdView({ model: model }).render().el);
        }.bind(this));
        /*
        _.each(this.model.embedded(prefix + "ads"), function (data) {
            $tbody.append(new AdView({model: data}).render().el);
        }, this);
        */
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
        var data = URI(this.model.link(where).href()).query(true);
        this.model.fetch({data: data});
    }
});

var AdView = Backbone.View.extend({
    tagName: "tr",
    template: _.template($("#ad-template").html()),
    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    }
});

module.exports = View;