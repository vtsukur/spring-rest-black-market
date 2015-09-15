var $ = require("jquery"),
    _ = require("underscore"),
    Backbone = require("backbone"),
    Backform = require("backform"),
    resource = require("../../controller/resource.js"),
    AdView;

AdView = Backbone.View.extend({
    tagName: "tr",
    template: _.template($("#ad-template").html()),
    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },
    events: {
        "click": function () {
            this.$el.addClass("highlight").siblings().removeClass("highlight");
        }
    }
});

module.exports = AdView;