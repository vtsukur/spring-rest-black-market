var $ = require("jquery"),
    _ = require("underscore"),
    Backbone = require("backbone");

var View = Backbone.View.extend({
    el: $(".container"),
    initialize: function () {
        _.bindAll(this, "render");
        this.model.bind("change", this.render);
    },
    render: function () {
        var $tbody = this.$("#ads-list tbody"),
            next = this.$("#next"),
            prev = this.$("#prev");
        $tbody.empty();
        _.each(this.model.embedded("currency-black-market:ads"), function (data) {
            $tbody.append(new AdView({model: data}).render().el);
        }, this);
        this.model.link("next") ? next.show() : next.hide();
        this.model.link("prev") ? prev.show() : prev.hide();
    },
    events: {
        "click #next": "goNext",
        "click #prev": "goPrev"
    },
    goNext: function (e) {
        e.preventDefault(e);
        this.go("next");
    },
    goPrev: function (e) {
        e.preventDefault(e);
        this.go("prev");
    },
    go: function (where) {
        var data = URI(this.model.link(where).href()).query(true);
        ads.fetch({data: data});
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

module.export = {
    View: View,
    AdView: AdView
};