var Controller = function (controllerConfig) {
    this.status = $("#status");
    this.initialize(controllerConfig);
};


Controller.prototype.getFields = function (callback) {
    setTimeout(function() {
        callback.call(1,2);
    }, 5000);
};
Controller.prototype.initialize = function (config) {
    var self = this,
        api = config.api,
        fields = require("./formFields.js");

    self.ad = config.ad;
    self.ordersResource = config.ordersResource;

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
                            //self.renderForm(self.ad, fields);
                        }
                    });
                });
            });
        });
};

Controller.prototype.getOperations = function (model) {
    this.setModel(model);
    ["update", "create", "publish", "delete", "expire"].forEach(function (relation) {
        this.initOperation(model, relation);
    }, this);
};

Controller.prototype.initOperation = function (model, relation) {
    var el = this.getForm().$el.find("." + relation);
    model.hasLink("currency-black-market:" + relation) ? el.removeClass("hide") : el.addClass("hide");
};

Controller.prototype.createNew = function () {
    this.setModel(this.ad);
    this.getForm().model.set(this.ad.defaults);
    this.getForm().model.set("user", this.ordersResource.get("user"));
    this.getForm().$el.find(".form-group.ctrl:not(.create)").addClass("hide");
    this.getForm().$el.find(".create").removeClass("hide");
};

Controller.prototype.setModel = function (model) {
    this.model = model;
};

Controller.prototype.getModel = function () {
    return this.model;
};

Controller.prototype.getForm = function () {
    return this.form || {};
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

Controller.prototype.renderForm = function (model, fields) {
    var self = this,
        Backform = require("backform");

    this.form = new Backform.Form({
        el: $("#form"),
        model: model,
        fields: fields,
        events: {
            "click .update": function (e) {
                e.preventDefault();
                self.makeAction("update", this.model.toJSON());
                return false;
            },
            "click .create": function (e) {
                e.preventDefault();
                this.model.set("user", self.ordersResource.get("user"));
                self.makeAction("create", this.model.toJSON());
                return false;
            },
            "click .publish": function (e) {
                e.preventDefault();
                self.makeAction("publish", this.model.toJSON());
                return false;
            },
            "click .expire": function (e) {
                e.preventDefault();
                this.model.set("status", "OUTDATED");
                self.makeAction("expire", this.model.toJSON());
                return false;
            },
            "click .delete": function (e) {
                e.preventDefault();
                self.makeAction("delete", this.model.toJSON());
                return false;
            }
        }
    }).render();
};

module.exports = Controller;