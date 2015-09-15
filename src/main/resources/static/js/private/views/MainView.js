var $ = require("jquery"),
    _ = require("underscore"),
    Backbone = require("backbone"),
    Backform = require("backform"),
    prefix = require("../../config.js").prefix,
    resource = require("../../controller/resource.js");

var MainView = Backbone.View.extend({
    el: $(".container"),
    $status: $("#status"),
    $tbody: $("#ads-list tbody"),
    initialize: function (options) {
        _.bindAll(this, "render");
        var controller = options.controller;
        this.AdView = require("./AdView.js");
        this.model.bind("sync", function (model) {
            this.render(controller.getModelItems(model));
            this.createNew();
            this.updateStatus();
        }.bind(this));
        var FormView = require("./FormView.js"),
            AdModel = require("../models/AdModel.js");
        this.form = new FormView({
            vm: this.model,
            model: new AdModel(),
            controller: options.controller
        });
    },
    updateStatus: function () {
        var status = this.model.get("status");
        if (status) {
            this.$status.text("Ваша заявка успешно " + status);
            setTimeout(function () {
                this.$status.text("");
            }.bind(this), 2000);
        }
    },
    render: function (models) {
        var self = this;
        this.$tbody.empty();
        models && models.forEach(function (model) {
            this.$tbody.append(new this.AdView({
                model: model,
                events: {
                    "click": function () {
                        self.form.updateState(this.model);
                    }
                }
            }).render().el);
        }.bind(this));
    },
    createNew: function () {
        this.$el.find("tr").removeClass("highlight");
        this.form.createNew();
    },
    events: {
        "click #createNew": function () {
            this.createNew();
        }
    }
});

module.exports = MainView;