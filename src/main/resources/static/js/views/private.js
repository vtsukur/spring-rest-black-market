//function App(controller) {

    var $ = require("jquery"),
        _ = require("underscore"),
        Backbone = require("backbone"),
        Backform = require("backform");

    var View = Backbone.View.extend({
        el: $(".container"),
        initialize: function (options) {
            this.controller = options.controller;
            _.bindAll(this, "render");
            this.model.bind("change reset", this.render);
            var form = new Form({model: this.model, controller: this.controller});
            form.render();
        },
        render: function () {
            var $tbody = this.$("#ads-list tbody");
            $tbody.empty();
            _.each(this.model.models, function (data) {
                $tbody.append(new AdView({model: data, controller: this.controller}).render().el);
            }, this);
        },
        events: {
            "click #createNew": function (e) {
                this.$el.find("tr").removeClass("highlight");
                e.preventDefault();
                this.controller.createNew();
            }
        }
    });

    var AdView = Backbone.View.extend({
        initialize: function (options) {
            this.controller = options.controller;
        },
        tagName: "tr",
        template: _.template($("#ad-template").html()),
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        events: {
            "click": function () {
                this.controller.getForm().model.set(this.model.toJSON());
                this.controller.getOperations(this.model);
                this.$el.addClass("highlight").siblings().removeClass("highlight");
            }
        }
    });

    var Form = Backform.Form.extend({
        initialize: function (options) {
            var self = this;
            this.controller = options.controller;
            this.controller.getFields(function (model, fields) {
                console.log(model, fields);
                self.fields = fields;
            });
        },
        el: $("#form"),
        fields: [{
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
            name: "ctrl delete hide",
            control: "button",
            label: "Удалить"
        }, {
            name: "ctrl expire hide",
            control: "button",
            label: "Закрыть"
        }, {
            name: "ctrl publish hide",
            control: "button",
            label: "Опубликовать"
        }],
        events: {
            "click .update": function (e) {
                e.preventDefault();
                this.controller.makeAction("update", this.model.toJSON());
                return false;
            },
            "click .create": function (e) {
                e.preventDefault();
                this.model.set("user", this.controller.ordersResource.get("user"));
                this.controller.makeAction("create", this.model.toJSON());
                return false;
            },
            "click .publish": function (e) {
                e.preventDefault();
                this.controller.makeAction("publish", this.model.toJSON());
                return false;
            },
            "click .expire": function (e) {
                e.preventDefault();
                this.model.set("status", "OUTDATED");
                this.controller.makeAction("expire", this.model.toJSON());
                return false;
            },
            "click .delete": function (e) {
                e.preventDefault();
                this.controller.makeAction("delete", this.model.toJSON());
                return false;
            }
        }
    });
//}
module.exports = {
    View: View
};