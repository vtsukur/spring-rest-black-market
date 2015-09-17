var $ = require("jquery"),
    _ = require("underscore"),
    Backbone = require("backbone"),
    AdView = Backbone.View.extend({
        tagName: "tr",
        template: _.template($("#ad-template").html()),
        render: function () {
            this.$el.html(this.template(this.model));
            return this;
        }
});

module.exports = AdView;