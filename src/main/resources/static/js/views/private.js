var $ = require("jquery"),
        _ = require("underscore"),
        Backbone = require("backbone"),
        Backform = require("backform");

var rootUri = "/",
    prefix = "currency-black-market:",
    JsonHalAdapter = require("traverson-hal"),
    traverson = require("traverson");

traverson.registerMediaType(JsonHalAdapter.mediaType,
    JsonHalAdapter);

var api = traverson.from(rootUri);

    var View = Backbone.View.extend({
        el: $(".container"),
        initialize: function (options) {
            _.bindAll(this, "render");
            var self = this;
            this.model.bind("sync", function () {
                self.render(this.embedded(prefix + "ads"));
                self.createNew();
            });
            this.form = new FormView({
                model: options.adModel,
                controller: options.controller
            });
        },
        status: $("#status"),
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
                }],
                arg = arguments,
                self = this;

            api.jsonHal()
                .follow("profile")
                .getResource(function (err, res) {
                    if (err) {
                        console.log(err);
                        return;
                    }
                    var descriptors = res.descriptors,
                        getField = function (filedName) {
                            return fields.filter(function (el) {
                                return el.name === filedName;
                            })[0];
                        };
                    descriptors.forEach(function (el) {
                        $.ajax({
                            url: el.href
                        }).done(function (res) {
                            $.each(res.descriptors, function (key, value) {
                                if (value.id === "ad-representation") {
                                    $.each(value.descriptors, function (key, value) {
                                        if (value.name === "currency" || value.name === "type") {
                                            getField(value.name).options = value.doc.value.split(",").map(function (el) {
                                                var value = el.trim();
                                                return {label: value, value: value};
                                            });
                                        }
                                    });
                                    self.fields = fields;
                                    Backform.Form.prototype.initialize.apply(self, arg);
                                    self.render();
                                }
                            });
                        });
                    });
                });
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
                this.controller.makeAction(e.target.name.split(" ")[0], this.model);
            }
        }
    });

module.exports = View;