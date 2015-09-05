var $ = require("jquery"),
    _ = require("underscore"),
    Backbone = require("backbone"),
    Backform = require("backform"),
    prefix = require("../config.js").prefix,
    resource = require("../controller/resource.js");

    var View = Backbone.View.extend({
        el: $(".container"),
        initialize: function (options) {
            _.bindAll(this, "render");
            var self = this;
            this.model.bind("sync", function () {
                self.render(this.embedded(prefix + "ads"));
                self.createNew();
                self.updateStatus()
            });
            this.form = new FormView({
                vm: this.model,
                model: options.adModel,
                controller: options.controller
            });
        },
        status: $("#status"),
        updateStatus: function () {
            var status = this.model.get("status");
            if (status) {
                this.status.text("Ваша заявка успешно " + status);
                setTimeout(function () {
                    this.status.text("");
                }.bind(this), 2000);
            }
        },
        render: function (models) {
            var self = this,
                $tbody = this.$("#ads-list tbody");
            $tbody.empty();
            _.each(models, function (data) {
                $tbody.append(new AdView({
                    model: data,
                    events: {
                        "click": function() {
                            self.form.updateState(this.model);
                        }
                    }
                }).render().el);
            }, this);
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

    var AdView = Backbone.View.extend({
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

    var FormView = Backform.Form.extend({
        initialize: function (options) {
            var arg = arguments;
            this.vm = options.vm;
            this.controller = options.controller;

            var fields = [{
                    name: "amount",
                    label: "Количество:",
                    control: "input",
                    type: "number"
                }, {
                    name: "currency",
                    label: "Тип валюты:",
                    control: "select"
                }, {
                    name: "rate",
                    label: "Курс:",
                    control: "input",
                    type: "number"
                }, {
                    name: "type",
                    label: "Тип ордера:",
                    control: "select"
                }, {
                    name: "location.city",
                    label: "Город:",
                    control: "input"
                }, {
                    name: "location.area",
                    label: "Район:",
                    control: "input"
                }, {
                    name: "comment",
                    label: "Комментарий:",
                    control: "input"
                }, {
                    name: "create ctrl",
                    control: "button",
                    label: "Создать"
                }, {
                    name: "update ctrl hide",
                    control: "button",
                    label: "Обновить"
                }, {
                    name: "delete ctrl hide",
                    control: "button",
                    label: "Удалить"
                }, {
                    name: "expire ctrl hide",
                    control: "button",
                    label: "Закрыть"
                }, {
                    name: "publish ctrl hide",
                    control: "button",
                    label: "Опубликовать"
                }];

            resource.getFields(fields, function(fields) {
                this.fields = fields;
                Backform.Form.prototype.initialize.apply(this, arg);
                this.render();
            }.bind(this));
        },
        el: $("#form"),
        disableFields: function(status) {
            this.$el.find(".form-control:not(.ctrl)").attr("disabled", status);
        },
        createNew: function () {
            this.model.set(this.model.defaults);
            this.$el.find(".form-group.ctrl:not(.create)").addClass("hide");
            this.$el.find(".create").removeClass("hide");
            this.disableFields(false);
        },
        updateState: function (model) {
            this.model.set(model);
            this.model.set("_links", model._links);
            this.controller.getOperations(model, this);
            //this.controller.getOperations2(this.model, function(status, expire) {
            //    var disable = true;
            //    if (status.length && !expire) {
            //        disable = false;
            //        this.hideButtons(status);
            //    }
            //    this.disableFields(disable);
            //}.bind(this));
        },
        /*hideButtons: function(operations) {
            //Need to filter only items in array and disable others
            operations.forEach(function (operation) {
                var button = this.$el.find("." + operation);
                this.model.hasLink(prefix + operation) ? button.removeClass("hide") : button.addClass("hide");
            },bind(this));
        },*/
        events: {
            "click .ctrl": function (e) {
                e.preventDefault();
                this.controller.makeAction(e.target.name.split(" ")[0], this.model, this.vm);
            }
        }
    });

module.exports = View;