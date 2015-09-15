var prefix = require("../config.js").prefix,
    URI = require("URIjs");

module.exports = {

    getOperations: function (model, callback) {
        callback(["update", "create", "publish", "delete", "expire"].filter(function(operation) {
            return model.hasLink(prefix + operation);
        }), model.hasLink(prefix + "expire"));
    },

    getOperationLink: function (operation) {
        return prefix + operation;
    },

    getLinkToGo: function (href) {
        return URI(href).query(true);
    },

    initOperation: function (model, relation, form) {
        var ctrl = form.$el.find("." + relation);
        form.model.hasLink(prefix + relation) ? ctrl.removeClass("hide") : ctrl.addClass("hide");
    },

    getModelItems: function (model) {
        return model.embedded(prefix + "ads");
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