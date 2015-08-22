var Controller = function (controllerConfig) {
    this.adsCollection = controllerConfig.adsCollection;
    this.ad = controllerConfig.ad;
    this.Model = controllerConfig.Model;
    this.status = $("#status");
    this.prefix = "currency-black-market:";
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
                .follow(self.prefix + "ads", "search", self.prefix + "my")
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
    var views = require("./views/private.js");
    new views.View({
        model: this.resource,
        adModel: this.ad,
        controller: this
    }).render();
};

Controller.prototype.fetchFields = function (callback) {
    var fields = require("./formFields.js");

    this.api.jsonHal()
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
    ["update", "create", "publish", "delete", "expire"].forEach(function (relation) {
        this.initOperation(model, relation, form);
    }, this);
};

Controller.prototype.initOperation = function (model, relation, form) {
    var el = form.$el.find("." + relation);
    form.model.hasLink(this.prefix + relation) ? el.removeClass("hide") : el.addClass("hide");
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

    if (action !== "create") {
        options = {
            url: model.link(this.prefix + action).href()
        }
    }
    //TODO fix dirty trick
    model.get("location").city = "Roma";
    model.get("location").area = "Vatican";
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