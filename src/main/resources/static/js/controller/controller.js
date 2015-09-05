var prefix = require("../config.js").prefix;

module.exports = {

    getOperations: function (model, form) {
        var allOperations = ["update", "create", "publish", "delete", "expire"],
            fields = form.$el.find(".form-control:not(.ctrl)");


        fields.attr("disabled", !allOperations.filter(function(operation) {
                this.initOperation(model, operation, form);
                return form.model.hasLink(prefix + operation);
            }.bind(this)).length || form.model.hasLink(prefix + "expire"));
    },

    getOperations2: function (model, callback) {
        callback(["update", "create", "publish", "delete", "expire"].filter(function(operation) {
            return model.hasLink(prefix + operation);
        }), model.hasLink(prefix + "expire"));
    },

    initOperation: function (model, relation, form) {
        var ctrl = form.$el.find("." + relation);
        form.model.hasLink(prefix + relation) ? ctrl.removeClass("hide") : ctrl.addClass("hide");
    },

    makeAction: function (action, model, vm) {
        var options = {},
            actions = {
                "create" : "create",
                "update" : "patch",
                "publish": "create",
                "expire" : "create",
                "delete" : "delete"
            },
            status = {
                "create" : "создана",
                "update" : "обновлена",
                "publish": "опубликована",
                "expire" : "закрыта",
                "delete" : "удалена"
            };

        if (action !== "create") options = {
            url: model.link(prefix + action).href()
        };

        model.sync(actions[action], model, options)
            .done(function () {
                vm.set("status", status[action]);
                vm.fetch();
            })
            .fail(function (error) {
                console.error(error);
            });
    }
};