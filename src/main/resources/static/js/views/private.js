var $ = require("jquery"),
        _ = require("underscore"),
        Backbone = require("backbone"),
        Backform = require("backform");

    var View = Backbone.View.extend({
        el: $(".container"),
        initialize: function (options) {
            this.controller = options.controller;
            _.bindAll(this, "render");
            var self = this;
            this.model.bind("sync", function () {
                self.render(this.embedded("currency-black-market:ads"));
                self.createNew();
            });
            this.controller.fetchFields(function (fields) {
                this.form = new FormView({
                    model: options.adModel,
                    fields: fields,
                    controller: this.controller
                });
                this.form.render();
            }.bind(this));
        },
        status: $("#status"),
        render: function (models) {
            var $tbody = this.$("#ads-list tbody");
            $tbody.empty();
            _.each(models, function (data) {
                $tbody.append(new AdView({
                    model: data,
                    controller: this.controller,
                    form: this.form
                }).render().el);
            }, this);
        },
        createNew: function (e) {
            if (e) e.preventDefault();
            this.$el.find("tr").removeClass("highlight");
            this.form.model.set(this.form.model.defaults);
            this.form.$el.find(".form-group.ctrl:not(.create)").addClass("hide");
            this.form.$el.find(".create").removeClass("hide");
        },
        events: {
            "click #createNew": this.createNew
        }
    });

    var AdView = Backbone.View.extend({
        initialize: function (options) {
            this.controller = options.controller;
            this.form = options.form;
        },
        tagName: "tr",
        template: _.template($("#ad-template").html()),
        render: function () {
            this.$el.html(this.template(this.model));
            return this;
        },
        events: {
            "click": function () {
                this.form.model.set(this.model);
                this.form.model.set("location.area", this.model.location.area)
                this.form.model.set("location.city", this.model.location.city)
                this.form.model.set("_links", this.model._links);
                this.controller.getOperations(this.model, this.form);
                this.$el.addClass("highlight").siblings().removeClass("highlight");
            }
        }
    });

    var FormView = Backform.Form.extend({
        initialize: function (options) {
            this.controller = options.controller;
            Backform.Form.prototype.initialize.apply(this, arguments);
        },
        el: $("#form"),
        normalizeModel: function () {
            this.model.set("locatioin.area", undefined);
            this.model.set("locatioin.city", undefined);
        },
        events: {
            "click .update": function (e) {
                e.preventDefault();
                this.normalizeModel();
                this.controller.makeAction("update", this.model);
                return false;
            },
            "click .create": function (e) {
                e.preventDefault();
                this.normalizeModel();
                this.controller.makeAction("create", this.model);
                return false;
            },
            "click .publish": function (e) {
                e.preventDefault();
                this.normalizeModel();
                this.controller.makeAction("publish", this.model);
                return false;
            },
            "click .expire": function (e) {
                e.preventDefault();
                this.normalizeModel();
                this.model.set("status", "OUTDATED");
                this.controller.makeAction("expire", this.model);
                return false;
            },
            "click .delete": function (e) {
                e.preventDefault();
                this.normalizeModel();
                this.controller.makeAction("delete", this.model);
                return false;
            }
        }
    });

module.exports = {
    View: View
};