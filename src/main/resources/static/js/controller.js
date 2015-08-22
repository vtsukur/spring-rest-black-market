var Controller = function (controllerConfig) {
    this.status = $("#status");
};


Controller.prototype.getFields = function (callback) {
    var JsonHalAdapter = require("traverson-hal"),
        fields = require("./formFields.js"),
        traverson = require("traverson");

    traverson.registerMediaType(JsonHalAdapter.mediaType,
        JsonHalAdapter);

    var api = traverson.from('/');
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
                            callback.call(this, fields);
                        }
                    });
                });
            });
        });
};

Controller.prototype.getOperations = function (model, form) {
    this.setModel(model);
    ["update", "create", "publish", "delete", "expire"].forEach(function (relation) {
        this.initOperation(model, relation, form);
    }, this);
};

Controller.prototype.initOperation = function (model, relation, form) {
    var el = form.$el.find("." + relation);
    model.hasLink("currency-black-market:" + relation) ? el.removeClass("hide") : el.addClass("hide");
};

Controller.prototype.createNew = function () {
    this.setModel(this.ad);
};

Controller.prototype.setModel = function (model) {
    this.model = model;
};

Controller.prototype.getModel = function () {
    return this.model;
};

Controller.prototype.makeAction = function (action, data) {
    var self = this,
        model = this.getModel(),
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

    if (action !== "create") {
        options = {
            url: model.link("currency-black-market:" + action).href()
        }
    }
    model.set(data, {silent: true});
    model.sync(actions[action], model, options)
        .done(function () {
            ordersResource.updateCollection();
            self.createNew();
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