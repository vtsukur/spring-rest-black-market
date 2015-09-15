var $ = require("jquery"),
    Backbone = require("backbone"),
    Backform = require("backform"),
    resource = require("../../controller/resource.js"),
    FormView;

FormView = Backform.Form.extend({
    initialize: function (options) {
        var args = arguments;
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
            Backform.Form.prototype.initialize.apply(this, args);
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
        this.controller.getOperations(this.model, function(status, expire) {
            var disable = true;
            if (status.length && !expire) {
                disable = false;
            }
            this.hideButtons(status);
            this.disableFields(disable);
        }.bind(this));
    },
    hideButtons: function(operations) {
        this.$el.find(".ctrl").addClass("hide");
        operations.forEach(function (operation) {
            var button = this.$el.find("." + operation),
                link = this.controller.getOperationLink(operation);
            if (this.model.hasLink(link)) {
                button.removeClass("hide")
            }
        }.bind(this));
    },
    events: {
        "click .ctrl": function (e) {
            e.preventDefault();
            this.controller.makeAction(e.target.name.split(" ")[0], this.model, this.vm);
        }
    }
});

module.exports = FormView;