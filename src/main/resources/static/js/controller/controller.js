var prefix = "currency-black-market:",
    Controller = function () {
    this.status = $("#status");
    var rootUri = '/',
        JsonHalAdapter = require("traverson-hal"),
        traverson = require("traverson");

    traverson.registerMediaType(JsonHalAdapter.mediaType,
        JsonHalAdapter);

    this.api = traverson.from(rootUri);
    var self = this;
    var Resource = Backbone.RelationalHalResource.extend({
        initialize: function() {
            self.api.jsonHal()
                .follow(prefix + "ads", "search", prefix + "my")
                .getUri(function (err, uri) {
                    if (err) {
                        console.log(err);
                        return;
                    }
                    this.url = uri;
                    this.fetch();
                }.bind(this));
        }
    });
    this.resource = new Resource();
};

Controller.prototype.getOperations = function (model, form) {
    var allOperations = ["update", "create", "publish", "delete", "expire"],
        fields = form.$el.find(".form-control:not(.ctrl)");


    fields.attr("disabled", !allOperations.filter(function(operation) {
            this.initOperation(model, operation, form);
            return form.model.hasLink(prefix + operation);
        }.bind(this)).length || form.model.hasLink(prefix + "expire"));
};

Controller.prototype.getOperations2 = function (model, callback) {
    callback(["update", "create", "publish", "delete", "expire"].filter(function(operation) {
        return model.hasLink(prefix + operation);
        }), model.hasLink(prefix + "expire"));
};

Controller.prototype.initOperation = function (model, relation, form) {
    var ctrl = form.$el.find("." + relation);
    form.model.hasLink(prefix + relation) ? ctrl.removeClass("hide") : ctrl.addClass("hide");
};

Controller.prototype.makeAction = function (action, model) {
    var self = this,
        options = {},
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

    if (action !== "create") options = { url: model.link(prefix + action).href() };
    if (action === "expire") model.set("status", "OUTDATED");

    model.sync(actions[action], model, options)
        .done(function () {
            self.resource.fetch();
            self.status.text("Ваша заявка успешно " + status[action]);
            setTimeout(function () {
                self.status.text("");
            }, 2000);
        })
        .fail(function (error) {
            console.error(error);
        });
};

module.exports = Controller;