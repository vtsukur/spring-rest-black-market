var $ = require("jquery"),
        _ = require("underscore"),
        Backbone = require("backbone"),
        Backform = require("backform");

    // Temporary fix until npm is outdated;
    Backform.ButtonControl.prototype.template = _.template([
        '<label class="<%=Backform.controlLabelClassName%>">&nbsp;</label>',
        '<div class="<%=Backform.controlsClassName%>">',
        '  <button type="<%=type%>" name="<%=name%>" class="btn <%=extraClasses.join(\' \')%>" <%=disabled ? "disabled" : ""%> ><%=label%></button>',
        '  <% var cls = ""; if (status == "error") cls = Backform.buttonStatusErrorClassName; if (status == "success") cls = Backform.buttonStatusSuccessClassname; %>',
        '  <span class="status <%=cls%>"><%=message%></span>',
        '</div>'
    ].join("\n"));


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
        createNew: function () {
            this.$el.find("tr").removeClass("highlight");
            this.controller.setDefault(this.form.model);
            this.form.model.set(this.form.model.defaults);
            this.form.$el.find(".form-group.ctrl:not(.create)").addClass("hide");
            this.form.$el.find(".create").removeClass("hide");
        },
        events: {
            "click #createNew": function() {
                this.createNew();
            }
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
        events: {
            "click .update": function (e) {
                e.preventDefault();
                this.controller.makeAction("update", this.model);
                return false;
            },
            "click .create": function (e) {
                e.preventDefault();
                this.controller.makeAction("create", this.model);
                return false;
            },
            "click .publish": function (e) {
                e.preventDefault();
                this.controller.makeAction("publish", this.model);
                return false;
            },
            "click .expire": function (e) {
                e.preventDefault();
                this.model.set("status", "OUTDATED");
                this.controller.makeAction("expire", this.model);
                return false;
            },
            "click .delete": function (e) {
                e.preventDefault();
                this.controller.makeAction("delete", this.model);
                return false;
            }
        }
    });

module.exports = {
    View: View
};